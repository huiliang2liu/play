package com.http.service;

import android.content.Context;

import java.util.Map;

public interface HttpServiceResponse {
    void init(Context context,String hostName,int port);
    /**
     * get请求
     * @param url
     * @param headers
     * @param params
     * @param session
     * @return
     */
    Response get(String url, Map<String,String> headers, Map<String,String> params, HttpService.IHTTPSession session);

    /**
     * post请求
     * @param url
     * @param headers
     * @param params
     * @param session
     * @return
     */
    Response post(String url, Map<String,String> headers, Map<String,String> params, HttpService.IHTTPSession session);

    /**
     * 其他请求
     * @param method
     * @param url
     * @param headers
     * @param params
     * @param session
     * @return
     */
    Response other(String method ,String url, Map<String,String> headers, Map<String,String> params, HttpService.IHTTPSession session);
}
