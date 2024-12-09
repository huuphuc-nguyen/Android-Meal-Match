package edu.utsa.cs3443.mealmatch.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * HelperFunctions class contains utility functions that are used throughout the application.
 * These functions are used for parsing data, copying files, and other miscellaneous tasks.
 *
 * @author Felix Nguyen
 */
public class HelperFunctions {
    /**
     * Parses a string of integers separated by a delimiter into an ArrayList of integers.
     *
     * @param data the string of integers to parse
     * @param delimiter the delimiter separating the integers
     * @return an ArrayList of integers
     */
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

    /**
     * Parses a string of strings separated by a delimiter into an ArrayList of strings.
     *
     * @param data the string of strings to parse
     * @param delimiter the delimiter separating the strings
     * @return an ArrayList of strings
     */
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

    /**
     * Parses a string date into a Date object.
     *
     * @param dateStr the string date to parse
     * @return a Date object
     */
    public static Date parseDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Copies a file from the assets folder to the internal storage.
     *
     * @param context the application context
     * @param assetFileName the name of the file in the assets folder
     * @param outputFileName the name of the file in the internal storage
     */
    public static void copyFileFromAssets(Context context, String assetFileName, String outputFileName) {
        try (InputStream is = context.getAssets().open(assetFileName);
             OutputStream os = context.openFileOutput(outputFileName, Context.MODE_PRIVATE)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}