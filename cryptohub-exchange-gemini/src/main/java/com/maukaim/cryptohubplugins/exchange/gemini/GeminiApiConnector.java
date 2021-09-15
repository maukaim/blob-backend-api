package com.maukaim.cryptohubplugins.exchange.gemini;

import com.maukaim.cryptohubplugins.exchange.gemini.symbol.SymbolDetails;

import java.util.List;

public interface GeminiApiConnector {
    List<String> getAllSymbols();
    List<SymbolDetails> getSymbolsDetails(List<String> symbols);
}
