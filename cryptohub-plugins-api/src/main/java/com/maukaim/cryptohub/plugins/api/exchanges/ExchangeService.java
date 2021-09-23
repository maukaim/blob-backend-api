package com.maukaim.cryptohub.plugins.api.exchanges;

import com.maukaim.cryptohub.plugins.api.exchanges.listeners.ExchangeServiceListener;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameters;
import com.maukaim.cryptohub.plugins.api.plugin.Module;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.ExchangeConnectionException;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.ExchangeDisconnectionException;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.OrderTypeNotFoundException;
import com.maukaim.cryptohub.plugins.api.exchanges.listeners.ConnectionListener;
import com.maukaim.cryptohub.plugins.api.exchanges.listeners.MarketDataListener;
import com.maukaim.cryptohub.plugins.api.exchanges.listeners.OrderUpdateListener;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameter;
import com.maukaim.cryptohub.plugins.api.exchanges.model.CryptoPair;
import com.maukaim.cryptohub.plugins.api.order.Order;
import com.maukaim.cryptohub.plugins.api.order.OrderParameter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Contract each exchange connector has to follow:
 *  Has to provide order types and arguments expected
 *      We can then reply with these arguments, and the connector interprets
 *  Has to provide parameters required for connection
 *
 */
public interface ExchangeService extends Module {
    //TODO: Replace by an Akka system ?
    void setExchangeServiceListener(ExchangeServiceListener listener);

    void connect(ConnectionParameters connectionParameters) throws ExchangeConnectionException;
    void disconnect();

    // Symbols management

    Set<CryptoPair> getAvailableSymbols();

    // Order Management
    List<String> getOrderTypes(CryptoPair cryptoPair);

    List<OrderParameter> getOrderParameter(String orderType, CryptoPair pair) throws OrderTypeNotFoundException;
    Collection<Order> getExistingOrders();
    Optional<Order> createOrder(Order order);
    Optional<Order> cancelOrder(Order order);
    Optional<Order> updateOrder(Order order);



}
