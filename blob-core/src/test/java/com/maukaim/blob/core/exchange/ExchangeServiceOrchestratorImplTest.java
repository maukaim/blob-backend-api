package com.maukaim.blob.core.exchange;

import com.maukaim.blob.plugins.api.exchanges.ExchangeService;
import com.maukaim.blob.plugins.core.PluginService;
import com.maukaim.blob.plugins.core.model.Plugin;
import com.maukaim.blob.plugins.core.model.PluginInfo;
import com.maukaim.blob.plugins.core.model.module.ModuleInfo;
import com.maukaim.blob.plugins.core.model.module.ModuleProvider;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeServiceOrchestratorImplTest {
    @Mock
    private ExchangeStreamer streamer;
    @Mock
    private ExchangeServiceFactory factory;
    @Mock
    private PluginService pluginService;

    @InjectMocks
    private ExchangeServiceOrchestratorImpl exchangeServiceOrchestrator;

    private ModuleProvider<? extends ExchangeService> moduleProvider(String exchangeName, String pluginId) {
        return ModuleProvider.<ExchangeService>builder()
                .moduleInfo(ModuleInfo.builder()
                        .plugin(Plugin.builder()
                                .info(PluginInfo.builder()
                                        .pluginId(pluginId)
                                        .build())
                                .build())
                        .name(exchangeName)
                        .build())
                .build();
    }

    @Test
    public void should_getAvailableExchangesInfo_return_empty_list_when_no_exchangeService_available() {
        Mockito.when(pluginService.getProviders(eq(ExchangeService.class))).thenReturn(Lists.emptyList());
        Assert.assertEquals("getAvailableExchangesInfo() Should return empty list", 0,
                this.exchangeServiceOrchestrator.getAvailableExchangesInfo().size());
        Mockito.verify(pluginService, Mockito.times(1)).getProviders(eq(ExchangeService.class));
    }

    @Test
    public void should_getAvailableExchangesInfo_return_list_of_size_n_when_n_exchangeService_available() {
        int max_tested_size = 5;

        for (int i = 1; i < max_tested_size; i++) {
            List<ModuleProvider<? extends ExchangeService>> providers = new ArrayList<>();
            IntStream.range(0, i).forEach(integer -> {
                ModuleProvider<? extends ExchangeService> moduleProvider = ModuleProvider.<ExchangeService>builder().moduleInfo(ModuleInfo.builder().build()).build();
                providers.add(moduleProvider);
            });
            Mockito.when(pluginService.getProviders(eq(ExchangeService.class))).thenReturn(providers);
            Assert.assertEquals("When " + i + " Exchange Services are available, we should get " + i + " ModuleInfo", i,
                    this.exchangeServiceOrchestrator.getAvailableExchangesInfo().size());
            Mockito.verify(pluginService, Mockito.times(i)).getProviders(eq(ExchangeService.class));
        }

    }

    @Test
    public void should_getExchangeProvider_return_optional_with_value_when_requested_exchangeService_is_available() {
        Mockito.when(pluginService.getProviders(eq(ExchangeService.class)))
                .thenReturn(List.of(moduleProvider("exchangeName", "pluginId")));

        Assert.assertTrue("getExchangeProvider should return empty optional when provider requested is not in the list",
                this.exchangeServiceOrchestrator.getExchangeProvider("pluginId", "exchangeName").isPresent());

        Mockito.verify(pluginService, Mockito.times(1)).getProviders(eq(ExchangeService.class));

    }

    @Test
    public void should_getExchangeProvider_return_optionalEmpty_when_no_exchangeService_available() {

        Mockito.when(pluginService.getProviders(eq(ExchangeService.class))).thenReturn(Lists.emptyList());
        Assert.assertTrue("getExchangeProvider should return empty optional when no exchange service available",
                this.exchangeServiceOrchestrator.getExchangeProvider("ignored", "ignored").isEmpty());

        Mockito.verify(pluginService, Mockito.times(1)).getProviders(eq(ExchangeService.class));

    }

    @Test
    public void should_getExchangeProvider_return_optionalEmpty_when_exchangeServices_available_are_not_the_one_requested() {
        Mockito.when(pluginService.getProviders(eq(ExchangeService.class)))
                .thenReturn(List.of(moduleProvider("anotherExchangeName", "anotherPluginId")));

        Assert.assertTrue("getExchangeProvider should return empty optional when provider requested is not in the list",
                this.exchangeServiceOrchestrator.getExchangeProvider("pluginId", "exchangeName").isEmpty());

        Mockito.verify(pluginService, Mockito.times(1)).getProviders(eq(ExchangeService.class));

    }

    @Test
    public void should_getExchangeProvider_return_optionalEmpty_when_exchangeName_exist_but_not_in_the_pluginRequested() {
        Mockito.when(pluginService.getProviders(eq(ExchangeService.class)))
                .thenReturn(List.of(moduleProvider("exchangeName", "anotherPluginId")));

        Assert.assertTrue("getExchangeProvider should return empty optional when provider requested is not in the list",
                this.exchangeServiceOrchestrator.getExchangeProvider("pluginId", "exchangeName").isEmpty());

        Mockito.verify(pluginService, Mockito.times(1)).getProviders(eq(ExchangeService.class));

    }

    @Test
    public void should_getExchangeProvider_return_optionalEmpty_when_pluginRequested_provide_an_exchangeService_but_not_the_requested_one() {
        Mockito.when(pluginService.getProviders(eq(ExchangeService.class)))
                .thenReturn(List.of(moduleProvider("anotherExchangeName", "pluginId")));

        Assert.assertTrue("getExchangeProvider should return empty optional when provider requested is not in the list",
                this.exchangeServiceOrchestrator.getExchangeProvider("pluginId", "exchangeName").isEmpty());

        Mockito.verify(pluginService, Mockito.times(1)).getProviders(eq(ExchangeService.class));

    }


}
