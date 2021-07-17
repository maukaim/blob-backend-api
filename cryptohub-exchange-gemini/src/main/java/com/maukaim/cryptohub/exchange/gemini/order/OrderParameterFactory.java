package com.maukaim.cryptohub.exchange.gemini.order;

import com.maukaim.cryptohub.commons.exchanges.model.CryptoPair;
import com.maukaim.cryptohub.commons.order.OrderParameter;

import java.math.BigDecimal;
import java.util.List;

public class OrderParameterFactory {

    public static List<OrderParameter> build(OrderType type, CryptoPair pair){
        switch(type){
            case LIMIT:
            case MAKER_OR_CANCEL:
            case IMMEDIATE_OR_CANCEL:
            case FILL_OR_KILL:
                return OrderParameterFactory.buildPriceQuantity(pair);
            case STOP_LIMIT:
                return OrderParameterFactory.buildStopLimit(pair);
            case MARKET:
            default:
                return List.of(getQuantityParameter(pair.getQuoteCurrency()));
        }
    }

    private static List<OrderParameter> buildStopLimit(CryptoPair pair) {
        return List.of(
                getPriceParameter(pair, "Stop Price"),
                getPriceParameter(pair, "Limit Price"),
                getQuantityParameter(pair.getQuoteCurrency())
        );
    }

    private static List<OrderParameter> buildPriceQuantity(CryptoPair pair) {
        return List.of(
                getPriceParameter(pair, "Price"),
                getQuantityParameter(pair.getQuoteCurrency())
        );
    }

    private static OrderParameter getPriceParameter(CryptoPair pair, String name){
        return OrderParameter.builder()
                .name(name)
                .value("0")
                .type(BigDecimal.class)
                .description(String.format("How much %s for %s.", pair.getQuoteCurrency(), pair.getBaseCurrency()))
                .shouldBeFilled(true)
                .build();
    }

    private static OrderParameter getQuantityParameter(String quantityCurrency){
        return OrderParameter.builder()
                .name("Quantity")
                .value("0")
                .type(BigDecimal.class)
                .description(String.format("How much %s for the proposed price.", quantityCurrency))
                .shouldBeFilled(true)
                .build();
    }
}
