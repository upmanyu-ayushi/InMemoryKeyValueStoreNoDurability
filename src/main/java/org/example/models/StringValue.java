package org.example.models;

import lombok.Value;

@Value
public class StringValue implements AttributeValue {

    String value;

    @Override
    public String toString() {
        return value.toString();
    }
}
