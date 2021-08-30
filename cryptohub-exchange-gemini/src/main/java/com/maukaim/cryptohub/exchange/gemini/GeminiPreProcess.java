package com.maukaim.cryptohub.exchange.gemini;

import com.maukaim.cryptohub.plugins.api.exchanges.ExchangeServicePreProcess;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameter;

import java.util.List;
import java.util.Map;

public class GeminiPreProcess implements ExchangeServicePreProcess {

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
}
