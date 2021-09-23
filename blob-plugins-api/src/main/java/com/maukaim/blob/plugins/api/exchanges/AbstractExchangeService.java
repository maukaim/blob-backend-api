package com.maukaim.blob.plugins.api.exchanges;

import com.maukaim.blob.plugins.api.exchanges.listeners.ExchangeServiceListener;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractExchangeService implements ExchangeService {
    @Getter
    @Setter
    private ExchangeServiceListener exchangeServiceListener;

}
