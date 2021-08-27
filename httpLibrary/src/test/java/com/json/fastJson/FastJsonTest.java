package com.json.fastJson;

import android.text.TextUtils;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.json.IJson;
import com.json.gson.Gson;
import com.json.jackson.Jackson;
import com.json.loganSquare.LoganSquare;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static org.junit.Assert.*;

/**
 * User：liuhuiliang
 * Date：2019-12-27
 * Time：14:58
 * Descripotion：Json测试
 */
public class FastJsonTest {
    private static final String textUrl = "http://ip-api.com/json?lang=zh-CN";
    private String text;
    private TestClass testClass;

    @Before
    public void setUp() throws Exception {
        URL url = new URL(textUrl);
        URLConnection connection = url.openConnection();
        connection.connect();
        InputStream is = connection.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byff = new byte[1024 * 1024];
        int len = -1;
        while ((len = is.read(byff)) > 0)
            byteArrayOutputStream.write(byff, 0, len);
        text = new String(byteArrayOutputStream.toByteArray());
        System.err.println(text);
        is.close();
        byteArrayOutputStream.close();
        testClass = new TestClass();
        testClass.status = "success";
        testClass.country = "中国";
        testClass.countryCode = "CN";
        testClass.region = "BJ";
    }

    @Test
    public void parasJson() {
        assertEquals(new FastJson().parasJson(text, TestClass.class), testClass);
        assertEquals(new Gson().parasJson(text, TestClass.class), testClass);
//        System.out.println(new Jackson().object2string(testClass));
//        System.out.println(new Jackson().parasJson(text, TestClass.class));
        assertEquals(new Jackson().parasJson(text, TestClass.class), testClass);
//        assertEquals(new LoganSquare().parasJson(text, TestClass.class), testClass);
    }

    @Test
    public void object2string() {
        IJson iJson=new Gson();
        assertEquals(testClass,iJson.parasJson(iJson.object2reader(testClass),TestClass.class));
        iJson=new Jackson();
        assertEquals(testClass,iJson.parasJson(iJson.object2reader(testClass),TestClass.class));
        iJson=new FastJson();
        assertEquals(testClass,iJson.parasJson(iJson.object2reader(testClass),TestClass.class));
    }

//    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TestClass {
        public String status;
        public String country;
        public String countryCode;
        public String region;

        @Override
        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof TestClass))
                return false;
            TestClass t = (TestClass) obj;
            return status.equals(t.status) &&
                    country.equals(t.country) &&
                    countryCode.equals(t.countryCode) &&
                    region.equals(t.region);
        }

        @NonNull
        @Override
        public String toString() {
            return String.format("{status:%s,country:%s,countryCode:%s,region:%s}", status, country, countryCode, region);
        }
    }
}