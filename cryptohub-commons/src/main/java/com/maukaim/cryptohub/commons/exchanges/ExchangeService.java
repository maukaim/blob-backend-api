package com.maukaim.cryptohub.commons.exchanges;

import com.maukaim.cryptohub.commons.exchanges.exception.ExchangeConnectionException;
import com.maukaim.cryptohub.commons.exchanges.exception.ExchangeDisconnectionException;
import com.maukaim.cryptohub.commons.exchanges.listeners.ErrorListener;
import com.maukaim.cryptohub.commons.exchanges.listeners.MarketDataListener;
import com.maukaim.cryptohub.commons.exchanges.listeners.OrderUpdateListener;
import com.maukaim.cryptohub.commons.exchanges.model.ConnectionParameter;
import com.maukaim.cryptohub.commons.exchanges.model.CryptoPair;
import com.maukaim.cryptohub.commons.exchanges.exception.OrderTypeNotFoundException;
import com.maukaim.cryptohub.commons.order.Order;
import com.maukaim.cryptohub.commons.order.OrderParameter;


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
public interface ExchangeService {

    void connect(List<ConnectionParameter> connectionParameters) throws ExchangeConnectionException;
    void disconnect() throws ExchangeDisconnectionException;

    // Symbols management
    Set<CryptoPair> getAvailableSymbols();


    // Order Management
    List<String> getOrderTypes(CryptoPair cryptoPair);
    List<OrderParameter> getOrderParameter(String orderType, CryptoPair pair) throws OrderTypeNotFoundException;

    Collection<Order> getExistingOrders();
    Optional<Order> createOrder(Order order);
    Optional<Order> cancelOrder(Order order);
    Optional<Order> updateOrder(Order order);
    void subscribeOnOrderUpdate(OrderUpdateListener listener);

    // MarketData management
    void subscribeOnError(ErrorListener listener);
    void subscribeOnMarketData(MarketDataListener listener, CryptoPair symbol);

    //IDEA: Ajouter du Aka pour eviter de subscribe ici a chaque fois. On passe juste au connector le system Aka
    // dans un init() et on lui dit a quoi envoyer des donnees. Les autres beans n'ont que a ecouter.
}
