package com.maukaim.blob.core.exchange;

import com.maukaim.blob.plugins.core.model.module.AbstractModuleFactory;
import com.maukaim.blob.plugins.api.exchanges.ExchangeService;
import com.maukaim.blob.plugins.api.exchanges.model.ConnectionParameters;
import org.springframework.stereotype.Service;

@Service
public class ExchangeServiceFactory extends AbstractModuleFactory<ExchangeService> {

    ExchangeWrapper wrap(ExchangeService service, ConnectionParameters parameters, ExchangeStreamer sender) {
        return new ExchangeWrapper(service, parameters, sender);
    }
}
