package com.json.loganSquare;

import com.json.IJson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;


/**
 * com.json.loganSquare
 * 2018/10/16 11:12
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class LoganSquare implements IJson {
    @Override
    public <T> T parasJson(String json, Class<T> cls) {
        try {
            return com.bluelinelabs.logansquare.LoganSquare.parse(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T parasJson(InputStream is, Class<T> cls) {
        try {
            return com.bluelinelabs.logansquare.LoganSquare.parse(is, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T parasJson(byte[] buff, Class<T> cls) {
        return parasJson(new ByteArrayInputStream(buff), cls);
    }

    @Override
    public <T> T parasJson(Reader reader, Class<T> cls) {
        try {
            BufferedReader br = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                line.trim();
                if (line.isEmpty())
                    continue;
                sb.append(line);
            }
            br.close();
            reader.close();
            return parasJson(sb.toString(), cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T parasJson(File file, Class<T> cls) {
        try {
            return parasJson(new FileInputStream(file), cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T parasJson(URL url, Class<T> cls) {
        try {
            return parasJson(url.openStream(), cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T parasJson(URI uri, Class<T> cls) {
        try {
            return parasJson(uri.toURL(), cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String object2string(Object o) {
        try {
            return com.bluelinelabs.logansquare.LoganSquare.serialize(o);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] object2bytes(Object o) {
        String s = object2string(o);
        if (s == null)
            return null;
        return s.getBytes();
    }

    @Override
    public InputStream object2stream(Object o) {
        return new ByteArrayInputStream(object2bytes(o));
    }

    @Override
    public Reader object2reader(Object o) {
        return new InputStreamReader(object2stream(o));
    }
}
