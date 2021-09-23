package com.maukaim.blob.plugins.api.exchanges.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * List of ConnectionParameter attached to an Identifier.
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionParameters {
    private String identifier;
    private List<ConnectionParameter> parameters;

    @Override
    public int hashCode() {
        return Objects.hash(this.identifier);
    }


    @Override
    public boolean equals(Object o) {
        return o == this ||
                (o instanceof ConnectionParameters &&
                        Objects.equals(((ConnectionParameters) o).parameters, this.parameters)
                );
    }
}
