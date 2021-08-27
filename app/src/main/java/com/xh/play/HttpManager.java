package com.xh.play;

import android.content.Context;

import com.http.Http;
import com.http.Response;
import com.http.ResponseString;
import com.http.listen.ResponseListener;
import com.http.listen.ResponseObjectListener;
import com.http.listen.ResponseStringListener;
import com.json.Json;
import com.xh.base.log.Logger;
import com.xh.play.entities.AssetsUrl;

import java.io.IOException;
import java.util.Map;

public class HttpManager {
    private static final String TAG = "HttpManager";
    private Context context;
    private Http http;
    private AssetsUrl url;

    public HttpManager(Context context) {
        this.context = context;
        http = new Http.Build().context(context).build();
        try {
            url = Json.parasJson(context.getAssets().open("test.json"), AssetsUrl.class);
            StringBuilder sb = new StringBuilder();
            if (url.urls != null)
                for (AssetsUrl.AUrl a : url.urls) {
                    if (a.url == null || a.url.isEmpty())
                        continue;
                    if (!a.url.startsWith("http://") && !a.url.startsWith("https://")) {
                        sb.append(url.host).append("/").append(a.url);
                        a.url = sb.toString();
                        sb.setLength(0);
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void request(ResponseListener listener) {
        request(null, null, null, listener);
    }

    public void request(Map<String, Object> params, ResponseListener listener) {
        request(params, null, null, listener);
    }

    public void request(String raw, Map<String, Object> headers, ResponseListener listener) {
        request(null, headers, raw, listener);
    }

    public void request(Map<String, Object> params, Map<String, Object> headers, ResponseListener listener) {
        request(params, headers, null, listener);
    }

    private void request(Map<String, Object> params, Map<String, Object> headers, String raw, ResponseListener listener) {
        if (url == null || url.urls == null || url.urls.length <= 0) {
            if (listener != null)
                listener.failure();
            return;
        }
        String method = getMethodName();
        for (AssetsUrl.AUrl a : url.urls) {
            if (method.equals(a.getMethod())) {
                Class c = aurl2class(a);
                Http.RequestEntity entity = entity(a, params, headers, raw);
                if (c == null) {
                    entity.stringListener = (ResponseStringListener) listener;
                    if (a.isPost) {
                        http.postAsyn(entity);
                        return;
                    }
                    http.getAsyn(entity);
                    return;
                }
                entity.cls = c;
                entity.objectListener = (ResponseObjectListener) listener;
                if (a.isPost) {
                    http.postObjectAsyn(entity);
                    return;
                }
                http.getObjectAsyn(entity);
                return;
            }
        }
        if (listener != null)
            listener.failure();
        return;
    }

    public Response request() {
        return request((Map<String, Object>) null, (Map<String, Object>) null, (String) null);
    }

    public Response request(Map<String, Object> params) {
        return request(params, (Map<String, Object>) null, (String) null);
    }

    public Response request(String raw, Map<String, Object> headers) {
        return request((Map<String, Object>) null, headers, raw);
    }

    public Response request(Map<String, Object> params, Map<String, Object> headers) {
        return request(params, headers, (String) null);
    }

    private Response request(Map<String, Object> params, Map<String, Object> headers, String raw) {
        if (url == null || url.urls == null || url.urls.length <= 0)
            return null;
        String method = getMethodName();
        for (AssetsUrl.AUrl a : url.urls) {
            if (method.equals(a.getMethod())) {
                Class c = aurl2class(a);
                Http.RequestEntity entity = entity(a, params, headers, raw);
                if (c == null) {
                    if (a.isPost)
                        return http.post(entity);
                    return http.get(entity);
                }
                entity.cls = c;
                if (a.isPost)
                    return http.postObject(entity);
                return http.getObject(entity);
            }
        }
        return null;
    }

    private Http.RequestEntity entity(AssetsUrl.AUrl aUrl, Map<String, Object> params, Map<String, Object> headers, String raw) {
        Http.RequestEntity entity = new Http.RequestEntity();
        entity.url = aUrl.url;
        entity.params = params;
        if (raw != null)
            try {
                entity.raw = raw.getBytes("utf-8");
            } catch (Exception e) {
                entity.raw = raw.getBytes();
            }
        entity.heard = headers;
        return entity;
    }

    private Class aurl2class(AssetsUrl.AUrl aUrl) {
        if (aUrl.clazz != null && !aUrl.clazz.isEmpty())
            try {
                return Class.forName(aUrl.clazz);
            } catch (Exception e) {
                if (url.packages != null && url.packages.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (String pa : url.packages) {
                        sb.append(pa).append(".").append(aUrl.clazz);
                        try {
                            String name = sb.toString();
                            Class cla = Class.forName(name);
                            aUrl.clazz = name;
                            return cla;
                        } catch (Exception classNotFoundException) {
                            sb.setLength(0);
                        }
                    }
                }
            }
        return null;
    }

    public void test(ResponseListener listener) {
        Logger.e(Logger.TAG, getMethodName());
    }

    private String getMethodName() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length < 6)
            return "";
        return elements[5].getMethodName();
    }
}
