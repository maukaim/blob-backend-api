package com.maukaim.cryptohub.plugins.api.exchanges;

import com.maukaim.cryptohub.plugins.api.exchanges.exception.ExchangeConnectionException;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.OrderTypeNotFoundException;
import com.maukaim.cryptohub.plugins.api.exchanges.listeners.ExchangeServiceListener;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameters;
import com.maukaim.cryptohub.plugins.api.exchanges.model.CryptoPair;
import com.maukaim.cryptohub.plugins.api.order.Order;
import com.maukaim.cryptohub.plugins.api.order.OrderParameter;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractExchangeService implements ExchangeService {
    @Getter
    @Setter
    private ExchangeServiceListener exchangeServiceListener;

}
