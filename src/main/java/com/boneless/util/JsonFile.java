package com.boneless.util;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

public class JsonFile {

    private static final String DEFAULT_DIRECTORY = "/src/main/resources/data/";

    public static String read(String filename, String mainKey, String valueKey) {
        try (Reader reader = new FileReader(getFilePath(filename))) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            if (jsonObject.has(mainKey)) {
                JSONObject valuesObject = jsonObject.getJSONObject(mainKey);

                if (valuesObject.has(valueKey)) {
                    return valuesObject.getString(valueKey);
                } else {
                    return "invalid key";
                }
            } else {
                return "invalid mainKey";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "invalid key";
    }

    public static void writeln(String filename, String mainKey, String valueKey, String value) {
        try {
            JSONObject jsonObject = readJsonObject(filename);

            if (jsonObject == null) {
                jsonObject = new JSONObject();
            }

            JSONObject valuesObject = jsonObject.optJSONObject(mainKey);
            if (valuesObject == null) {
                valuesObject = new JSONObject();
                jsonObject.put(mainKey, valuesObject);
            }

            valuesObject.put(valueKey, value);

            try (Writer writer = new FileWriter(getFilePath(filename))) {
                writer.write(jsonObject.toString(2)); // Indentation for better readability
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void create(String filename, String mainKey, String valueKey, String value) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(mainKey, new JSONObject());
            jsonObject.getJSONObject(mainKey).put(valueKey, value);

            try (Writer writer = new FileWriter(getFilePath(filename))) {
                writer.write(jsonObject.toString(2)); // Indentation for better readability
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFilePath(String filename) {
        String directory = System.getProperty("user.dir") + DEFAULT_DIRECTORY;
        return directory + filename;
    }

    private static JSONObject readJsonObject(String filename) {
        try (Reader reader = new FileReader(getFilePath(filename))) {
            JSONTokener tokener = new JSONTokener(reader);
            return new JSONObject(tokener);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
