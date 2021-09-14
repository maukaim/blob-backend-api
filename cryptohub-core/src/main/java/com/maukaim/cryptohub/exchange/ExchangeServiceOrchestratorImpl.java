package com.maukaim.cryptohub.exchange;

import com.google.common.collect.Maps;
import com.maukaim.cryptohub.plugins.api.exchanges.ExchangeService;
import com.maukaim.cryptohub.plugins.api.exchanges.ExchangeServicePreProcess;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.ExchangeConnectionException;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameter;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameters;
import com.maukaim.cryptohub.plugins.api.plugin.PreProcess;
import com.maukaim.cryptohub.plugins.core.PluginService;
import com.maukaim.cryptohub.plugins.core.model.module.ModuleInfo;
import com.maukaim.cryptohub.plugins.core.model.module.ModuleProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExchangeServiceOrchestratorImpl implements ExchangeServiceOrchestrator {
    private ExchangeServiceFactory factory;
    private PluginService pluginService;

    private Map<String, ExchangeWrapper> wrappersCached = Maps.newConcurrentMap();

    public ExchangeServiceOrchestratorImpl(
            @Autowired ExchangeServiceFactory factory,
            @Autowired PluginService pluginService) {
        this.pluginService = pluginService;
        this.factory = factory;
    }

    @PostConstruct
    public void init() {

        List<ModuleProvider<? extends ExchangeService>> providers = this.pluginService.getProviders(ExchangeService.class);
        if (!providers.isEmpty()) {
            List<ExchangeService> services = providers.stream().map(p -> this.factory.build(p)).collect(Collectors.toList());
            services.forEach(service -> {
                List<String> orderTypes = service.getOrderTypes(null);
                log.info("Plugin's ExchangeService provides the following orderTypes : {}", String.join(", ", orderTypes));
            });
        } else {
            log.info("NO EXCHANGE SERVICES MODULE PROVIDERS !!!!");
        }

    }

    @PreDestroy
    public void preDestroy() {
        this.wrappersCached.forEach((id, wrapper) -> wrapper.getService().disconnect());
        this.wrappersCached.clear();
    }

    private List<ModuleProvider<? extends ExchangeService>> getAllModuleProviders(){
        return this.pluginService.getProviders(ExchangeService.class);
    }
    @Override
    public List<ModuleInfo> getAvailableExchangesInfo() {
        return this.getAllModuleProviders().stream()
                .map(ModuleProvider::getModuleInfo)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<ConnectionParameter>> getConnectionParameters(ModuleProvider<? extends ExchangeService> exchangeProvider) {

        Class<? extends PreProcess> preProcess = exchangeProvider.getPreProcess();
        ExchangeServicePreProcess preProcessor = this.factory.buildPreProcess(preProcess, ExchangeServicePreProcess.class);
        return preProcessor.getConnectionParameters();
    }

    @Override
    public ExchangeWrapper connect(ModuleProvider<? extends ExchangeService> mp,
                                   ConnectionParameters connectionParameters) throws ExchangeConnectionException {
        ExchangeService exchangeService = this.factory.build(mp);
        ExchangeWrapper wrapper = this.factory.wrap(exchangeService, connectionParameters);
        exchangeService.listenConnection(wrapper);

        exchangeService.connect(connectionParameters);

        this.wrappersCached.putIfAbsent(wrapper.getId(), wrapper);
        return wrapper;
    }

    @Override
    public Optional<ExchangeWrapper> getExchange(String wrapperId) {
        return Optional.ofNullable(this.wrappersCached.get(wrapperId));
    }

    @Override
    public Optional<ModuleProvider<? extends ExchangeService>> getExchangeProvider(String pluginId, String exchangeName) {
        return this.getAllModuleProviders().stream()
                .filter(moduleProvider -> {
                    ModuleInfo moduleInfo = moduleProvider.getModuleInfo();
                    return moduleInfo.getName().equalsIgnoreCase(exchangeName) &&
                            moduleInfo.getPlugin().getInfo().getPluginId().equalsIgnoreCase(pluginId);
                }).findFirst();
    }
}
