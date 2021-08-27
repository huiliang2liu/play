package com.http.interceptor;

import android.content.Context;

import com.http.Http;

public interface RequestInterceptor {
    Http.RequestEntity intercept(Http.RequestEntity entity, Context context);
}
