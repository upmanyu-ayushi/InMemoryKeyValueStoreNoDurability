package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Value {

    private Map<String, AttributeValue> attributes = new HashMap<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        attributes.forEach((key, value) -> sb.append(key).append(": ").append(value).append(", "));
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
