package com.maukaim.opensource.cryptohub.exchange;

import com.maukaim.opensource.cryptohub.exchange.listeners.ErrorListener;
import com.maukaim.opensource.cryptohub.exchange.listeners.MarketDataListener;
import com.maukaim.opensource.cryptohub.exchange.listeners.OrderUpdateListener;
import com.maukaim.opensource.cryptohub.exchange.model.ConnectionParameter;
import com.maukaim.opensource.cryptohub.exchange.model.CryptoPair;
import com.maukaim.opensource.cryptohub.order.Order;
import com.maukaim.opensource.cryptohub.order.OrderParameter;

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

    // Connection to Exchange
    List<ConnectionParameter> getConnectionParameters();
    void connect(List<ConnectionParameter> connectionParameters);
    void disconnect();

    // Symbols management
    Set<CryptoPair> getAvailableSymbols();


    // Order Management
    List<String> getOrderTypes();
    List<OrderParameter> getOrderParameter(String orderType);

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
