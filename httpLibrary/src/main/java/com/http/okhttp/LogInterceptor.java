package com.http.okhttp;

import android.util.Log;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * com.http.okhttp
 * 2018/12/4 11:47
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class LogInterceptor implements Interceptor {
    private final static String TAG="LogInterceptor";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        Request.Builder builder=request.newBuilder();
        Log.e(TAG,request.url().toString());
        return chain.proceed(builder.build());
    }
}
