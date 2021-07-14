package com.maukaim.opensource.cryptohub.exchange.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPair {
    private String symbol;
    private String baseCurrency;
    private String quoteCurrency;
    private BigDecimal tickSize;
    private BigDecimal quoteIncrement;
    private BigDecimal minOrderSize;


    public Optional<BigDecimal> getTickSize(){
        return Optional.ofNullable(this.tickSize);
    }

    public Optional<BigDecimal> getQuoteIncrement(){
        return Optional.ofNullable(this.quoteIncrement);
    }

    public Optional<BigDecimal> getMinOrderSize(){
        return Optional.ofNullable(this.minOrderSize);
    }
}
