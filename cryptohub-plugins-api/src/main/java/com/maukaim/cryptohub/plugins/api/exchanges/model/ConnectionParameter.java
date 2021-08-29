package com.maukaim.cryptohub.plugins.api.exchanges.model;


import com.maukaim.cryptohub.plugins.api.parameter.Parameter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionParameter extends Parameter {
    private Boolean required;
}
