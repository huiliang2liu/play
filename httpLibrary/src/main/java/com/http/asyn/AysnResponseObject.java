package com.http.asyn;

import com.http.ResponseObject;
import com.http.listen.ResponseObjectListener;
import com.json.Json;
import com.loopj.android.http.RequestHandle;

import cz.msebera.android.httpclient.Header;

/**
 * com.http.asyn
 * 2018/10/18 17:02
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class AysnResponseObject extends AysnResponse {
    private ResponseObjectListener listener;
    private Class mCls;
    private Object tag;
    private AsynHttp http;
    private RequestHandle handle;

    AysnResponseObject(Class cls, Object tag, AsynHttp http) {
        mCls = cls;
        this.tag = tag;
        this.http = http;
    }

    AysnResponseObject(Class cls) {
        mCls = cls;
    }

    void setHandle(RequestHandle handle) {
        this.handle = handle;
    }

    public void setListener(ResponseObjectListener listener) {
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
            listener.success(get(response, mCls));
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
        if (http != null)
            http.remove(tag, handle);
        super.onFailure(statusCode, headers, errorResponse, e);
        if (listener != null)
            listener.failure();
    }

    public ResponseObject get() {
        return get(response(), mCls);
    }

    private ResponseObject get(byte[] buff, Class cls) {
        ResponseObject rs = new ResponseObject();
        if (buff != null) {
            rs.code = code;
            rs.heard = heards;
            rs.response = Json.parasJson(buff, cls);
        }
        return rs;
    }
}
