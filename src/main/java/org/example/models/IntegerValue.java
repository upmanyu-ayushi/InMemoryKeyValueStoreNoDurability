package org.example.models;

import lombok.Value;

@Value
public class IntegerValue implements AttributeValue {

    Integer value;

    @Override
    public String toString() {
        return value.toString();
    }
}
