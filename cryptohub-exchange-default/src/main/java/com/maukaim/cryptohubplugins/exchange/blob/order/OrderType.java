package com.maukaim.cryptohubplugins.exchange.blob.order;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;


@AllArgsConstructor
public enum OrderType {
    LIMIT("Limit"),
    STOP_LIMIT("Stop-Limit"),
    MAKER_OR_CANCEL("Maker-or-Cancel"),
    IMMEDIATE_OR_CANCEL("Immediate-or-Cancel"),
    FILL_OR_KILL("Fill-or-Kill"),
    MARKET("Market");

    @Getter
    private String name;

    public static Optional<OrderType> of(String name){
        return Stream.of(OrderType.values())
                .filter(type -> type.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
