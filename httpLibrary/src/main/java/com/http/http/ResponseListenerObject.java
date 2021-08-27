package com.http.http;

import com.http.ResponseObject;
import com.http.http.request.Request;
import com.http.http.response.Response;
import com.http.listen.ResponseObjectListener;
import com.json.Json;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * com.http.http
 * 2018/10/18 18:29
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class ResponseListenerObject implements Runnable {
    private ResponseObjectListener listener;
    private Request request;
    private ResponseObject responseString;
    private Object lock = new Object();
    private boolean loaded = false;
    private Class mCls;
    private Future future;
    private Http http;
    private Object tag;

    ResponseListenerObject(Request request, ResponseObjectListener listener, Class cls, ExecutorService executorService, Http http, Object tag) {
        this.listener = listener;
        this.request = request;
        mCls = cls;
        this.future = executorService.submit(this);
        this.http = http;
        this.tag = tag;
        if (http != null)
            http.add(tag, future);
    }

    @Override
    public void run() {
        http.runUiThread(new Runnable() {
            @Override
            public void run() {
                if (listener != null)
                    listener.start();
            }
        });
        if (request == null) {
            if (http != null)
                http.remove(tag, future);
            responseString = response(null, mCls);
            http.runUiThread(new Runnable() {
                @Override
                public void run() {
                    if (listener != null)
                        listener.failure();
                }
            });
            loaded = true;
            synchronized (lock) {
                lock.notifyAll();
            }
        } else {
            responseString = response(HttpManage.response(request), mCls);
            if (http != null)
                http.remove(tag, future);
            http.runUiThread(new Runnable() {
                @Override
                public void run() {
                    if (listener != null)
                        listener.success(responseString);
                }
            });
            loaded = true;
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }

    public ResponseObject response() {
        if (!loaded)
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return responseString;
    }

    private ResponseObject response(Response response, Class cls) {
        ResponseObject ro = new ResponseObject();
        if (response != null) {
            ro.code = response.getCode();
            ro.response = Json.parasJson(response.getInputStream(), cls);
            ro.heard = response.heard();
        }
        return ro;
    }
}
