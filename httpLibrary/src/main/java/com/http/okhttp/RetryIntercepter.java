package com.http.okhttp;

import android.util.Log;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * com.http.okhttp
 * 2019/1/22 11:28
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class RetryIntercepter implements Interceptor {
    private static final String TAG = "RetryIntercepter";
    private static final String LOG = "重试第%d次";
    public int maxRetry = 0;//最大重试次数
    private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response.isSuccessful() || maxRetry <= 0)
            return response;
        while (true) {
            retryNum++;
            Log.e(TAG, String.format(LOG, retryNum));
            response = chain.proceed(request);
            if (response.isSuccessful() || retryNum > maxRetry)
                break;
        }
        return response;
    }
}
