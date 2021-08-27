package com.http.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.http.AbsHttp;
import com.http.ResponseObject;
import com.http.ResponseString;
import com.http.interceptor.SSLContextInterceptor;

import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * com.http.volley
 * 2018/10/18 14:53
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class VolleyHttp extends AbsHttp {
    private RequestQueue mQueue;

    public VolleyHttp(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    public void setSSLInterceptor(SSLContextInterceptor interceptor) {
        if (interceptor != null)
            HttpsURLConnection.setDefaultSSLSocketFactory(interceptor.interceptor().getSocketFactory());
    }

    @Override
    public ResponseString get(RequestEntity entity) {
        VolleyRequestString vrs = new VolleyRequestString(entity.url, Request.Method.GET, transform(entity.params), transform(entity.heard));
        vrs.setTag(entity.tag);
        mQueue.add(vrs);
        return vrs.get();
    }

    @Override
    public ResponseObject getObject(RequestEntity entity) {
        VolleyRequestObject vrs = new VolleyRequestObject<>(entity.url, Request.Method.GET, transform(entity.params), transform(entity.heard), entity.cls);
        vrs.setTag(entity.tag);
        mQueue.add(vrs);
        return vrs.get();
    }

    @Override
    public ResponseString post(RequestEntity entity) {
        VolleyRequestString vrs = null;
        if (entity.raw != null) {
            vrs = new VolleyRequestString(entity.url, Request.Method.POST, null, transform(entity.heard));
            vrs.setRaw(entity.raw, entity.type);
        } else
            vrs = new VolleyRequestString(entity.url, Request.Method.POST, transform(entity.params), transform(entity.heard));
        vrs.setTag(entity.tag);
        mQueue.add(vrs);
        return vrs.get();
    }

    @Override
    public ResponseObject postObject(RequestEntity entity) {
        VolleyRequestObject vrs = null;
        if (entity.raw != null) {
            vrs = new VolleyRequestObject(entity.url, Request.Method.POST, null, transform(entity.heard), entity.cls);
            vrs.setRaw(entity.raw, entity.type);
        } else
            vrs = new VolleyRequestObject(entity.url, Request.Method.POST, transform(entity.params), transform(entity.heard), entity.cls);
        vrs.setTag(entity.tag);
        mQueue.add(vrs);
        return vrs.get();
    }

    @Override
    public void getAsyn(RequestEntity entity) {
        VolleyRequestString vrs = new VolleyRequestString(entity.url, Request.Method.GET, transform(entity.params), transform(entity.heard));
        vrs.setListener(entity.stringListener);
        vrs.setTag(entity.tag);
        mQueue.add(vrs);
    }

    @Override
    public void getObjectAsyn(RequestEntity entity) {
        VolleyRequestObject vrs = new VolleyRequestObject<>(entity.url, Request.Method.GET, transform(entity.params), transform(entity.heard), entity.cls);
        vrs.setListener(entity.objectListener);
        vrs.setTag(entity.tag);
        mQueue.add(vrs);
    }

    @Override
    public void postAsyn(RequestEntity entity) {
        VolleyRequestString vrs = null;
        if (entity.raw != null) {
            vrs = new VolleyRequestString(entity.url, Request.Method.POST, null, transform(entity.heard));
            vrs.setRaw(entity.raw, entity.type);
        } else
            vrs = new VolleyRequestString(entity.url, Request.Method.POST, transform(entity.params), transform(entity.heard));
        vrs.setListener(entity.stringListener);
        vrs.setTag(entity.tag);
        mQueue.add(vrs);
    }

    @Override
    public void postObjectAsyn(RequestEntity entity) {
        VolleyRequestObject vrs = null;
        if (entity.raw != null) {
            vrs = new VolleyRequestObject(entity.url, Request.Method.POST, null, transform(entity.heard), entity.cls);
            vrs.setRaw(entity.raw, entity.type);
        } else
            vrs = new VolleyRequestObject(entity.url, Request.Method.POST, transform(entity.params), transform(entity.heard), entity.cls);
        vrs.setListener(entity.objectListener);
        vrs.setTag(entity.tag);
        mQueue.add(vrs);
    }

    private Map<String, String> transform(Map<String, Object> map) {
        Map<String, String> params = new HashMap<>();
        if (map != null && map.size() > 0)
            for (String key : map.keySet())
                params.put(key, String.valueOf(map.get(key)));
        return params;
    }

    @Override
    public void cancle(Object tag) {
        mQueue.cancelAll(tag);
    }
}
