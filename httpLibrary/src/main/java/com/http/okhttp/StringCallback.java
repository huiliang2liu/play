package com.http.okhttp;

import com.http.ResponseString;
import com.http.listen.ResponseStringListener;

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
class StringCallback implements Callback {
    private Object tag;
    private OkHttp okHttp;
    private ResponseStringListener listener;
    private Object lock = new Object();
    private ResponseString rs;

    public StringCallback(Object tag, OkHttp okHttp, ResponseStringListener listener) {
        this.tag = tag;
        this.okHttp = okHttp;
        this.listener = listener;
        okHttp.runUiThread(new Runnable() {
            @Override
            public void run() {
                if (StringCallback.this.listener != null)
                    StringCallback.this.listener.start();
            }
        });
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (okHttp != null)
            okHttp.remove(tag, call);
        okHttp.runUiThread(new Runnable() {
            @Override
            public void run() {
                if (listener != null)
                    listener.failure();
            }
        });
        rs = new ResponseString();
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (okHttp != null)
            okHttp.remove(tag, call);
        rs = new ResponseString();
        response2string(response, rs);
        okHttp.runUiThread(new Runnable() {
            @Override
            public void run() {
                if (listener != null)
                    listener.success(rs);
            }
        });
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public ResponseString get() {
        if (rs == null) {
            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;
    }

    private void response2string(Response response, ResponseString rs) {
        if (!response.isSuccessful())
            return;
        rs.code = response.code();
        try {
            rs.response = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int size = response.headers().size();
        if (size <= 0)
            return;
        Map<String, String> heard = new HashMap<>();
        for (int i = 0; i < size; i++) {
            heard.put(response.headers().name(i), response.headers().value(i));
        }
        rs.heard = heard;
    }
}
