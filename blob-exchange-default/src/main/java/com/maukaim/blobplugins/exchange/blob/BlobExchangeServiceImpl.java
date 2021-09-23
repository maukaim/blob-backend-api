package com.maukaim.blobplugins.exchange.blob;

import com.maukaim.blobplugins.exchange.blob.symbol.SymbolDetails;
import com.maukaim.blob.plugins.api.exchanges.AbstractExchangeService;
import com.maukaim.blob.plugins.api.exchanges.exception.ExchangeConnectionException;
import com.maukaim.blob.plugins.api.exchanges.exception.OrderTypeNotFoundException;
import com.maukaim.blob.plugins.api.exchanges.model.ConnectionParameters;
import com.maukaim.blob.plugins.api.exchanges.model.CryptoPair;
import com.maukaim.blob.plugins.api.market.model.MarketData;
import com.maukaim.blob.plugins.api.order.Order;
import com.maukaim.blob.plugins.api.order.OrderParameter;
import com.maukaim.blob.plugins.api.plugin.HasPreProcess;
import com.maukaim.blob.plugins.api.plugin.ModuleDeclarator;
import com.maukaim.blobplugins.exchange.blob.order.OrderParameterFactory;
import com.maukaim.blobplugins.exchange.blob.order.OrderType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@HasPreProcess(BlobExchangePreProcess.class)
@ModuleDeclarator(name = "Blob Exchange",
        description = "Provides socket and API connection capability to Blob Exchange.")
public class BlobExchangeServiceImpl extends AbstractExchangeService {

    public BlobExchangeServiceImpl() {
    }

    @Override
    public void connect(ConnectionParameters connectionParameters) throws ExchangeConnectionException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            this.getExchangeServiceListener().onMarketData(
                    MarketData.builder()
                            .ask(fakePrice(120.4D, 130.6D))
                            .bid(BigDecimal.valueOf(1))
                            .symbol("hehe")
                            .time(LocalDateTime.now())
                            .build());
        }, 1000, 250, TimeUnit.MILLISECONDS);
    }

    private BigDecimal fakePrice(Double min, Double max) {
        BigDecimal maxB = BigDecimal.valueOf(max);
        BigDecimal minB = BigDecimal.valueOf(min);
        BigDecimal randomBigDecimal = minB.add(BigDecimal.valueOf(Math.random())
                .multiply(maxB.subtract(minB)));
        return randomBigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void disconnect() {
        //TODO: Disconnect and Call onDisconnect
    }

    @Override
    public Set<CryptoPair> getAvailableSymbols() {
        List<String> allSymbols = Collections.EMPTY_LIST;
        List<SymbolDetails> symbolsDetails = Collections.EMPTY_LIST;
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

}
