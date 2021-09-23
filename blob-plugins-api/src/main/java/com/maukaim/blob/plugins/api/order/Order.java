package com.maukaim.blob.plugins.api.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String id;
    private LocalDateTime time;
    private String symbol;
    private OrderStatus orderStatus;
    private List<OrderParameter> parameters;
}
