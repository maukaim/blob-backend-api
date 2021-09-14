package com.maukaim.cryptohubplugins.exchange.gemini.symbol;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Data
public class SymbolDetails implements Serializable {
    private String symbol;
    @JsonProperty("base_currency")
    private String baseCurrency;
    @JsonProperty("quote_currency")
    private String quoteCurrency;
    @JsonProperty("tick_size")
    private BigDecimal tickSize;
    @JsonProperty("quote_increment")
    private BigDecimal quoteIncrement;
    @JsonProperty("min_order_size")
    private BigDecimal minOrderSize;
    private String status;

    public boolean isOpen(){
        return this.status.equalsIgnoreCase("open");
    }


}
