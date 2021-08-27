package com.http;

import android.content.Context;

import com.http.asyn.AsynHttp;
import com.http.okhttp.OkHttp;
import com.http.volley.VolleyHttp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class HttpTest {
    private static final String TEST_URL="http://ip-api.com/json?lang=zh-CN";
    private Http.RequestEntity entity;
    private Context context;
    @Before
    public void setUp() throws Exception {
        entity=new Http.RequestEntity();
        entity.url=TEST_URL;
        context= InstrumentationRegistry.getInstrumentation().getContext();
    }

    @Test
    public void get() {
        Http http=new OkHttp();
//        assertFalse(http.get(entity).response.isEmpty());
        http=new AsynHttp();
        assertFalse(http.get(entity).response.isEmpty());
        http=new com.http.http.Http();
        assertFalse(http.get(entity).response.isEmpty());
        http = new VolleyHttp(context);
        assertFalse(http.get(entity).response.isEmpty());
    }
}