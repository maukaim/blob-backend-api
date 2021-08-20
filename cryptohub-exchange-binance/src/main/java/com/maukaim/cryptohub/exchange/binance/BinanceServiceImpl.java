package com.maukaim.cryptohub.exchange.binance;

import com.maukaim.cryptohub.commons.exchanges.ExchangeService;
import com.maukaim.cryptohub.commons.exchanges.listeners.ErrorListener;
import com.maukaim.cryptohub.commons.exchanges.listeners.MarketDataListener;
import com.maukaim.cryptohub.commons.exchanges.listeners.OrderUpdateListener;
import com.maukaim.cryptohub.commons.exchanges.model.ConnectionParameter;
import com.maukaim.cryptohub.commons.exchanges.model.CryptoPair;
import com.maukaim.cryptohub.exchange.binance.order.OrderType;
import com.maukaim.cryptohub.commons.exchanges.exception.OrderTypeNotFoundException;
import com.maukaim.cryptohub.commons.order.Order;
import com.maukaim.cryptohub.commons.order.OrderParameter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BinanceServiceImpl implements ExchangeService {

    private final BinanceApiConnector geminiApiConnector;
    private final BinanceSocketConnector geminiSocketConnector;

    public BinanceServiceImpl(){
        this.geminiApiConnector = new BinanceApiConnector();
        this.geminiSocketConnector = new BinanceSocketConnector();
    }

    @Override
    public String getExchangeName() {
        return "Binance Exchange";
    }

    @Override
    public List<ConnectionParameter> getConnectionParameters() {
        //TODO: Provide ConnectionParameters for Oauth2 and Account access.
        return null;
    }

    @Override
    public void connect(List<ConnectionParameter> connectionParameters) {
        //TODO: Connect to SOCKET and test connection to Oauth2 API
    }

    @Override
    public void disconnect() {
        //TODO: Disconnect and Call onDisconnect
    }

    @Override
    public Set<CryptoPair> getAvailableSymbols() {
        //TODO: 1) ask to Gemini 2) translate as CryptoPair list
        return null;
    }

    @Override
    public List<String> getOrderTypes(CryptoPair cryptoPair) {
        return null;
    }

    @Override
    public List<OrderParameter> getOrderParameter(String orderTypeName, CryptoPair pair) throws OrderTypeNotFoundException {
        Optional<OrderType> optionalOrderType = OrderType.of(orderTypeName);
        if(optionalOrderType.isPresent()){
            OrderType orderType = optionalOrderType.get();
            return Collections.emptyList();
        }else{
            List<String> orderTypesAvailable = Stream.of(OrderType.values()).map(OrderType::getName).collect(Collectors.toList());
            throw new OrderTypeNotFoundException(
                    String.format("%s not found. Please use one of the following -> %s",
                            orderTypeName,
                            String.join(", ", orderTypesAvailable)));
        }
    }



    @Override
    public Collection<Order> getExistingOrders() {
        return null;

    }

    @Override
    public Optional<Order> createOrder(Order order) {
        return Optional.empty();
    }

    @Override
    public Optional<Order> cancelOrder(Order order) {
        return Optional.empty();
    }

    @Override
    public Optional<Order> updateOrder(Order order) {
        return Optional.empty();
    }

    @Override
    public void subscribeOnOrderUpdate(OrderUpdateListener listener) {

    }

    @Override
    public void subscribeOnError(ErrorListener listener) {

    }

    @Override
    public void subscribeOnMarketData(MarketDataListener listener, CryptoPair symbol) {

    }
}
