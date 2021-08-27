package com.http.down;

import com.http.http.HttpManage;
import com.http.http.request.DownRequest;
import com.http.http.response.Response;

import java.util.concurrent.ExecutorService;

/**
 * com.http.down
 * 2018/11/1 17:41
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class HttpDown implements Runnable {
    private DownEntity mEntity;
    private Listener mListener;
    private String mUrl;

    public HttpDown(DownEntity entity, Listener listener, String url, ExecutorService service) {
        mEntity = entity;
        mListener = listener;
        mUrl = url;
        service.execute(this);
    }

    @Override
    public void run() {
        DownRequest downRequest = new DownRequest();
        downRequest.setPath(mUrl);
        downRequest.setStartAndEnd(mEntity.start, mEntity.end);
        Response ris = HttpManage.response(downRequest);
        mListener.success(ris.getInputStream(), mEntity);
    }
}
