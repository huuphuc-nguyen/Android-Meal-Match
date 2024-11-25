package edu.utsa.cs3443.mealmatch.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
