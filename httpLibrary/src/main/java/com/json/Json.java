package com.json;

import android.util.Log;

import com.json.fastJson.FastJson;
import com.json.gson.Gson;
import com.json.jackson.Jackson;
import com.json.loganSquare.LoganSquare;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;

/**
 * com.json.loganSquare
 * 2018/10/16 12:04
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class Json {
    private final static String TAG = "Json";
    private static volatile IJson json;

    static {
        if (isClass("com.alibaba.fastjson.JSONObject")) {
            json = new FastJson();
            Log.i(TAG, "fastjson");
        } else if (isClass("com.google.gson.Gson")) {
            json = new Gson();
            Log.i(TAG, "gson");
        } else if (isClass("com.fasterxml.jackson.databind.ObjectMapper")) {
            json = new Jackson();
            Log.i(TAG, "jackson");
        } else if (isClass("com.bluelinelabs.logansquare.LoganSquare")) {
            json = new LoganSquare();
            Log.i(TAG, "logansquare");
        } else
            throw new RuntimeException("not found paras json");
    }

    public static <T> T parasJson(String json, Class<T> cls) {
        return Json.json.parasJson(json, cls);
    }

    public static <T> T parasJson(InputStream is, Class<T> cls) {
        return json.parasJson(is, cls);
    }

    public static <T> T parasJson(byte[] buff, Class<T> cls) {
        return json.parasJson(buff, cls);
    }

    public static <T> T parasJson(Reader reader, Class<T> cls) {
        return json.parasJson(reader, cls);
    }

    public static <T> T parasJson(File file, Class<T> cls) {
        return json.parasJson(file, cls);
    }

    public static <T> T parasJson(URL url, Class<T> cls) {
        return json.parasJson(url, cls);
    }

    public static <T> T parasJson(URI uri, Class<T> cls) {
        return json.parasJson(uri, cls);
    }

    public static String object2string(Object o) {
        return json.object2string(o);
    }

    public static byte[] object2bytes(Object o) {
        return json.object2bytes(o);
    }

    public static InputStream object2stream(Object o) {
        return json.object2stream(o);
    }

    public static Reader object2reader(Object o) {
        return json.object2reader(o);
    }

    public static boolean object2file(Object o, File file) {
        InputStream is=object2stream(o);
        if (is == null || file == null) {
            return false;
        }
        File target = new File(file.getParentFile(), System.currentTimeMillis() + "");
        FileOutputStream fos = null;
        boolean success = false;
        try {
            fos = new FileOutputStream(target);
            byte[] buff = new byte[1024*1024];
            int len = is.read(buff);
            while (len > 0) {
                fos.write(buff, 0, len);
                len = is.read(buff);
            }
            success = target.renameTo(file);
            fos.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(fos!=null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    private static boolean isClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
