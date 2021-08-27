package com.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

/**
 * com.json.jackson
 * 2018/10/16 10:32
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class JacksonUtil {
    public static String toJson(Object object) {
        if (object == null)
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] toJsonByte(Object object) {
        if (object == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static InputStream toJsonStream(Object object) {
        byte[] buff = toJsonByte(object);
        if (buff == null)
            return null;
        return new ByteArrayInputStream(buff);
    }

    public static <T> T formJson(String json, Class<T> cl) {
        if (json == null || json.isEmpty() || cl == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, cl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T formJson(File file, Class<T> cl) {
        if (file == null || !file.exists() || file.isAbsolute() || cl == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(file, cl);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T formJson(URL json, Class<T> cl) {
        if (json == null || cl == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, cl);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T formJson(byte[] buff, Class<T> cl) {
        if (buff == null || buff.length <= 0 || cl == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(buff, cl);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T formJson(InputStream inputStream, Class<T> cl) {
        if (inputStream == null || cl == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(inputStream, cl);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T formJson(Reader reader, Class<T> cl) {
        if (reader == null || cl == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(reader, cl);
        } catch (Exception e) {
            return null;
        }
    }
}
