package com.http.down;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.ByteArrayInputStream;

import cz.msebera.android.httpclient.Header;

public class AsynDown extends AsyncHttpResponseHandler {
    private DownEntity downEntity;
    private Listener listener;

    public AsynDown(DownEntity downEntity, Listener listener, String url) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Range", "bytes=" + downEntity.start + "-" + downEntity.end);
        this.downEntity = downEntity;
        this.listener = listener;
        httpClient.get(url, this);
    }

    @Override
    public void onStart() {
        // called before request is started
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
        // called when response HTTP status is "200 OK"
        listener.success(new ByteArrayInputStream(response), downEntity);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
        // called when response HTTP status is "4XX" (eg. 401, 403, 404)

    }

    @Override
    public void onRetry(int retryNo) {
        // called when request is retried
    }
}
