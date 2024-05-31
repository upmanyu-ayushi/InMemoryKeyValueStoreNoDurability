package org.example.models;

import lombok.Value;

@Value
public class BooleanValue implements AttributeValue {

    Boolean value;

    @Override
    public String toString() {
        return value.toString();
    }
}
