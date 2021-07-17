package com.maukaim.cryptohub.commons.parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
