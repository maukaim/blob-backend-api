package com.maukaim.cryptohub.exchange.sniffer;

import com.maukaim.cryptohub.commons.module.ExchangeDeclarator;
import com.maukaim.cryptohub.plugins.PluginModuleManager;

import java.util.Map;

public interface ExchangeModuleManager extends PluginModuleManager {
    Map<String, ExchangeDeclarator> getExchangeLoaders();
    ExchangeDeclarator getExchangeLoader(String exchangePluginName);
}
