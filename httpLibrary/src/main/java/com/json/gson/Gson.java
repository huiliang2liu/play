package com.json.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.json.IJson;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;


public class Gson implements IJson {
    com.google.gson.Gson mGson;

    {
        mGson = new GsonBuilder()
//                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式
//                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
                .setPrettyPrinting() //对json结果格式化.
                .setVersion(1.0)
                .disableHtmlEscaping()//默认是GSON把HTML 转义的，但也可以设置不转义
                .serializeNulls()//把null值也转换，默认是不转换null值的，可以选择也转换,为空时输出为{a:null}，而不是{}
                .create();
//        mGson=new com.google.gson.Gson();
    }

    @Override
    public <T> T parasJson(String json, Class<T> cls) {
        return mGson.fromJson(json, cls);
    }

    @Override
    public <T> T parasJson(InputStream is, Class<T> cls) {
        return parasJson(new InputStreamReader(is), cls);
    }

    @Override
    public <T> T parasJson(byte[] buff, Class<T> cls) {
        return parasJson(new ByteArrayInputStream(buff), cls);
    }

    @Override
    public <T> T parasJson(Reader reader, Class<T> cls) {
        return mGson.fromJson(reader, cls);
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
            return parasJson(uri.toURL().openStream(), cls);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String object2string(Object o) {
        return mGson.toJson(o);
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
