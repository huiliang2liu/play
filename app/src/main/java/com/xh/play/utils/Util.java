package com.xh.play.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Util {
    private final static int LENGTH = 1024 * 1024;
    private final static String CHARSET = "utf-8";

    public static String dealWithUrl(String url, String baseUlr, String host) {
        if(url==null||url.isEmpty())
            return url;
        if (url.startsWith("http:") | url.startsWith("https:"))
            return url;
        if (url.startsWith("//"))
            return baseUlr.startsWith("http:") ? "http:" + url : "https:" + url;
        if (url.startsWith("/"))
            return host + url;
        return baseUlr.substring(0, baseUlr.lastIndexOf("/")) + url;
    }

    public static String stream2string(InputStream is, String charset) {
        byte[] buff = stream2bytes(is);
        if (buff == null || buff.length <= 0)
            return null;
        if (charset == null || charset.isEmpty())
            charset = CHARSET;
        try {
            return new String(buff, charset);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public static byte[] stream2bytes(InputStream is) {
        if (is == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[LENGTH];
        byte[] arr = null;
        try {
            int len = is.read(buff);
            while (len > 0) {
                baos.write(buff, 0, len);
                len = is.read(buff);
            }
            arr = baos.toByteArray();
            baos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close(baos);
            close(is);
        }
        return arr;
    }

    public static void close(Closeable closeable) {
        if (closeable == null)
            return;
        try {
            closeable.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
