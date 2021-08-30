package com.maukaim.cryptohub.exchange.gemini;

import com.maukaim.cryptohub.exchange.gemini.order.OrderParameterFactory;
import com.maukaim.cryptohub.exchange.gemini.order.OrderType;
import com.maukaim.cryptohub.exchange.gemini.symbol.SymbolDetails;
import com.maukaim.cryptohub.plugins.api.exchanges.ExchangeService;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.ExchangeConnectionException;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.ExchangeDisconnectionException;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.OrderTypeNotFoundException;
import com.maukaim.cryptohub.plugins.api.exchanges.listeners.ErrorListener;
import com.maukaim.cryptohub.plugins.api.exchanges.listeners.MarketDataListener;
import com.maukaim.cryptohub.plugins.api.exchanges.listeners.OrderUpdateListener;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameter;
import com.maukaim.cryptohub.plugins.api.exchanges.model.CryptoPair;
import com.maukaim.cryptohub.plugins.api.order.Order;
import com.maukaim.cryptohub.plugins.api.order.OrderParameter;
import com.maukaim.cryptohub.plugins.api.plugin.HasPreProcess;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@HasPreProcess(GeminiPreProcess.class)
public class GeminiServiceImpl implements ExchangeService {
    private final GeminiSocketConnector geminiSocketConnector;

    public GeminiServiceImpl() {
        this.geminiSocketConnector = new GeminiSocketConnector();
    }

    @Override
    public void connect(List<ConnectionParameter> connectionParameters) throws ExchangeConnectionException {
        //TODO: Connect to SOCKET and test connection to Oauth2 API

    }

    @Override
    public void disconnect() throws ExchangeDisconnectionException {
        //TODO: Disconnect and Call onDisconnect
    }

    @Override
    public Set<CryptoPair> getAvailableSymbols() {
        List<String> allSymbols = Collections.EMPTY_LIST; //this.geminiApiConnector.getAllSymbols();
        List<SymbolDetails> symbolsDetails = Collections.EMPTY_LIST; // this.geminiApiConnector.getSymbolsDetails(allSymbols);
        return symbolsDetails.stream()
                .map(symbolDetails -> CryptoPair.builder()
                        .symbol(symbolDetails.getSymbol())
                        .baseCurrency(symbolDetails.getBaseCurrency())
                        .quoteCurrency(symbolDetails.getQuoteCurrency())
                        .tickSize(symbolDetails.getTickSize())
                        .minOrderSize(symbolDetails.getMinOrderSize())
                        .quoteIncrement(symbolDetails.getQuoteIncrement())
                        .build()
                )
                .collect(Collectors.toSet());
    }

    @Override
    public List<String> getOrderTypes(CryptoPair pair) {
        return Stream.of(OrderType.values()).map(OrderType::getName).collect(Collectors.toList());
    }

    @Override
    public List<OrderParameter> getOrderParameter(String orderTypeName, CryptoPair pair) throws OrderTypeNotFoundException {
        Optional<OrderType> optionalOrderType = OrderType.of(orderTypeName);
        if (optionalOrderType.isPresent()) {
            OrderType orderType = optionalOrderType.get();
            return OrderParameterFactory.build(orderType, pair);
        } else {
            List<String> orderTypesAvailable = Stream.of(OrderType.values()).map(OrderType::getName).collect(Collectors.toList());
            throw new OrderTypeNotFoundException(
                    String.format("%s not found. Please use one of the following -> %s",
                            orderTypeName,
                            String.join(", ", orderTypesAvailable)));
        }
    }

    @Override
    public Collection<Order> getExistingOrders() {
        return new ArrayList<>();
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
