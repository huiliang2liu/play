package com.http.asyn;

import com.http.AbsHttp;
import com.http.FileHttp;
import com.http.ResponseObject;
import com.http.ResponseString;
import com.http.interceptor.SSLContextInterceptor;
import com.http.listen.ResponseObjectListener;
import com.http.listen.ResponseStringListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * com.http.asyn
 * 2018/10/18 17:14
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class AsynHttp extends AbsHttp implements FileHttp {
    Map<Object, List<RequestHandle>> handleMap = new HashMap<>();
    AsyncHttpClient httpClient = new AsyncHttpClient();

    public void setSSLInterceptor(SSLContextInterceptor interceptor) {
        if (interceptor != null) {
            httpClient.setSSLSocketFactory(new cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory(interceptor.interceptor()));
        }
    }

    @Override
    public ResponseString get(RequestEntity entity) {
        AysnResponseString ars = new AysnResponseString(entity.tag, this);
        RequestHandle handle = getClient(entity.heard).get(url(entity.url, entity.params), ars);
        ars.setHandle(handle);
        put(entity.tag, handle);
        return ars.get();
    }

    @Override
    public ResponseObject getObject(RequestEntity entity) {
        AysnResponseObject aro = new AysnResponseObject(entity.cls, entity.tag, this);
        RequestHandle handle = getClient(entity.heard).get(entity.url, new RequestParams(entity.params), aro);
        aro.setHandle(handle);
        put(entity.tag, handle);
        return aro.get();
    }

    @Override
    public ResponseString post(RequestEntity entity) {
        AysnResponseString ars = new AysnResponseString(entity.tag, this);
        RequestHandle handle = null;
        if (entity.raw != null) {
            handle = getClient(entity.heard).post(null, entity.url, new RawEntity(entity.raw, entity.type), null, ars);
        } else
            handle = getClient(entity.heard).post(entity.url, new RequestParams(entity.params), ars);
        ars.setHandle(handle);
        put(entity.tag, handle);
        return ars.get();
    }

    @Override
    public ResponseObject postObject(RequestEntity entity) {
        AysnResponseObject aro = new AysnResponseObject(entity.cls, entity.tag, this);
        RequestHandle handle = null;
        if (entity.raw != null) {
            handle = getClient(entity.heard).post(null, entity.url, new RawEntity(entity.raw, entity.type), null, aro);
        } else
            handle = getClient(entity.heard).post(entity.url, new RequestParams(entity.params), aro);
        aro.setHandle(handle);
        put(entity.tag, handle);
        return aro.get();
    }

    @Override
    public void getAsyn(RequestEntity entity) {
        AysnResponseString ars = new AysnResponseString(entity.tag, this);
        ars.setListener(entity.stringListener);
        RequestHandle handle = getClient(entity.heard).get(url(entity.url, entity.params), ars);
        ars.setHandle(handle);
        put(entity.tag, handle);
    }

    @Override
    public void getObjectAsyn(RequestEntity entity) {
        AysnResponseObject aro = new AysnResponseObject(entity.cls, entity.tag, this);
        aro.setListener(entity.objectListener);
        RequestHandle handle = getClient(entity.heard).get(entity.url, new RequestParams(entity.params), aro);
        aro.setHandle(handle);
        put(entity.tag, handle);
    }

    @Override
    public void postAsyn(RequestEntity entity) {
        AysnResponseString ars = new AysnResponseString(entity.tag, this);
        RequestHandle handle = null;
        if (entity.raw != null) {
            handle = getClient(entity.heard).post(null, entity.url, new RawEntity(entity.raw, entity.type), null, ars);
        } else
            handle = getClient(entity.heard).post(entity.url, new RequestParams(entity.params), ars);
        ars.setHandle(handle);
        put(entity.tag, handle);
    }

    @Override
    public void postObjectAsyn(RequestEntity entity) {
        AysnResponseObject aro = new AysnResponseObject(entity.cls, entity.tag, this);
        aro.setListener(entity.objectListener);
        RequestHandle handle = null;
        if (entity.raw != null) {
            handle = getClient(entity.heard).post(null, entity.url, new RawEntity(entity.raw, entity.type), null, aro);
        } else
            handle = getClient(entity.heard).post(entity.url, new RequestParams(entity.params), aro);
        aro.setHandle(handle);
        put(entity.tag, handle);
    }

    @Override
    public synchronized void cancle(Object tag) {
        List<RequestHandle> handles = handleMap.get(tag);
        if (handles == null || handles.size() <= 0)
            return;
        for (RequestHandle handle : handles)
            handle.cancel(true);
        handleMap.remove(tag);
    }


    AsyncHttpClient getClient(Map<String, Object> heard) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        if (heard != null) {
            Iterator<String> keys = heard.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                httpClient.addHeader(key, String.valueOf(heard.get(key)));
            }
        }
        return httpClient;
    }

    private synchronized void put(Object tag, RequestHandle handle) {
        List<RequestHandle> handles = handleMap.get(tag);
        if (handles != null)
            handles.add(handle);
        else {
            handles = new ArrayList<>();
            handles.add(handle);
            handleMap.put(tag, handles);
        }
    }

    synchronized void remove(Object tag, RequestHandle handle) {
        List<RequestHandle> handles = handleMap.get(tag);
        if (handles != null && handles.size() > 0) {
            int index = handles.indexOf(handle);
            if (index >= 0)
                handles.remove(index);
            if (handles.size() <= 0)
                handleMap.remove(tag);
        }
    }

    @Override
    public ResponseString file(FileRequestEntity entity) {
        AysnResponseString ars = new AysnResponseString();
        RequestParams rp = new RequestParams(entity.params);
        if (entity.object instanceof Map) {
            Map<String, File> fileMap = (Map<String, File>) entity.object;
            if (fileMap != null && fileMap.size() > 0)
                try {
                    for (Map.Entry<String, File> entry : fileMap.entrySet())
                        rp.put(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        } else
            try {
                rp.put(entity.fileKey, entity.object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        ars.setListener(entity.stringListener);
        getClient(entity.heard).post(entity.url, rp, ars);
        return ars.get();
    }

    @Override
    public ResponseObject fileObject(FileRequestEntity entity) {
        AysnResponseObject aro = new AysnResponseObject(entity.cls);
        RequestParams rp = new RequestParams(entity.params);
        if (entity.object instanceof Map) {
            Map<String, File> fileMap = (Map<String, File>) entity.object;
            if (fileMap != null && fileMap.size() > 0)
                try {
                    for (Map.Entry<String, File> entry : fileMap.entrySet())
                        rp.put(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        } else
            try {
                rp.put(entity.fileKey, entity.object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        aro.setListener(entity.objectListener);
        getClient(entity.heard).post(entity.url, rp, aro);
        return aro.get();
    }

    @Override
    public void fileAsyn(FileRequestEntity entity) {
        AysnResponseString ars = new AysnResponseString();
        RequestParams rp = new RequestParams(entity.params);
        if (entity.object instanceof Map) {
            Map<String, File> fileMap = (Map<String, File>) entity.object;
            if (fileMap != null && fileMap.size() > 0)
                try {
                    for (Map.Entry<String, File> entry : fileMap.entrySet())
                        rp.put(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        } else
            try {
                rp.put(entity.fileKey, entity.object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        ars.setListener(entity.stringListener);
        getClient(entity.heard).post(entity.url, rp, ars);
    }


    @Override
    public void fileAsynObject(FileRequestEntity entity) {
        AysnResponseObject aro = new AysnResponseObject(entity.cls);
        RequestParams rp = new RequestParams(entity.params);
        if (entity.object instanceof Map) {
            Map<String, File> fileMap = (Map<String, File>) entity.object;
            if (fileMap != null && fileMap.size() > 0)
                try {
                    for (Map.Entry<String, File> entry : fileMap.entrySet())
                        rp.put(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        } else
            try {
                rp.put(entity.fileKey, entity.object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        aro.setListener(entity.objectListener);
        getClient(entity.heard).post(entity.url, rp, aro);
    }
}
