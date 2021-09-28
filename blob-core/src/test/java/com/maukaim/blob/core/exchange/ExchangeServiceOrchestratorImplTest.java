package com.maukaim.blob.core.exchange;

import com.maukaim.blob.plugins.api.exchanges.ExchangeService;
import com.maukaim.blob.plugins.api.exchanges.ExchangeServicePreProcess;
import com.maukaim.blob.plugins.core.PluginService;
import com.maukaim.blob.plugins.core.model.Plugin;
import com.maukaim.blob.plugins.core.model.PluginInfo;
import com.maukaim.blob.plugins.core.model.module.ModuleInfo;
import com.maukaim.blob.plugins.core.model.module.ModuleProvider;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceOrchestratorImplTest {
    @Mock
    private ExchangeStreamer streamer;
    @Mock
    private ExchangeServiceFactory factory;
    @Mock
    private PluginService pluginService;

    @InjectMocks
    private ExchangeServiceOrchestratorImpl orchestrator;

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
        Assertions.assertEquals(0, this.orchestrator.getAvailableExchangesInfo().size(),
                "getAvailableExchangesInfo() Should return empty list");
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
            Assertions.assertEquals(i, this.orchestrator.getAvailableExchangesInfo().size(),
                    "When " + i + " Exchange Services are available, we should get " + i + " ModuleInfo");
            Mockito.verify(pluginService, Mockito.times(i)).getProviders(eq(ExchangeService.class));
        }

    }

    @Test
    public void should_getExchangeProvider_return_optional_with_value_when_requested_exchangeService_is_available() {
        Mockito.when(pluginService.getProviders(eq(ExchangeService.class)))
                .thenReturn(List.of(moduleProvider("exchangeName", "pluginId")));

        Assertions.assertTrue(this.orchestrator.getExchangeProvider("pluginId", "exchangeName").isPresent(),
                "getExchangeProvider should return empty optional when provider requested is not in the list");

        Mockito.verify(pluginService, Mockito.times(1)).getProviders(eq(ExchangeService.class));

    }

    @Test
    public void should_getExchangeProvider_return_optionalEmpty_when_no_exchangeService_available() {

        Mockito.when(pluginService.getProviders(eq(ExchangeService.class))).thenReturn(Lists.emptyList());
        Assertions.assertTrue(this.orchestrator.getExchangeProvider("ignored", "ignored").isEmpty(),
                "getExchangeProvider should return empty optional when no exchange service available");

        Mockito.verify(pluginService, Mockito.times(1)).getProviders(eq(ExchangeService.class));

    }

    @Test
    public void should_getExchangeProvider_return_optionalEmpty_when_exchangeServices_available_are_not_the_one_requested() {
        Mockito.when(pluginService.getProviders(eq(ExchangeService.class)))
                .thenReturn(List.of(moduleProvider("anotherExchangeName", "anotherPluginId")));

        Assertions.assertTrue(this.orchestrator.getExchangeProvider("pluginId", "exchangeName").isEmpty(),
                "getExchangeProvider should return empty optional when provider requested is not in the list");

        Mockito.verify(pluginService, Mockito.times(1)).getProviders(eq(ExchangeService.class));

    }

    @Test
    public void should_getExchangeProvider_return_optionalEmpty_when_exchangeName_exist_but_not_in_the_pluginRequested() {
        Mockito.when(pluginService.getProviders(eq(ExchangeService.class)))
                .thenReturn(List.of(moduleProvider("exchangeName", "anotherPluginId")));

        Assertions.assertTrue(this.orchestrator.getExchangeProvider("pluginId", "exchangeName").isEmpty(),
                "getExchangeProvider should return empty optional when provider requested is not in the list");

        Mockito.verify(pluginService, Mockito.times(1)).getProviders(eq(ExchangeService.class));

    }

    @Test
    public void should_getExchangeProvider_return_optionalEmpty_when_pluginRequested_provide_an_exchangeService_but_not_the_requested_one() {
        Mockito.when(pluginService.getProviders(eq(ExchangeService.class)))
                .thenReturn(List.of(moduleProvider("anotherExchangeName", "pluginId")));

        Assertions.assertTrue(this.orchestrator.getExchangeProvider("pluginId", "exchangeName").isEmpty(),
                "getExchangeProvider should return empty optional when provider requested is not in the list");

        Mockito.verify(pluginService, Mockito.times(1)).getProviders(eq(ExchangeService.class));

    }


    @Test
    public void should_getConnectionParameters_return_emptyMap_when_moduleProvider_doesnot_provide_preProcess() {
        Assertions.assertTrue(CollectionUtils.isEmpty(
                this.orchestrator.getConnectionParameters(moduleProvider("ignored", "ignored"))),
                "ModuleProvider does not provide a preProcess, connectionParametersMap should be empty.");
    }

    @Test
    public void should_getConnectionParameters_throw_NullPointer_when_method_param_exchangeProvider_is_null() {
        Assertions.assertThrows(NullPointerException.class, () -> this.orchestrator.getConnectionParameters(null),
                "getConnectionParameters should throw NullPointerException when its parameter is null");
    }

    @Test
    public void should_getConnectionParameters_throw_NullPointer_when_factory_result_is_null() {
        ModuleProvider<? extends ExchangeService> moduleProvider = moduleProvider("ignored", "ignored");
        moduleProvider.setPreProcess(ExchangeServicePreProcess.class);

        Mockito.when(this.factory.buildPreProcess(Mockito.any(), Mockito.any()))
                .thenReturn(null);

        Assertions.assertThrows(NullPointerException.class, () -> this.orchestrator.getConnectionParameters(moduleProvider),
                "getConnectionParameters should throw NullPointerException when the factory used returns null");
        Mockito.verify(this.factory, Mockito.times(1))
                .buildPreProcess(Mockito.any(), Mockito.any());
    }

    @Test
    public void should_getConnectionParameters_throw_NullPointer_when_the_preprocess_s_getConnectionParameters_method_return_null() {
        ExchangeServicePreProcess mockedPreProcess = Mockito.mock(ExchangeServicePreProcess.class);
        Mockito.when(mockedPreProcess.getConnectionParameters()).thenReturn(null);
        Mockito.when(this.factory.buildPreProcess(Mockito.any(), Mockito.any()))
                .thenReturn(mockedPreProcess);

        ModuleProvider<? extends ExchangeService> moduleProvider = moduleProvider("ignored", "ignored");
        moduleProvider.setPreProcess(ExchangeServicePreProcess.class);

        Assertions.assertThrows(NullPointerException.class, () -> this.orchestrator.getConnectionParameters(moduleProvider),
                "getConnectionParameters should throw NullPointerException when the ExchangeServicePreProcess's getConnectionParameters method return null");
        Mockito.verify(mockedPreProcess, Mockito.times(1))
                .getConnectionParameters();
        Mockito.verify(this.factory, Mockito.times(1))
                .buildPreProcess(Mockito.any(), Mockito.any());
    }

}
