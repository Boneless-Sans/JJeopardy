package com.boneless.util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("CallToPrintStackTrace")
public class JsonFile {
    private JsonFile() {
    }

    public static String read(String filename, String mainKey, String valueKey) {
        try (Reader reader = new FileReader(getFilePath(filename))) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            if (jsonObject.has(mainKey)) {
                Object mainValue = jsonObject.get(mainKey);

                if (mainValue instanceof JSONObject valuesObject) {

                    if (valuesObject.has(valueKey)) {
                        Object value = valuesObject.get(valueKey);

                        // Check the type of value and handle accordingly
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
                return "-1"; // Return a sentinel value for invalid mainKey
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return "invalid key";
    }

    public static String readWithThreeKeys(String filename, String mainKey, String subKey, String valueKey) {
        try (Reader reader = new FileReader(getFilePath(filename))) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            if (jsonObject.has(mainKey)) {
                Object mainValue = jsonObject.get(mainKey);

                if (mainValue instanceof JSONObject subObject) {

                    if (subObject.has(subKey)) {
                        Object valuesObject = subObject.get(subKey);

                        if (valuesObject instanceof JSONObject valueObject) {

                            if (valueObject.has(valueKey)) {
                                Object value = valueObject.get(valueKey);

                                // Check the type of value and handle accordingly
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
                return "-1"; // Return a sentinel value for invalid mainKey
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return "invalid key";
    }

    public static String[][] read2DArray(String filename, String mainKey) {
        try (Reader reader = new FileReader(getFilePath(filename))) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            if (jsonObject.has(mainKey)) {
                Object mainValue = jsonObject.get(mainKey);

                if (mainValue instanceof JSONArray outerArray) {
                    int rows = outerArray.length();
                    int cols = ((JSONArray) outerArray.get(0)).length(); // Assuming all inner arrays have the same length

                    String[][] resultArray = new String[rows][cols];

                    for (int i = 0; i < rows; i++) {
                        JSONArray innerArray = (JSONArray) outerArray.get(i);
                        for (int j = 0; j < cols; j++) {
                            resultArray[i][j] = innerArray.getString(j);
                        }
                    }

                    return resultArray;
                } else {
                    System.out.println("Invalid array type");
                }
            } else {
                System.out.println("Invalid mainKey");
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String readTwoKeys(String filename, String firstKey, String secondKey) {
        try (Reader reader = new FileReader(getFilePath(filename))) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            if (jsonObject.has(firstKey)) {
                Object firstValue = jsonObject.get(firstKey);

                if (firstValue instanceof JSONObject nestedObject) {

                    if (nestedObject.has(secondKey)) {
                        Object secondValue = nestedObject.get(secondKey);

                        if (secondValue instanceof String) {
                            // Return the single string value
                            return (String) secondValue;
                        } else {
                            System.out.println("Invalid value type for secondKey");
                        }
                    } else {
                        System.out.println("Invalid secondKey");
                    }
                } else {
                    System.out.println("Invalid nested object type");
                }
            } else {
                System.out.println("Invalid firstKey");
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Color[] readColorArray(String filename, String mainKey) {
        try (Reader reader = new FileReader(getFilePath(filename))) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            if (jsonObject.has(mainKey)) {
                Object mainValue = jsonObject.get(mainKey);

                if (mainValue instanceof JSONArray arrayValue) {
                    int length = arrayValue.length();
                    Color[] resultArray = new Color[length];

                    for (int i = 0; i < length; i++) {
                        resultArray[i] = stringToColor(arrayValue.getString(i));
                    }

                    return resultArray;
                } else {
                    System.out.println("Invalid array type");
                }
            } else {
                System.out.println("Invalid mainKey");
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Color stringToColor(String colorString) {
        if (colorString.startsWith("Color.")) {
            try {
                // Try to get predefined color by name
                return (Color) Color.class.getField(colorString.substring(6).toLowerCase()).get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (colorString.startsWith("new Color(") && colorString.endsWith(")")) {
            try {
                // Try to parse RGB values from the string
                String[] rgbValues = colorString.substring(11, colorString.length() - 1).split(",");
                int r = Integer.parseInt(rgbValues[0].trim());
                int g = Integer.parseInt(rgbValues[1].trim());
                int b = Integer.parseInt(rgbValues[2].trim());

                return new Color(r, g, b);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            // Try to get a color by the name directly (case-insensitive)
            try {
                java.lang.reflect.Field field = Color.class.getDeclaredField(colorString.toLowerCase());
                return (Color) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void writeToArray(String filename, String mainKey, String data) {
        try {
            JSONObject jsonObject = readJsonObject(filename);

            if (jsonObject == null) {
                jsonObject = new JSONObject();
            }

            JSONArray arrayValue = jsonObject.optJSONArray(mainKey);
            if (arrayValue == null) {
                arrayValue = new JSONArray();
                jsonObject.put(mainKey, arrayValue);
            }

            arrayValue.put(data);

            try (Writer writer = new FileWriter(getFilePath(filename))) {
                writer.write(jsonObject.toString(2)); // Indentation for better readability
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFilePath(String filename) {
        filename = (filename == null) ? "template.json" : filename;
        String directory;
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("windows") && filename.contains("C:\\") || OS.contains("mac") && (filename.contains("/Users/") || filename.contains("/var/"))) {
            directory = filename;
        } else {
            File file = new File("src/main/resources/data/" + filename);
            directory = file.getAbsolutePath();
        }
        return directory;
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

    public static int getIntValueFromJson(String filePath, String key) {
        try (Reader reader = new FileReader(filePath)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            return jsonObject.getInt(key);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.JSONException e) {
            System.out.println("Key not found or value is not an integer.");
        }
        return -1; // Return a default value in case of error
    }

    public static int getIntValueFromNestedJson(String filePath, String mainKey, String subKey) {
        try (Reader reader = new FileReader(filePath)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            JSONObject mainObject = jsonObject.getJSONObject(mainKey);
            return mainObject.getInt(subKey);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.JSONException e) {
            System.out.println("Key not found or value is not an integer.");
        }
        return -1; // Return a default value in case of error
    }

    public static int getColorAsIntFromJson(String filePath, String mainKey, String colorKey) {
        try (Reader reader = new FileReader(filePath)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            JSONObject mainObject = jsonObject.getJSONObject(mainKey);
            String[] rgb = mainObject.getString(colorKey).split(",");
            int r = Integer.parseInt(rgb[0].trim());
            int g = Integer.parseInt(rgb[1].trim());
            int b = Integer.parseInt(rgb[2].trim());
            return (r << 16) | (g << 8) | b;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } catch (org.json.JSONException e) {
            System.out.println("Key not found or value is not an integer.");
        }
        return -1; // Return a default value in case of error
    }

    public static int getFirstRGBValue(String filePath, String mainKey, String colorKey) {
        try (Reader reader = new FileReader(filePath)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            JSONObject mainObject = jsonObject.getJSONObject(mainKey);
            String[] rgb = mainObject.getString(colorKey).split(",");
            return Integer.parseInt(rgb[0].trim());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } catch (org.json.JSONException e) {
            System.out.println("Key not found or value is not an integer.");
        }
        return -1; // Return a default value in case of error
    }

    public static int getSecondRGBValue(String filePath, String mainKey, String colorKey) {
        try (Reader reader = new FileReader(filePath)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            JSONObject mainObject = jsonObject.getJSONObject(mainKey);
            String[] rgb = mainObject.getString(colorKey).split(",");
            return Integer.parseInt(rgb[1].trim());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } catch (org.json.JSONException e) {
            System.out.println("Key not found or value is not an integer.");
        }
        return -1; // Return a default value in case of error
    }

    public static int getThirdRGBValue(String filePath, String mainKey, String colorKey) {
        try (Reader reader = new FileReader(filePath)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);
            JSONObject mainObject = jsonObject.getJSONObject(mainKey);
            String[] rgb = mainObject.getString(colorKey).split(",");
            return Integer.parseInt(rgb[2].trim());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } catch (org.json.JSONException e) {
            System.out.println("Key not found or value is not an integer.");
        }
        return -1; // Return a default value in case of error
    }

}

