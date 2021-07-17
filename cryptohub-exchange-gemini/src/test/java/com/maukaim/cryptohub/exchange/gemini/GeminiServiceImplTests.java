package com.maukaim.cryptohub.exchange.gemini;

import com.maukaim.cryptohub.commons.exchanges.model.CryptoPair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class GeminiServiceImplTests {

    private GeminiServiceImpl geminiService;

    @Before
    public void init(){
        this.geminiService = new GeminiServiceImpl();
    }

    @Test
    public void should_get_all_cryptoPair_from_gemini(){
        Set<CryptoPair> availableSymbols = this.geminiService.getAvailableSymbols();
        System.out.println(availableSymbols);
        Assert.assertNotEquals("Should get a non-empty list", 0, availableSymbols.size());

    }

}
