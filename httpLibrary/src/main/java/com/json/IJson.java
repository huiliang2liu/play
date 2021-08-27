package com.json;


import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
/**
 * User：liuhuiliang
 * Date：2019-12-27
 * Time：16:31
 * Descripotion：json数据解析
 * 大小 gson:232kb jackson:1.5m fastjson:673 loganSquare:307kb
 * 速度 fastjson>loganSquare>jackson>gson
 */
public interface IJson {
    public <T> T parasJson(String json, Class<T> cls);

    public <T> T parasJson(InputStream is, Class<T> cls);

    public <T> T parasJson(byte[] buff, Class<T> cls);

    public <T> T parasJson(Reader reader, Class<T> cls);

    public <T> T parasJson(File file, Class<T> cls);

    public <T> T parasJson(URL url, Class<T> cls);

    public <T> T parasJson(URI uri, Class<T> cls);

    public String object2string(Object o);

    public byte[] object2bytes(Object o);

    public InputStream object2stream(Object o);

    public Reader object2reader(Object o);
}
