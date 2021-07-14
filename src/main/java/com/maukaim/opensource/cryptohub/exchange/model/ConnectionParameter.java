package com.maukaim.opensource.cryptohub.exchange.model;

import com.maukaim.opensource.cryptohub.model.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionParameter extends Parameter {
    private Boolean required;
}
