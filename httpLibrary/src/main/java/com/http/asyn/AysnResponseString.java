package com.http.asyn;

import com.http.ResponseString;
import com.http.listen.ResponseStringListener;
import com.loopj.android.http.RequestHandle;

import cz.msebera.android.httpclient.Header;

/**
 * com.http.asyn
 * 2018/10/18 17:02
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class AysnResponseString extends AysnResponse {
    private ResponseStringListener listener;
    private Object tag;
    private AsynHttp http;
    private RequestHandle handle;

    AysnResponseString(Object tag, AsynHttp http) {
        this.tag = tag;
        this.http = http;
    }

    AysnResponseString() {
    }

    void setHandle(RequestHandle handle) {
        this.handle = handle;
    }

    public void setListener(ResponseStringListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (listener != null)
            listener.start();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
        if (http != null)
            http.remove(tag, handle);
        super.onSuccess(statusCode, headers, response);
        if (listener != null)
            listener.success(get(response));
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
        if (http != null)
            http.remove(tag, handle);
        super.onFailure(statusCode, headers, errorResponse, e);
        if (listener != null)
            listener.failure();
    }

    public ResponseString get() {
        return get(response());
    }

    private ResponseString get(byte[] buff) {
        ResponseString rs = new ResponseString();
        if (buff != null) {
            rs.code = code;
            rs.heard = heards;
            rs.response = new String(buff);
        }
        return rs;
    }
}
