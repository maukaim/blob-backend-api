package com.maukaim.blob.core.plugin;


import com.maukaim.blob.plugins.core.PluginLoader;
import com.maukaim.blob.plugins.core.PluginLoaderImpl;
import com.maukaim.blob.plugins.core.PluginRepository;
import com.maukaim.blob.plugins.core.PluginRepositoryImpl;
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
