package com.maukaim.cryptohub.commons.exchanges.model;

import com.maukaim.cryptohub.commons.parameter.Parameter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionParameter extends Parameter {
    private Boolean required;
}
