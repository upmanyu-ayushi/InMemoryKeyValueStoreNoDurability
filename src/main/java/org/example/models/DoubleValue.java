package org.example.models;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class DoubleValue implements AttributeValue {

    BigDecimal value;

    @Override
    public String toString() {
        return value.toString();
    }
}
