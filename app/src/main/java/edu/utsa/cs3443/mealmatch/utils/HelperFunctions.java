package edu.utsa.cs3443.mealmatch.utils;

import java.util.ArrayList;

public class HelperFunctions {
    public static ArrayList<Integer> parseIntegerList(String data, String delimiter) {
        ArrayList<Integer> list = new ArrayList<>();
        if (!data.isEmpty()) {
            String[] items = data.split(delimiter);
            for (String item : items) {
                list.add(Integer.parseInt(item.trim()));
            }
        }
        return list;
    }

    public static ArrayList<String> parseStringList(String data, String delimiter) {
        ArrayList<String> list = new ArrayList<>();
        if (!data.isEmpty()) {
            String[] items = data.split(delimiter);
            for (String item : items) {
                list.add(item.trim());
            }
        }
        return list;
    }
}
