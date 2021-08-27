package com.json.fastJson;

import com.alibaba.fastjson.JSONObject;
import com.json.IJson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;


/**
 * com.json.fastJson
 * 2018/10/16 10:43
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class FastJson implements IJson {
    @Override
    public <T> T parasJson(String json, Class<T> cls) {
        return JSONObject.parseObject(json, cls);
    }

    @Override
    public <T> T parasJson(InputStream is, Class<T> cls) {
        try {
            return JSONObject.parseObject(is, cls);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T parasJson(byte[] buff, Class<T> cls) {
        return JSONObject.parseObject(buff, cls);
    }

    @Override
    public <T> T parasJson(Reader reader, Class<T> cls) {

        try {
            BufferedReader in = new BufferedReader(reader);
            StringBuffer buffer = new StringBuffer();
            String line = " ";
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                buffer.append(line);
            }
            in.close();
            reader.close();
            return parasJson(buffer.toString(), cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T parasJson(File file, Class<T> cls) {
        try {
            return parasJson(new FileInputStream(file), cls);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T parasJson(URL url, Class<T> cls) {
        try {
            return parasJson(url.openStream(), cls);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T parasJson(URI uri, Class<T> cls) {
        try {
            return parasJson(uri.toURL(), cls);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String object2string(Object o) {
        return JSONObject.toJSONString(o);
    }

    @Override
    public byte[] object2bytes(Object o) {
        return object2string(o).getBytes();
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
