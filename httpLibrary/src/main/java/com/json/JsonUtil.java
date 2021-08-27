package com.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    public static void put(String key, Object value, JSONObject jsonObject) {
        if (jsonObject == null || key == null || key.isEmpty() || value == null)
            return;
        try {
            jsonObject.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void put(String key, int value, JSONObject jsonObject) {
        if (jsonObject == null || key == null || key.isEmpty())
            return;
        try {
            jsonObject.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void put(String key, boolean value, JSONObject jsonObject) {
        if (jsonObject == null || key == null || key.isEmpty())
            return;
        try {
            jsonObject.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void put(String key, long value, JSONObject jsonObject) {
        if (jsonObject == null || key == null || key.isEmpty())
            return;
        try {
            jsonObject.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void put(String key, double value, JSONObject jsonObject) {
        if (jsonObject == null || key == null || key.isEmpty())
            return;
        try {
            jsonObject.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void put(String key, String value, JSONObject jsonObject) {
        if (jsonObject == null || key == null || key.isEmpty() || value == null)
            return;
        try {
            jsonObject.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void put(Map<String, Object> map, JSONObject jsonObject) {
        if (jsonObject == null || map == null || map.size() <= 0)
            return;
        Iterator<String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key1 = keys.next();
            put(key1, map.get(key1), jsonObject);
        }

    }

    public static Object get(String key, JSONObject jsonObject) {
        if (jsonObject == null)
            return null;
        return jsonObject.opt(key);

    }

    public static JSONObject getJSONObject(String key, JSONObject jsonObject) {
        if (jsonObject == null)
            return null;
        return jsonObject.optJSONObject(key);

    }

    public static JSONArray getJSONArray(String key, JSONObject jsonObject) {
        if (jsonObject == null)
            return null;
        return jsonObject.optJSONArray(key);

    }

    public static int getInt(String key, JSONObject jsonObject, int defaultValue) {
        if (jsonObject == null)
            return defaultValue;
        return jsonObject.optInt(key, defaultValue);
    }

    public static String getString(String key, JSONObject jsonObject, String defaultValue) {
        if (jsonObject == null)
            return defaultValue;
        return jsonObject.optString(key, defaultValue);
    }

    public static String getString(String key, JSONObject jsonObject) {
        return getString(key, jsonObject, "");
    }

    public static int getInt(String key, JSONObject jsonObject) {
        return getInt(key, jsonObject, 0);
    }

    public static long getLong(String key, JSONObject jsonObject,
                               long defaultValue) {
        if (jsonObject == null)
            return defaultValue;
        return jsonObject.optLong(key, defaultValue);
    }

    public static long getLong(String key, JSONObject jsonObject) {
        return getLong(key, jsonObject, 0);
    }

    public static double getDouble(String key, JSONObject jsonObject,
                                   double defaultValue) {
        if (jsonObject == null)
            return defaultValue;
        return jsonObject.optDouble(key, defaultValue);
    }

    public static double getDouble(String key, JSONObject jsonObject) {
        return getDouble(key, jsonObject, 0.0);
    }

    public static boolean getBoolean(String key, JSONObject jsonObject,
                                     boolean defaultValue) {
        if (jsonObject == null)
            return defaultValue;
        return jsonObject.optBoolean(key, defaultValue);
    }

    public static boolean getBoolean(String key, JSONObject jsonObject) {
        return getBoolean(key, jsonObject, false);
    }

    public static void put(int index, Object value, JSONArray jsonArray) {
        if (value == null || jsonArray == null)
            return;
        if (index < 0 || index >= jsonArray.length())
            jsonArray.put(value);
        else
            try {
                jsonArray.put(index, value);
            } catch (Exception e) {

            }
    }

    public static void put(Object value, JSONArray jsonArray) {
        put(-1, value, jsonArray);
    }

    public static void put(int index, int value, JSONArray jsonArray) {
        if (jsonArray == null)
            return;
        if (index < 0 || index >= jsonArray.length())
            jsonArray.put(value);
        else
            try {
                jsonArray.put(index, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void put(int value, JSONArray jsonArray) {
        put(-1, value, jsonArray);
    }

    public static void put(int index, boolean value, JSONArray jsonArray) {
        if (jsonArray == null)
            return;
        if (index < 0 || index >= jsonArray.length())
            jsonArray.put(value);
        else
            try {
                jsonArray.put(index, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void put(boolean value, JSONArray jsonArray) {
        put(-1, value, jsonArray);
    }

    public static void put(int index, long value, JSONArray jsonArray) {
        if (jsonArray == null)
            return;
        if (index < 0 || index >= jsonArray.length())
            jsonArray.put(value);
        else
            try {
                jsonArray.put(index, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void put(long value, JSONArray jsonArray) {
        put(-1, value, jsonArray);
    }

    public static void put(int index, double value, JSONArray jsonArray) {
        if (jsonArray == null)
            return;
        if (index < 0 || index >= jsonArray.length())
            try {
                jsonArray.put(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        else
            try {
                jsonArray.put(index, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void put(int index, String value, JSONArray jsonArray) {
        if (jsonArray == null || value == null)
            return;
        if (index < 0 || index > jsonArray.length())
            jsonArray.put(value);
        else
            try {
                jsonArray.put(index, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void put(List<Object> objects, JSONArray jsonArray) {
        if (jsonArray == null || objects == null || objects.size() <= 0)
            return;
        Iterator<Object> keys = objects.iterator();
        while (keys.hasNext()) {
            put(-1, keys.next(), jsonArray);
        }

    }

    public static Object get(int index, JSONArray jsonArray) {
        if (jsonArray == null || index < 0 || index >= jsonArray.length())
            return null;
        return jsonArray.opt(index);
    }

    public static JSONObject getJSONObject(int index, JSONArray jsonArray) {
        if (jsonArray == null || index < 0 || index >= jsonArray.length())
            return null;
        return jsonArray.optJSONObject(index);
    }

    public static JSONArray getJSONArray(int index, JSONArray jsonArray) {
        if (jsonArray == null || index < 0 || index >= jsonArray.length())
            return null;
        return jsonArray.optJSONArray(index);
    }

    public static int getInt(int index, JSONArray jsonArray, int defaultValue) {
        if (jsonArray == null || index < 0 || index >= jsonArray.length())
            return defaultValue;
        return jsonArray.optInt(index, defaultValue);
    }

    public static int getInt(int index, JSONArray jsonArray) {
        return getInt(index, jsonArray, 0);
    }

    public static String getString(int index, JSONArray jsonArray, String defaultValue) {
        if (jsonArray == null || index < 0 || index >= jsonArray.length())
            return defaultValue;
        return jsonArray.optString(index, defaultValue);
    }

    public static String getString(int index, JSONArray jsonArray) {
        return getString(index, jsonArray, "");
    }

    public static long getLong(int index, JSONArray jsonArray, long defaultValue) {
        if (jsonArray == null || index < 0 || index >= jsonArray.length())
            return defaultValue;
        return jsonArray.optLong(index, defaultValue);
    }

    public static long getLong(int index, JSONArray jsonArray) {
        return getLong(index, jsonArray, 0);
    }

    public static double getDouble(int index, JSONArray jsonArray,
                                   double defaultValue) {
        if (jsonArray == null || index < 0 || index >= jsonArray.length())
            return defaultValue;
        return jsonArray.optDouble(index, defaultValue);
    }

    public static double getDouble(int index, JSONArray jsonArray) {
        return getDouble(index, jsonArray, 0.0);
    }

    public static boolean getBoolean(int index, JSONArray jsonArray,
                                     boolean defaultValue) {
        if (jsonArray == null || index < 0 || index >= jsonArray.length())
            return defaultValue;
        return jsonArray.optBoolean(index, defaultValue);
    }

    public static boolean getBoolean(int index, JSONArray jsonArray) {
        return getBoolean(index, jsonArray, false);
    }
}
