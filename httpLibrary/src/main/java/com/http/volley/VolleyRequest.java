package com.http.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;


/**
 * com.http.volley
 * 2018/10/18 15:08
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class VolleyRequest extends Request<byte[]> {
    private final static String TAG = "VolleyRequest";
    private String url;
    private Map<String, String> mParams = new HashMap<>();
    private Map<String, String> mHeard = new HashMap<>();
    protected Map<String, String> responseHeard = new HashMap<>();
    protected int code;
    private byte[] raw;
    private String type;
    private Object lock = new Object();
    private boolean loaded = false;
    public boolean success = false;
    private byte[] mResponse;

    public VolleyRequest(String url, int method, Map<String, String> params, Map<String, String> heard) {
        super(method, url, null);
        setShouldCache(false);
        if (heard != null && heard.size() > 0)
            mHeard.putAll(heard);
        if (params != null && params.size() > 0)
            mParams.putAll(params);
        setRetryPolicy(new DefaultRetryPolicy(10 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void setRaw(byte[] raw, String type) {
        this.type = type;
        this.raw = raw;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeard;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (raw != null)
            return raw;
        return super.getBody();
    }

    @Override
    public void deliverError(VolleyError error) {
        Log.i(TAG, "deliverError");
        loaded = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public String getBodyContentType() {
        if (type != null && !type.isEmpty())
            return String.format("application/%s; charset=" + getParamsEncoding(), type);
        return super.getBodyContentType();
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        responseHeard = response.headers;
        code = response.statusCode;
        return Response.success(response.data,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mResponse = response;
        Log.i(TAG, "deliverResponse");
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    byte[] getResponse() {
        if (!loaded)
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return mResponse;
    }

}
