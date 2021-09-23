package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.plugins.api.exchanges.ExchangeService;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameters;
import com.maukaim.cryptohub.plugins.core.model.module.AbstractModuleFactory;
import org.springframework.stereotype.Service;

@Service
public class ExchangeServiceFactory extends AbstractModuleFactory<ExchangeService> {

    ExchangeWrapper wrap(ExchangeService service, ConnectionParameters parameters, ExchangeStreamer sender) {
        return new ExchangeWrapper(service, parameters, sender);
    }
}
