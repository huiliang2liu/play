package com.http.down;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class VolleyDown extends Request<byte[]> {
    private RequestQueue mQueue;
    private Map<String,String> mHeard;
    private Listener listener;
    private DownEntity entity;

    VolleyDown(DownEntity downEntity, Listener listener, String url, Context context) {
        super(Method.GET,url,null);
        mQueue = Volley.newRequestQueue(context);
        this.listener=listener;
        entity=downEntity;
        mHeard.put("Range", "bytes=" + downEntity.start + "-" + downEntity.end);
        mQueue.add(this);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeard;
    }




    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        if(listener!=null)
            listener.success(new ByteArrayInputStream(response),entity);
    }

}
