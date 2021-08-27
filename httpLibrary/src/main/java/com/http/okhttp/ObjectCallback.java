package com.http.okhttp;

import com.http.ResponseObject;
import com.http.listen.ResponseObjectListener;
import com.json.Json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * com.http.okhttp
 * 2018/11/2 18:30
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class ObjectCallback implements Callback {
    private Object tag;
    private OkHttp okHttp;
    private ResponseObjectListener listener;
    private Object lock = new Object();
    private ResponseObject ro;
    private Class cls;

    public ObjectCallback(Object tag, OkHttp okHttp, ResponseObjectListener listener, Class cls) {
        this.tag = tag;
        this.okHttp = okHttp;
        this.listener = listener;
        this.cls = cls;
        okHttp.runUiThread(new Runnable() {
            @Override
            public void run() {
                if (ObjectCallback.this.listener != null)
                    ObjectCallback.this.listener.start();
            }
        });
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (okHttp != null)
            okHttp.remove(tag, call);
        ro = new ResponseObject();
        okHttp.runUiThread(new Runnable() {
            @Override
            public void run() {
                if (listener != null)
                    listener.failure();
            }
        });
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (okHttp != null)
            okHttp.remove(tag, call);
        ro = new ResponseObject();
        response2object(response);
        okHttp.runUiThread(new Runnable() {
            @Override
            public void run() {
                if (listener != null)
                    listener.success(ro);
            }
        });
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public ResponseObject get() {
        if (ro == null) {
            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return ro;
    }

    private void response2object(Response response) {
        if (!response.isSuccessful())
            return;
        ro.code = response.code();
        ro.response = Json.parasJson(response.body().byteStream(), cls);
        int size = response.headers().size();
        if (size <= 0)
            return;
        Map<String, String> heard = new HashMap<>();
        for (int i = 0; i < size; i++) {
            heard.put(response.headers().name(i), response.headers().value(i));
        }
        ro.heard = heard;
    }
}
