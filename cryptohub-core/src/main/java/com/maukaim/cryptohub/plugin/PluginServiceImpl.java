package com.maukaim.cryptohub.plugin;

import com.google.common.collect.Maps;
import com.maukaim.cryptohub.plugins.api.plugin.Module;
import com.maukaim.cryptohub.plugins.core.PluginJarFinder;
import com.maukaim.cryptohub.plugins.core.PluginJarFinderImpl;
import com.maukaim.cryptohub.plugins.core.PluginLifeCycleListener;
import com.maukaim.cryptohub.plugins.core.PluginService;
import com.maukaim.cryptohub.plugins.core.model.Plugin;
import com.maukaim.cryptohub.plugins.core.model.module.ModuleProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PluginServiceImpl implements PluginService {

    Map<String, List<PluginLifeCycleListener>> pluginListeners = Maps.newConcurrentMap();

    @Getter
    private PluginJarFinder pluginJarFinder;
    @Value("cryptohub.plugins.repository")
    private String repositories;
    public PluginServiceImpl(){
        List<Path> pluginRoots = Collections.emptyList(); //Stream.of(repositories).map(r-> Path.of(URI.create(r))).collect(Collectors.toList());
        this.pluginJarFinder = new PluginJarFinderImpl(pluginRoots, this);
    }


    @Override
    public <T extends Module> List<ModuleProvider<? extends T>> getProviders(Class<T> moduleInterface) {
        return null;
    }

    @Override
    public Plugin get(String pluginId) {
        return null;
    }

    @Override
    public List<Plugin> getPlugins() {
        return null;
    }

    @Override
    public List<Plugin> loadPlugins() {
        return null;
    }

    @Override
    public Plugin loadPlugin(Path pluginPath) {
        return null;
    }

    @Override
    public void start(Plugin plugin) {

    }

    @Override
    public void stop(Plugin plugin) {

    }

    @Override
    public void destroy(Plugin plugin) {
    }

    @Override
    public void addLifeCycleListener(PluginLifeCycleListener listener, ModuleProvider<Module> module) {
        String pluginId = module.getModuleInfo().getPlugin().getInfo().getId();
        this.addLifeCycleListener(pluginId, listener);
    }

    @Override
    public void addLifeCycleListener(PluginLifeCycleListener listener, Plugin plugin) {
        String pluginId = plugin.getInfo().getId();
        this.addLifeCycleListener(pluginId, listener);
    }

    private void addLifeCycleListener(String pluginId, PluginLifeCycleListener listener){
        this.pluginListeners.putIfAbsent(pluginId, new ArrayList<>());
        this.pluginListeners.computeIfPresent(pluginId, (plugId, listeners) -> {
            listeners.add(listener);
            return listeners;
        });
    }

}
