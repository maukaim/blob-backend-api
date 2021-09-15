package com.maukaim.cryptohub.plugins.api.parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Generic Parameter characteristics
 */
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Parameter {
    private String name;
    private Class type;
    private String value;
    private String description;
}
