package com.maukaim.cryptohub.plugins.api.market.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data provided by ExchangeService after having translated the format of the
 * exchange
 */
public class MarketData {
    private BigDecimal bid;
    private BigDecimal ask;
    private LocalDateTime time;
    private String symbol;

}
