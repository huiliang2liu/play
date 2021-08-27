package com.http.volley;

import com.android.volley.VolleyError;
import com.http.ResponseObject;
import com.http.listen.ResponseObjectListener;
import com.json.Json;

import java.util.Map;

/**
 * com.http.volley
 * 2018/10/18 15:36
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class VolleyRequestObject<T> extends VolleyRequest {
    private ResponseObjectListener listener;
    private Class<T> mCls;

    public VolleyRequestObject(String url, int method, Map<String, String> params, Map<String, String> heard, Class<T> cls) {
        super(url, method, params, heard);
        mCls = cls;
    }

    public void setListener(ResponseObjectListener listener) {
        if (listener != null)
            listener.start();
        this.listener = listener;
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        if (listener != null)
            listener.failure();
    }

    @Override
    protected void deliverResponse(byte[] response) {
        super.deliverResponse(response);
        if (listener != null)
            listener.success(response(response));
    }

    public ResponseObject get() {
        return response(getResponse());
    }

    private ResponseObject response(byte[] response) {
        ResponseObject responseString = new ResponseObject();
        if (response == null || response.length <= 0)
            return responseString;
        responseString.code = code;
        responseString.heard = responseHeard;
        responseString.response = Json.parasJson(response, mCls);
        return responseString;
    }
}
