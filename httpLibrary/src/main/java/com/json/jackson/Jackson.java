package com.json.jackson;

import com.json.IJson;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;


/**
 * com.json.jackson
 * 2018/10/16 10:33
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class Jackson implements IJson {
    @Override
    public <T> T parasJson(String json, Class<T> cls) {
        return JacksonUtil.formJson(json, cls);
    }

    @Override
    public <T> T parasJson(InputStream is, Class<T> cls) {
        return JacksonUtil.formJson(is, cls);
    }

    @Override
    public <T> T parasJson(byte[] buff, Class<T> cls) {
        return JacksonUtil.formJson(buff, cls);
    }

    @Override
    public <T> T parasJson(Reader reader, Class<T> cls) {
        return JacksonUtil.formJson(reader, cls);
    }

    @Override
    public <T> T parasJson(File file, Class<T> cls) {
        return JacksonUtil.formJson(file, cls);
    }

    @Override
    public <T> T parasJson(URL url, Class<T> cls) {
        return JacksonUtil.formJson(url, cls);
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
        return JacksonUtil.toJson(o);
    }

    @Override
    public byte[] object2bytes(Object o) {
        return JacksonUtil.toJsonByte(o);
    }

    @Override
    public InputStream object2stream(Object o) {
        return JacksonUtil.toJsonStream(o);
    }

    @Override
    public Reader object2reader(Object o) {
        return new InputStreamReader(object2stream(o));
    }
}
