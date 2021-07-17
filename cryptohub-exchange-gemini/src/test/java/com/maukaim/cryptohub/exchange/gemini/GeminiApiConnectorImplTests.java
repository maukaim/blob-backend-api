package com.maukaim.cryptohub.exchange.gemini;

import com.maukaim.cryptohub.exchange.gemini.symbol.SymbolDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GeminiApiConnectorImplTests {

    @Test
    public void shouldReturnSymbolsList(){
        GeminiApiConnectorImpl geminiApiConnector = new GeminiApiConnectorImpl();
        List<String> allSymbols = geminiApiConnector.getAllSymbols();
        System.out.println(allSymbols);
        Assert.assertNotEquals("Should get a non-empty list", 0, allSymbols.size());

    }

    @Test
    public void shouldReturnSymbolsDetails(){
        GeminiApiConnectorImpl geminiApiConnector = new GeminiApiConnectorImpl();
        List<String> allSymbols = geminiApiConnector.getAllSymbols();
        System.out.println(allSymbols);
        List<SymbolDetails> symbolsDetails = geminiApiConnector.getSymbolsDetails(allSymbols);
        System.out.println(symbolsDetails);
        Assert.assertNotEquals("Should get a non-empty list", 0, symbolsDetails.size());

    }
}
