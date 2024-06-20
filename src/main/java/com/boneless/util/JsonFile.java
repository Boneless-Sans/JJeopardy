package com.boneless.util;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.io.*;

public class JsonFile {
    private JsonFile() {} //disable no arg so the class can't be created

    public static String read(String filename, String mainKey, String valueKey) {
        try (Reader reader = new FileReader(getFilePath(filename))) {
            JSONTokener tokenizer = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokenizer);

            if (jsonObject.has(mainKey)) {
                Object mainValue = jsonObject.get(mainKey);

                if (mainValue instanceof JSONObject valuesObject) {

                    if (valuesObject.has(valueKey)) {
                        Object value = valuesObject.get(valueKey);

                        if (value instanceof String) {
                            return (String) value;
                        } else if (value instanceof Number) {
                            return String.valueOf(value);
                        } else {
                            return "invalid value type";
                        }
                    } else {
                        return "invalid key";
                    }
                } else {
                    return "invalid mainKey type";
                }
            } else {
                return "-1";
            }

        } catch (IOException | JSONException ignored) {}

        return "invalid key";
    }

    public static String readWith3Keys(String filename, String mainKey, String subKey, String valueKey) {
        try (Reader reader = new FileReader(getFilePath(filename))) {
            JSONTokener tokenizer = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokenizer);

            if (jsonObject.has(mainKey)) {
                Object mainValue = jsonObject.get(mainKey);

                if (mainValue instanceof JSONObject subObject) {

                    if (subObject.has(subKey)) {
                        Object valuesObject = subObject.get(subKey);

                        if (valuesObject instanceof JSONObject valueObject) {

                            if (valueObject.has(valueKey)) {
                                Object value = valueObject.get(valueKey);

                                if (value instanceof String) {
                                    return (String) value;
                                } else if (value instanceof Number) {
                                    return String.valueOf(value);
                                } else {
                                    return "invalid value type";
                                }
                            } else {
                                return "invalid key";
                            }
                        } else {
                            return "invalid subKey type";
                        }
                    } else {
                        return "invalid subKey";
                    }
                } else {
                    return "invalid mainKey type";
                }
            } else {
                return "-1"; //return value for invalid mainKey
            }

        } catch (IOException | JSONException ignored) {}

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
                writer.write(jsonObject.toString(2));
            }

        } catch (IOException ignored) {}
    }

    public static void writeln3Keys(String filename, String key1, String key2, String key3, String value) {
        try {
            //read data from json
            JSONObject jsonObject = readJsonObject(filename);

            //if its empty (shouldn't be), make sure it's properly formatted, so it doesn't go ka-boom
            if (jsonObject == null) {
                jsonObject = new JSONObject();
            }

            //navigate
            JSONObject level1Object = jsonObject.optJSONObject(key1);
            if (level1Object == null) {
                level1Object = new JSONObject();
                jsonObject.put(key1, level1Object);
            }

            //navigate more
            JSONObject level2Object = level1Object.optJSONObject(key2);
            if (level2Object == null) {
                level2Object = new JSONObject();
                level1Object.put(key2, level2Object);
            }

            //put date into key set
            level2Object.put(key3, value);

            //write
            try (Writer writer = new FileWriter(getFilePath(filename))) {
                writer.write(jsonObject.toString(2)); //indent
            }

        } catch (IOException ignored) {}
    }

    //delete object with 2 keys
    public static void delete(String filename, String mainKey, String valueKey) {
        try {
            JSONObject jsonObject = readJsonObject(filename);

            if (jsonObject != null && jsonObject.has(mainKey)) {
                JSONObject valuesObject = jsonObject.optJSONObject(mainKey);

                if (valuesObject != null && valuesObject.has(valueKey)) {
                    valuesObject.remove(valueKey);

                    try (Writer writer = new FileWriter(getFilePath(filename))) {
                        writer.write(jsonObject.toString(2));
                    }
                }
            }

        } catch (IOException ignored) {}
    }

    //delete object with 3 keys
    public static void delete3Keys(String filename, String key1, String key2, String key3) {
        try {
            JSONObject jsonObject = readJsonObject(filename);

            if (jsonObject != null && jsonObject.has(key1)) {
                JSONObject level1Object = jsonObject.optJSONObject(key1);

                if (level1Object != null && level1Object.has(key2)) {
                    JSONObject level2Object = level1Object.optJSONObject(key2);

                    if (level2Object != null && level2Object.has(key3)) {
                        level2Object.remove(key3);

                        try (Writer writer = new FileWriter(getFilePath(filename))) {
                            writer.write(jsonObject.toString(2));
                        }
                    }
                }
            }

        } catch (IOException ignored) {}
    }

    private static String getFilePath(String filename) {
        String directory;
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("windows") && filename.contains(":\\") || OS.contains("mac") && (filename.contains("/Users/") || filename.contains("/var/"))) {
            directory = filename;
        } else {
            File file = new File("src/main/resources/data/" + filename);
            directory = file.getAbsolutePath();
        }
        return directory;
    }

    private static JSONObject readJsonObject(String filename) {
        try (Reader reader = new FileReader(getFilePath(filename))) {
            JSONTokener tokenizer = new JSONTokener(reader);
            return new JSONObject(tokenizer);
        } catch (IOException ignored) {}
        return null;
    }
}