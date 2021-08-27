package com.http.asyn;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


/**
 * com.http.asyn
 * 2018/10/18 16:55
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class AysnDown extends AsyncHttpResponseHandler {
    private boolean loaded = false;
    private boolean buff = false;
    private Object lock = new Object();
    protected int code;
    protected Map<String, String> heards;
    private File saveFile;

    public AysnDown(File saveFile) {
        this.saveFile = saveFile;
        File parent = saveFile.getParentFile();
        if (!parent.exists())
            parent.mkdirs();
    }

    @Override
    public void onStart() {
        // called before request is started
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
        // called when response HTTP status is "200 OK"
        loaded = true;
        if (response != null && statusCode == 200) {
            try {
                FileOutputStream fos = new FileOutputStream(saveFile);
                fos.write(response);
                fos.flush();
                fos.close();
                buff = true;
            } catch (Exception e) {
            }
        }
        code = statusCode;
        if (headers != null || headers.length > 0) {
            heards = new HashMap<>();
            for (Header header : headers) {
                heards.put(header.getName(), header.getValue());
            }
        }
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
        loaded = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public void onRetry(int retryNo) {
        // called when request is retried
    }

    protected boolean response() {
        if (loaded)
            return buff;
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buff;
    }
}
