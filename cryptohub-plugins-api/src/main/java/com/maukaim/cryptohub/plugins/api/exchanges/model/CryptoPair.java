package com.maukaim.cryptohub.plugins.api.exchanges.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

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


    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public boolean equals(Object o) {
        return o == this ||
                (o instanceof CryptoPair &&
                        Objects.equals(((CryptoPair) o).getSymbol(), this.getSymbol())
                );
    }
}
