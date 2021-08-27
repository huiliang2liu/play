package com.http.http;

import com.http.ResponseString;
import com.http.http.request.Request;
import com.http.http.response.Response;
import com.http.listen.ResponseStringListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * com.http.http
 * 2018/10/18 18:29
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class ResponseListenerString implements Runnable {
    private ResponseStringListener listener;
    private Request request;
    private ResponseString responseString;
    private Object lock = new Object();
    private boolean loaded = false;
    private Future future;
    private Http http;
    private Object tag;

    ResponseListenerString(Request request, ResponseStringListener listener, ExecutorService executorService, Http http, Object tag) {
        this.listener = listener;
        this.request = request;
        this.future = executorService.submit(this);
        this.http = http;
        this.tag = tag;
        if (http != null)
            http.add(tag, this.future);
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
            responseString = response(null);
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
            responseString = response(HttpManage.response(request));
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

    public ResponseString response() {
        if (!loaded)
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return responseString;
    }

    private ResponseString response(Response response) {
        ResponseString rs = new ResponseString();
        if (response != null) {
            rs.code = response.getCode();
            rs.response = response.getString();
            rs.heard = response.heard();
        }
        return rs;
    }
}
