package org.example.services;

import org.example.models.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class KeyValueStore {

    private final Map<String, Value> store = new ConcurrentHashMap<>();
    private final Map<String, Class<?>> attributeTypes = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Value get(String key) {
        lock.readLock().lock();
        try {
            return store.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void put(String key, List<Pair<String, String>> listOfAttributePairs) throws Exception {
        lock.writeLock().lock();
        try {
            Value value = new Value(new HashMap<>());
            for (Pair<String, String> pair : listOfAttributePairs) {
                String attributeKey = pair.getKey();
                String attributeValue = pair.getValue();

                Class<?> existingType = attributeTypes.get(attributeKey);
                AttributeValue newAttributeValue = parseValue(attributeValue, existingType);
                if (newAttributeValue == null) {
                    throw new IllegalArgumentException("Data Type Error");
                }

                if (existingType == null) {
                    attributeTypes.put(attributeKey, newAttributeValue.getClass());
                } else if (!existingType.equals(newAttributeValue.getClass())) {
                    throw new IllegalArgumentException("Data Type Error");
                }

                value.getAttributes().put(attributeKey, newAttributeValue);
            }
            store.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void delete(String key) {
        lock.writeLock().lock();
        try {
            store.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<String> search(String attributeKey, String attributeValue) {
        lock.readLock().lock();
        List<String> keys = new ArrayList<>();
        try {
            store.forEach((key, value) -> {
                AttributeValue attrVal = value.getAttributes().get(attributeKey);
                if (attrVal != null && attrVal.toString().equals(attributeValue)) {
                    keys.add(key);
                }
            });

        } finally {
            lock.readLock().unlock();
        }
        Collections.sort(keys);
        return keys;
    }

    public List<String> keys() {
        lock.readLock().lock();
        try {
            List<String> result = new ArrayList<>(store.keySet());
            Collections.sort(result);
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    private AttributeValue parseValue(String value, Class<?> existingType) {
        if (existingType == StringValue.class || (existingType == null && !value.matches("-?\\d+(\\.\\d+)?"))) {
            return new StringValue(value);
        } else if (existingType == IntegerValue.class || (existingType == null && value.matches("-?\\d+"))) {
            return new IntegerValue(Integer.parseInt(value));
        } else if (existingType == DoubleValue.class || (existingType == null && value.matches("-?\\d+\\.\\d+"))) {
            return new DoubleValue(new BigDecimal(value));
        } else if (existingType == BooleanValue.class || (existingType == null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")))) {
            return new BooleanValue(Boolean.parseBoolean(value));
        } else {
            return null;
        }
    }
}
