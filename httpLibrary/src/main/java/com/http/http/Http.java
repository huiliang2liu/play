package com.http.http;

import android.os.Handler;
import android.os.Looper;

import com.http.AbsHttp;
import com.http.FileHttp;
import com.http.ResponseObject;
import com.http.ResponseString;
import com.http.http.request.FileUploadRequest;
import com.http.http.request.FlowRequest;
import com.http.http.request.Request;
import com.http.http.util.Method;
import com.http.interceptor.SSLContextInterceptor;
import com.http.listen.ResponseObjectListener;
import com.http.listen.ResponseStringListener;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * com.http.http
 * 2018/10/18 17:58
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class Http extends AbsHttp implements FileHttp {
    private Handler handler = new Handler(Looper.getMainLooper());
    ExecutorService executorService;
    Map<Object, List<Future>> futureMap = new HashMap<>();
    SSLContextInterceptor interceptor;

    {
        int threadNum = Runtime.getRuntime().availableProcessors();
        threadNum = threadNum << 1;
        executorService = new ThreadPoolExecutor(threadNum, threadNum << 1, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    }

    public void setInterceptor(SSLContextInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public void runUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    @Override
    public ResponseString get(RequestEntity entity) {
        ResponseListenerString string = new ResponseListenerString(getRequest(entity.url, entity.heard, entity.params), null, executorService, this, entity.tag);
        return string.response();
    }

    @Override
    public ResponseObject getObject(RequestEntity entity) {
        ResponseListenerObject object = new ResponseListenerObject(getRequest(entity.url, entity.heard, entity.params), null, entity.cls, executorService, this, entity.tag);
        return object.response();
    }

    @Override
    public ResponseString post(RequestEntity entity) {
        ResponseListenerString string = new ResponseListenerString(postRequest(entity.url, entity.heard, entity.params), null, executorService, this, entity.tag);
        return string.response();
    }

    @Override
    public ResponseObject postObject(RequestEntity entity) {
        ResponseListenerObject object = new ResponseListenerObject(postRequest(entity.url, entity.heard, entity.params), null, entity.cls, executorService, this, entity.tag);
        return object.response();
    }

    @Override
    public void getAsyn(RequestEntity entity) {
        new ResponseListenerString(getRequest(entity.url, entity.heard, entity.params), entity.stringListener, executorService, this, entity.tag);
    }

    @Override
    public void getObjectAsyn(RequestEntity entity) {
        new ResponseListenerObject(getRequest(entity.url, entity.heard, entity.params), entity.objectListener, entity.cls, executorService, this, entity.tag);
    }

    @Override
    public void postAsyn(RequestEntity entity) {
        new ResponseListenerString(postRequest(entity.url, entity.heard, entity.params), entity.stringListener, executorService, this, entity.tag);
    }

    @Override
    public void postObjectAsyn(RequestEntity entity) {
        new ResponseListenerObject(postRequest(entity.url, entity.heard, entity.params), entity.objectListener, entity.cls, executorService, this, entity.tag);
    }

    @Override
    public synchronized void cancle(Object tag) {
        List<Future> futures = futureMap.get(tag);
        if (futures == null || futures.size() <= 0)
            return;
        for (Future future : futures)
            if (!future.isDone())
                future.cancel(true);
    }

    synchronized void add(Object tag, Future future) {
        List<Future> futures = futureMap.get(tag);
        if (futures != null) {
            futures.add(future);
            return;
        }
        futures = new ArrayList<>();
        futures.add(future);
        futureMap.put(tag, futures);
    }

    synchronized void remove(Object tag, Future future) {
        List<Future> futures = futureMap.get(tag);
        if (futures == null || futures.size() <= 0)
            return;
        int index = futures.indexOf(future);
        if (index >= 0)
            futures.remove(index);
        if (futures.size() <= 0)
            futureMap.remove(tag);
    }

    @Override
    public ResponseString file(FileRequestEntity entity) {
        ResponseListenerString rls = null;
        if (entity.object instanceof File)
            rls = new ResponseListenerString(postRequest(entity.url, entity.heard, entity.params, entity.fileKey, (File) entity.object), null, executorService, this, entity.url);
        else if (entity.object instanceof List) {
            List<File> files = (List<File>) entity.object;
            for (File file : files)
                rls = new ResponseListenerString(postRequest(entity.url, entity.heard, entity.params, entity.fileKey, file), null, executorService, this, entity.url);
        } else {
            Map<String, File> map = (Map<String, File>) entity.object;
            for (Map.Entry<String, File> entry : map.entrySet())
                rls = new ResponseListenerString(postRequest(entity.url, entity.heard, entity.params, entry.getKey(), entry.getValue()), null, executorService, this, entity.url);
        }
        return rls.response();
    }

    @Override
    public ResponseObject fileObject(FileRequestEntity entity) {
        ResponseListenerObject responseListenerObject = null;
        if (entity.object instanceof File) {
            responseListenerObject = new ResponseListenerObject(postRequest(entity.url, entity.heard, entity.params, entity.fileKey, (File) entity.object), null, entity.cls, executorService, this, entity.url);
        } else if (entity.object instanceof List) {
            List<File> files = (List<File>) entity.object;
            for (File file : files)
                responseListenerObject = new ResponseListenerObject(postRequest(entity.url, entity.heard, entity.params, entity.fileKey, file), null, entity.cls, executorService, this, entity.url);
        } else {
            Map<String, File> map = (Map<String, File>) entity.object;
            for (Map.Entry<String, File> entry : map.entrySet())
                responseListenerObject = new ResponseListenerObject(postRequest(entity.url, entity.heard, entity.params, entry.getKey(), entry.getValue()), null, entity.cls, executorService, this, entity.url);
        }
        return responseListenerObject.response();
    }

    @Override
    public void fileAsyn(FileRequestEntity entity) {
        if (entity.object instanceof File)
            new ResponseListenerString(postRequest(entity.url, entity.heard, entity.params, entity.fileKey, (File) entity.object), entity.stringListener, executorService, this, entity.url);
        else if (entity.object instanceof List) {
            List<File> files = (List<File>) entity.object;
            for (File file : files)
                new ResponseListenerString(postRequest(entity.url, entity.heard, entity.params, entity.fileKey, file), entity.stringListener, executorService, this, entity.url);
        } else {
            Map<String, File> map = (Map<String, File>) entity.object;
            for (Map.Entry<String, File> entry : map.entrySet())
                new ResponseListenerString(postRequest(entity.url, entity.heard, entity.params, entry.getKey(), entry.getValue()), entity.stringListener, executorService, this, entity.url);
        }
    }

    @Override
    public void fileAsynObject(FileRequestEntity entity) {
        if (entity.object instanceof File) {
            new ResponseListenerObject(postRequest(entity.url, entity.heard, entity.params, entity.fileKey, (File) entity.object), entity.objectListener, entity.cls, executorService, this, entity.url);
        } else if (entity.object instanceof List) {
            List<File> files = (List<File>) entity.object;
            for (File file : files)
                new ResponseListenerObject(postRequest(entity.url, entity.heard, entity.params, entity.fileKey, file), entity.objectListener, entity.cls, executorService, this, entity.url);
        } else {
            Map<String, File> map = (Map<String, File>) entity.object;
            for (Map.Entry<String, File> entry : map.entrySet())
                new ResponseListenerObject(postRequest(entity.url, entity.heard, entity.params, entry.getKey(), entry.getValue()), entity.objectListener, entity.cls, executorService, this, entity.url);
        }

    }

    private Request getRequest(String url, Map<String, Object> heard, Map<String, Object> params) {
        Request request = new Request();
        request.addHead(heard);
        request.addParams(new HashMap<String, Object>(params));
        request.setMethod(Method.GET);
        request.setPath(url);
        if (interceptor != null)
            request.setFactory(interceptor.interceptor().getSocketFactory());
        return request;
    }

    private Request postRequest(String url, Map<String, Object> heard, Map<String, Object> params) {
        Request request = new Request();
        request.addHead(heard);
        request.addParams(new HashMap<String, Object>(params));
        request.setMethod(Method.POST);
        request.setPath(url);
        if (interceptor != null)
            request.setFactory(interceptor.interceptor().getSocketFactory());
        return request;
    }

    private Request postRequest(String url, Map<String, Object> heard, Map<String, Object> params, String fileKey, File file) {
        FileUploadRequest request = new FileUploadRequest(file);
        request.addHead(heard);
        request.addParams(new HashMap<String, Object>(params));
        request.setMethod(Method.POST);
        request.setFileKey(fileKey);
        request.setPath(url);
        if (interceptor != null)
            request.setFactory(interceptor.interceptor().getSocketFactory());
        return request;
    }

    private Request rawRequest(String url, Map<String, Object> heard, String raw, String type) {
        Request request = new FlowRequest(raw);
        request.addHead(heard);
        request.setContentType(String.format("application/%s", type));
        request.setMethod(Method.POST);
        request.setPath(url);
        if (interceptor != null)
            request.setFactory(interceptor.interceptor().getSocketFactory());
        return request;
    }
}
