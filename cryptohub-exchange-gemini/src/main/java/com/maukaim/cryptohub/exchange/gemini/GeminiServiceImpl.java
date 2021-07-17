package com.maukaim.cryptohub.exchange.gemini;

import com.maukaim.cryptohub.commons.exchanges.ExchangeService;
import com.maukaim.cryptohub.commons.exchanges.exception.ExchangeConnectionException;
import com.maukaim.cryptohub.commons.exchanges.exception.ExchangeDisconnectionException;
import com.maukaim.cryptohub.commons.exchanges.listeners.ErrorListener;
import com.maukaim.cryptohub.commons.exchanges.listeners.MarketDataListener;
import com.maukaim.cryptohub.commons.exchanges.listeners.OrderUpdateListener;
import com.maukaim.cryptohub.commons.exchanges.model.ConnectionParameter;
import com.maukaim.cryptohub.commons.exchanges.model.CryptoPair;
import com.maukaim.cryptohub.exchange.gemini.order.OrderParameterFactory;
import com.maukaim.cryptohub.exchange.gemini.order.OrderType;
import com.maukaim.cryptohub.commons.exchanges.exception.OrderTypeNotFoundException;
import com.maukaim.cryptohub.commons.order.Order;
import com.maukaim.cryptohub.commons.order.OrderParameter;
import com.maukaim.cryptohub.exchange.gemini.symbol.SymbolDetails;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GeminiServiceImpl implements ExchangeService {

    private final GeminiApiConnector geminiApiConnector;
    private final GeminiSocketConnector geminiSocketConnector;

    public GeminiServiceImpl() {
        this.geminiApiConnector = new GeminiApiConnectorImpl();
        this.geminiSocketConnector = new GeminiSocketConnector();
    }

    @Override
    public String getExchangeName() {
        return "Gemini Exchange"
    }

    @Override
    public List<ConnectionParameter> getConnectionParameters() {
        return List.of(
                ConnectionParameter.builder()
                        .name("Username")
                        .type(String.class)
                        .value("your-username")
                        .required(true)
                        .description("Username or email used to login on Gemini.")
                        .build(),
                ConnectionParameter.builder()
                        .name("Password")
                        .type(String.class)
                        .value("")
                        .required(true)
                        .description("Password used to login on Gemini.")
                        .build()
        );
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
        List<String> allSymbols = this.geminiApiConnector.getAllSymbols();
        List<SymbolDetails> symbolsDetails = this.geminiApiConnector.getSymbolsDetails(allSymbols);
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
