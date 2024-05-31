package org.example;

import org.example.models.Pair;
import org.example.services.KeyValueStore;
import org.example.models.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        KeyValueStore store = new KeyValueStore();

        while(true) {

            String input = sc.nextLine();

            String[] parts = input.split(" ");
            String command = parts[0];

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            switch (command) {
                case "get":
                    handleGetCommand(store, parts);
                    break;
                case "put":
                    handlePutCommand(store, parts);
                    break;
                case "delete":
                    handleDeleteCommand(store, parts);
                    break;
                case "search":
                    handleSearchCommand(store, parts);
                    break;
                case "keys":
                    handleKeysCommand(store, parts);
                    break;
                default:
                    System.out.println("Unknown command");
            }


        }

        sc.close();
    }

    private static void handleKeysCommand(KeyValueStore store, String[] parts) {
        if (parts.length != 1) {
            System.out.println("Invalid keys command");
            return;
        }
        List<String> keys = store.keys();
        System.out.println(String.join(", ", keys));
    }

    private static void handleSearchCommand(KeyValueStore store, String[] parts) {
        if (parts.length != 3) {
            System.out.println("Invalid search command");
            return;
        }

        String attributeKey = parts[1];
        String attributeValue = parts[2];
        List<String> keys = store.search(attributeKey, attributeValue);
        System.out.println(String.join(", ", keys));
    }

    private static void handleDeleteCommand(KeyValueStore store, String[] parts) {
        if (parts.length != 2) {
            System.out.println("Invalid delete command");
            return;
        }
        String key = parts[1];
        store.delete(key);
    }

    private static void handlePutCommand(KeyValueStore store, String[] parts) {
        if (parts.length < 4 || parts.length % 2 != 0) {
            System.out.println("Invalid put command");
            return;
        }

        String key = parts[1];
        List<Pair<String, String>> listOfAttributePairs = new ArrayList<>();
        for(int i=2;i<parts.length;i+=2) {
            listOfAttributePairs.add(new Pair<>(parts[i], parts[i+1]));
        }
        try {
            store.put(key, listOfAttributePairs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void handleGetCommand(KeyValueStore store, String[] parts) {
        if (parts.length != 2) {
            System.out.println("Invalid get command");
            return;
        }
        String key = parts[1];
        Value value = store.get(key);

        if (value != null) {
            System.out.println(value);
        } else {
            System.out.println("No entry found for " + key);
        }
    }
}