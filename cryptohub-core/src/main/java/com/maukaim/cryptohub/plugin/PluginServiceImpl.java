package com.maukaim.cryptohub.plugin;

import com.google.common.collect.Maps;
import com.maukaim.cryptohub.plugins.api.plugin.Module;
import com.maukaim.cryptohub.plugins.core.*;
import com.maukaim.cryptohub.plugins.core.model.Plugin;
import com.maukaim.cryptohub.plugins.core.model.PluginLoadResult;
import com.maukaim.cryptohub.plugins.core.model.PluginStatus;
import com.maukaim.cryptohub.plugins.core.model.module.ModuleProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PluginServiceImpl implements PluginService {

    private Map<String, List<PluginLifeCycleListener>> pluginListeners = Maps.newConcurrentMap();
    private Map<String, Plugin> pluginsById = Maps.newConcurrentMap();

    @Getter
    private PluginLoadResult lastLoadingResult = PluginLoadResult.EMPTY;
    private PluginAutoEnableListener autoEnableListener;

    @Getter
    private final PluginLoader pluginLoader;

    @Getter
    private final PluginRepository pluginRepository;

    @Getter
    @Setter
    private List<Path> pluginRoots;

    public PluginServiceImpl(@Value("cryptohub.plugins.repository") String repository,
                             PluginRepository pluginRepository, PluginLoader pluginLoader) {
        this.pluginRepository = pluginRepository;
        this.pluginLoader = pluginLoader;

        //TODO: En faire une injection de property Spring
        this.pluginRoots = List.of(Paths.get("/Users/julienelkaim/Desktop/plugins"));
        this.autoEnableListener = new PluginAutoEnableListener(this.getPluginRepository());

    }

    @PostConstruct
    public void init() {
        log.info("Initializing PluginServiceImpl...");
        List<Plugin> pluginLoaded = this.loadPlugins();

        if (!CollectionUtils.isEmpty(pluginLoaded)) {
            this.getPlugins().forEach(plg ->
                    log.warn("Plugin {} [{}] <{}> is loaded at initialization.",
                            plg.getInfo().getName(),
                            plg.getInfo().getPluginId(),
                            plg.getStatus().name()));

            Set<String> enabledPluginIds = this.getPluginRoots().stream()
                    .map(path -> this.getPluginRepository().getEnabledPluginIds(path))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            log.info("List of auto enabled plugins -> {}", String.join(", ", enabledPluginIds));

            List<PluginLifeCycleException> startExceptions = pluginLoaded.stream()
                    .peek(plg -> plg.setStatus(PluginStatus.AVAILABLE))
                    .map(plg -> {
                        String pluginId = plg.getInfo().getPluginId();
                        if (enabledPluginIds.contains(pluginId)) {
                            log.info("Auto-start plugin {} with ID -> {}.", plg.getInfo().getName(), pluginId);
                            return this.start(pluginId);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull).map(StatusChangeResult::getExceptions)
                    .flatMap(Collection::stream).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(startExceptions)) {
                log.info("While auto-starting plugins available, errors occured :\n{}",
                        startExceptions.stream()
                                .map(ple -> String.format(" %s [is blocking -> %s]", ple.getReason(), ple.isBlocking().toString()))
                                .collect(Collectors.joining("\n")));
            }

        } else {
            log.info("At initialization, no plugins available.");
        }
        log.info("PluginServiceImpl Initialized !");
    }


    @Override
    public <T extends Module> List<ModuleProvider<? extends T>> getProviders(Class<T> moduleInterface) {
        return this.getPlugins().stream()
                .filter(plg -> plg.getStatus().isStarted())

                .map(Plugin::getModuleProviders)
                .flatMap(Collection::stream)
                .filter(moduleProvider -> {
                    Class<? extends Module> moduleClass = moduleProvider.getModuleClass();
                    return moduleInterface.isAssignableFrom(moduleClass);
                })
                .map(moduleProvider -> (ModuleProvider<? extends T>) moduleProvider)
                .collect(Collectors.toList());
    }

    @Override
    public Plugin get(String pluginId) {
        return this.pluginsById.get(pluginId);
    }

    @Override
    public Collection<Plugin> getPlugins() {
        return this.pluginsById.values();
    }

    @Override
    public List<Plugin> loadPlugins() {
        Set<File> pluginFiles = this.pluginRepository.getPlugins(this.pluginRoots);
        PluginLoadResult pluginLoadResult = this.pluginLoader.loadPlugins(pluginFiles);

        pluginLoadResult.getPlugins().forEach(plugin -> {
            this.pluginsById.putIfAbsent(plugin.getInfo().getPluginId(), plugin);
            this.addLifeCycleListener(this.autoEnableListener, plugin.getInfo().getPluginId());
        });
        this.lastLoadingResult = pluginLoadResult;

        return pluginLoadResult.getPlugins();
    }

    @Override
    public StatusChangeResult start(String pluginId) {
        Plugin plugin = this.get(pluginId);
        return this.changeStatus(plugin, PluginStatus.ENABLED,
                (plg) -> plg.getStatus().canBeStarted(), PluginStatus.ERROR);
    }

    @Override
    public StatusChangeResult stop(String pluginId) {
        log.info("DEBUG::: On va STOP le pluginId");

        Plugin plugin = this.get(pluginId);
        return this.changeStatus(plugin, PluginStatus.DISABLED, (plg) -> plg.getStatus().canBeStopped());

    }

    @Override
    public DestroyResult destroy(String pluginId) {
        Boolean isDestroyed = false;
        Plugin plugin = this.get(pluginId);
        List<PluginLifeCycleException> exceptions = new ArrayList<>();

        if (plugin.getStatus().isDestroyable()) {
            List<PluginLifeCycleListener> lifeCycleListeners = this.pluginListeners.get(plugin.getInfo().getPluginId());
            for (PluginLifeCycleListener lcl : lifeCycleListeners) {
                try {
                    lcl.beforeDestroy(plugin);
                } catch (PluginLifeCycleException e) {
                    exceptions.add(e);
                }
            }
            if (CollectionUtils.isEmpty(exceptions)) {
                try {
                    this.getPluginRepository().destroyPlugin(plugin);
                    isDestroyed = true;
                } catch (PluginLifeCycleException e) {
                    exceptions.add(e);
                } catch (Exception e) {
                    exceptions.add(new PluginLifeCycleException(
                            String.format("Unexpected error occured while destroying %s -> %s",
                                    plugin.getInfo().getName(), e.getMessage()),
                            e, true));
                }

                if (isDestroyed) {
                    for (PluginLifeCycleListener lcl : lifeCycleListeners) {
                        try {
                            lcl.afterDestroy(plugin);
                        } catch (Exception e) {/* Just ignore errors.*/}
                    }
                    this.pluginsById.remove(pluginId);
                }
            }
        } else {
            exceptions.add(new PluginLifeCycleException(
                    String.format("Can't destroy plugin %s. You should disable it before.", plugin.getInfo().getName())
            ));
        }
        return DestroyResult.builder()
                .destroyed(isDestroyed)
                .exceptions(exceptions)
                .build();
    }

    private StatusChangeResult changeStatus(Plugin plugin, PluginStatus newStatus,
                                            Function<? super Plugin, ? extends Boolean> condition) {
        return this.changeStatus(plugin, newStatus, condition, null);
    }

    private StatusChangeResult changeStatus(Plugin plugin, PluginStatus newStatus,
                                            Function<? super Plugin, ? extends Boolean> condition,
                                            PluginStatus fallbackStatus) {
        PluginStatus old = plugin.getStatus();
        List<PluginLifeCycleException> exceptions = new ArrayList<>();
        List<PluginLifeCycleListener> lifeCycleListeners = this.pluginListeners.getOrDefault(
                plugin.getInfo().getPluginId(), Collections.emptyList());

        if (Objects.isNull(condition) || condition.apply(plugin)) {
            for (PluginLifeCycleListener lcl : lifeCycleListeners) {
                try {
                    lcl.beforeStatusChange(plugin, plugin.getStatus(), newStatus);
                } catch (PluginLifeCycleException e) {
                    exceptions.add(e);
                }
            }

            if (CollectionUtils.isEmpty(exceptions)) {
                plugin.setStatus(newStatus);
            } else {
                plugin.setStatus(Objects.requireNonNullElse(fallbackStatus, plugin.getStatus()));
            }
        } else {
            exceptions.add(new PluginLifeCycleException(String.format(
                    "Not authorized to switch status %s -> %s of plugin %s",
                    plugin.getStatus(), newStatus, plugin.getInfo().getName())));
            plugin.setStatus(Objects.requireNonNullElse(fallbackStatus, plugin.getStatus()));
        }

        if (plugin.getStatus() != old) {
            for (PluginLifeCycleListener lcl : lifeCycleListeners) {
                try {
                    lcl.afterStatusChanged(plugin, old, plugin.getStatus());
                } catch (Exception e) {/* Just ignore errors.*/}
            }
        }

        return StatusChangeResult.builder()
                .current(plugin.getStatus())
                .previous(old)
                .exceptions(exceptions)
                .build();
    }

    @Override
    public void addLifeCycleListener(PluginLifeCycleListener listener, ModuleProvider<Module> module) {
        String pluginId = module.getModuleInfo().getPlugin().getInfo().getPluginId();
        this.addLifeCycleListener(listener, pluginId);
    }

    @Override
    public void addLifeCycleListener(PluginLifeCycleListener listener, String pluginId) {
        this.pluginListeners.putIfAbsent(pluginId, new ArrayList<>());
        this.pluginListeners.computeIfPresent(pluginId, (plugId, listeners) -> {
            listeners.add(listener);
            return listeners;
        });
    }


}
