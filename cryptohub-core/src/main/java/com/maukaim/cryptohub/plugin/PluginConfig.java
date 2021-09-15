package com.maukaim.cryptohub.plugin;


import com.maukaim.cryptohub.plugins.core.PluginLoader;
import com.maukaim.cryptohub.plugins.core.PluginLoaderImpl;
import com.maukaim.cryptohub.plugins.core.PluginRepository;
import com.maukaim.cryptohub.plugins.core.PluginRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PluginConfig {

    @Bean
    public PluginRepository getPluginRepository() {
        return new PluginRepositoryImpl();
    }

    @Bean
    public PluginLoader getPluginLoader(){
        return new PluginLoaderImpl();
    }

}
