package com.http.listen;

public interface ResponseListener {
    /**
     * 开始加载
     */
    void start();

    /**
     * 加载失败
     */
    void failure();
}
