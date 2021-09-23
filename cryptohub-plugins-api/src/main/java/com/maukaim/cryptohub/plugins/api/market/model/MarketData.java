package com.maukaim.cryptohub.plugins.api.market.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data provided by ExchangeService after having translated the format of the
 * exchange
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketData {
    private BigDecimal bid;
    private BigDecimal ask;
    private LocalDateTime time;
    private String symbol;

}
