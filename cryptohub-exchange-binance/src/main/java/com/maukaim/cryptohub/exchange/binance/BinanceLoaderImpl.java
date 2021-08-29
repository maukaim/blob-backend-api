package com.maukaim.cryptohub.exchange.binance;

import com.maukaim.cryptohub.commons.exchanges.ExchangeService;
import com.maukaim.cryptohub.commons.exchanges.model.ConnectionParameter;
import com.maukaim.cryptohub.commons.module.ExchangeDeclarator;

import java.util.List;
import java.util.Map;

public class BinanceLoaderImpl implements ExchangeDeclarator {

    @Override
    public Map<String, List<ConnectionParameter>> getConnectionParameters() {
        return Map.ofEntries(
                Map.entry("Basic",
                        List.of(
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
                        )
                ));
    }

    @Override
    public String getName() {
        return "Binance Exchange";
    }

    @Override
    public String getDescription() {
        return "Je decris Binance loader";
    }

    @Override
    public void activate() {

    }

    @Override
    public void disactivate() {

    }

    @Override
    public ExchangeService getInstance() {
        return new BinanceServiceImpl();
    }

    @Override
    public void activate(List caca) {

    }
}
