package edu.utsa.cs3443.mealmatch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public static Date parseDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
