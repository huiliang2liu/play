package com.http.volley;

import android.util.Log;

import com.android.volley.VolleyError;
import com.http.ResponseString;
import com.http.listen.ResponseStringListener;

import java.util.Map;

/**
 * com.http.volley
 * 2018/10/18 15:36
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class VolleyRequestString extends VolleyRequest {
    private final static String TAG = "VolleyRequestString";
    private ResponseStringListener listener;

    public VolleyRequestString(String url, int method, Map<String, String> params, Map<String, String> heard) {
        super(url, method, params, heard);
    }

    public void setListener(ResponseStringListener listener) {
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
        else
            Log.i(TAG, "沒有回调");
    }

    public ResponseString get() {
        return response(getResponse());
    }

    private ResponseString response(byte[] response) {
        ResponseString responseString = new ResponseString();
        if (response == null || response.length <= 0)
            return responseString;
        responseString.code = code;
        responseString.heard = responseHeard;
        responseString.response = new String(response);
        return responseString;
    }
}
