package com.maukaim.cryptohub.exchange.gemini;

import com.maukaim.cryptohub.exchange.commons.rest.AbstractApiConnector;
import com.maukaim.cryptohub.exchange.gemini.symbol.SymbolDetails;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class GeminiApiConnectorImpl extends AbstractApiConnector implements GeminiApiConnector {

    private final RestTemplate restTemplate;

    public GeminiApiConnectorImpl(){
        this.restTemplate = this.getSimpleRestTemplate();
    }


    @Override
    public List<String> getAllSymbols() {
        return List.of(
                Objects.requireNonNull(
                        this.restTemplate.getForObject("https://api.gemini.com/v1/symbols", String[].class)
                ));
    }

    @Override
    public List<SymbolDetails> getSymbolsDetails(List<String> symbols) {
        return symbols.stream()
                .map(symbol ->{
                    ResponseEntity<SymbolDetails> response = this.restTemplate.getForEntity("https://api.gemini.com/v1/symbols/details/{symbol}", SymbolDetails.class, symbol);
                    if(response.getStatusCode().isError()){
                        return null;
                    }else{
                        return response.getBody();
                    }
                })
                .filter(symbolDetails -> Objects.nonNull(symbolDetails) && symbolDetails.isOpen())
                .collect(Collectors.toList());
    }
}
