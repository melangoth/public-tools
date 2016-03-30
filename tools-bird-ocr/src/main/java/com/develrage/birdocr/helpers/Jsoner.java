package com.develrage.birdocr.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Jsoner {
    public static String getJsonValue(String key, String typeref, String json)
            throws JSONException {
        JSONObject obj;
        obj = new JSONObject(json);
        return obj.getString(key);
    }

    public static double getJsonValue(String key, double typeref, String json)
            throws JSONException {
        JSONObject obj;
        obj = new JSONObject(json);
        return obj.getDouble(key);
    }

    public static int getJsonValue(String key, int typeref, String json)
            throws JSONException {
        JSONObject obj;
        obj = new JSONObject(json);
        return obj.getInt(key);
    }

    public static boolean getJsonValue(String key, boolean typeref, String json)
            throws JSONException {
        JSONObject obj;
        obj = new JSONObject(json);
        return obj.getBoolean(key);
    }

    public static int[] getJsonArrayValues(String key, int typeref, String json)
            throws JSONException {
        JSONObject obj;
        obj = new JSONObject(json);
        JSONArray arr;
        arr = obj.getJSONArray(key);
        int[] values = new int[arr.length()];
        for (int i = 0; i < arr.length(); i++) {
            values[i] = arr.getInt(i);
        }
        return values;
    }

    public static String[] getJsonArrayValues(String key, String typeref,
                                              String json) throws JSONException {
        JSONObject obj;
        obj = new JSONObject(json);
        JSONArray arr;
        arr = obj.getJSONArray(key);
        String[] values = new String[arr.length()];
        for (int i = 0; i < arr.length(); i++) {
            values[i] = arr.getString(i);
        }
        return values;
    }

    public static double[] getJsonArrayValues(String key, double typeref,
                                              String json) throws JSONException {
        JSONObject obj;
        obj = new JSONObject(json);
        JSONArray arr;
        arr = obj.getJSONArray(key);
        double[] values = new double[arr.length()];
        for (int i = 0; i < arr.length(); i++) {
            values[i] = arr.getDouble(i);
        }
        return values;
    }
}
