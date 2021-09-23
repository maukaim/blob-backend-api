package com.maukaim.blob.plugins.api.exchanges.model;


import com.maukaim.blob.plugins.api.parameter.Parameter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Parameter to connect with an Exchange
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionParameter extends Parameter {
    private Boolean required;
}
