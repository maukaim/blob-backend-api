package com.maukaim.cryptohub.exchange.gemini;

import com.maukaim.cryptohub.exchange.gemini.symbol.SymbolDetails;

import java.util.List;

public interface GeminiApiConnector {
    List<String> getAllSymbols();
    List<SymbolDetails> getSymbolsDetails(List<String> symbols);
}
