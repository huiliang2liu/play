package com.http;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.http.asyn.AsynHttp;
import com.http.interceptor.DefaultSSlInterceptor;
import com.http.interceptor.DnsInterceptor;
import com.http.interceptor.RequestInterceptor;
import com.http.interceptor.SSLContextInterceptor;
import com.http.okhttp.OkHttp;
import com.http.volley.VolleyHttp;

/**
 * com.http
 * 2018/10/18 18:59
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class HttpImpl implements Http {
    private final static String TAG = "HttpImpl";
    private volatile Http http;
    private Context context;

    public HttpImpl(Context context) {
        SSLContextInterceptor interceptor;
        this.context = context;
        try {
            interceptor = (SSLContextInterceptor) Class.forName(context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA).metaData.getString("http_ssl_interceptor")).newInstance();
            Log.d("Http.Build", "获取到SSLContext拦截器");
        } catch (Exception e) {
            interceptor = new DefaultSSlInterceptor();
        }
        if (isClass("okhttp3.OkHttpClient")) {
            OkHttp http = new OkHttp();
            Log.i(TAG, "okhttp");
            try {
                http.setDNS((DnsInterceptor) Class.forName(context.getPackageManager().getApplicationInfo(context.getPackageName(),
                        PackageManager.GET_META_DATA).metaData.getString("http_dns_interceptor")).newInstance());
            } catch (Exception e) {
            }
            http.setSSLInterceptor(interceptor);
            this.http = http;

        } else if (isClass("com.android.volley.RequestQueue")) {
            VolleyHttp http = new VolleyHttp(context);
            http.setSSLInterceptor(interceptor);
            this.http = http;
            Log.i(TAG, "volley");
        } else if (isClass("com.loopj.android.http.AsyncHttpClient")) {
            AsynHttp http = new AsynHttp();
            http.setSSLInterceptor(interceptor);
            this.http = http;
            Log.i(TAG, "asyn");
        } else {
            com.http.http.Http http = new com.http.http.Http();
            http.setInterceptor(interceptor);
            this.http = http;
            Log.i(TAG, "http");
        }

    }


    @Override
    public ResponseString get(RequestEntity entity) {
        return http.get(entity);
    }

    @Override
    public ResponseObject getObject(RequestEntity entity) {
        return http.getObject(entity);
    }

    @Override
    public ResponseString post(RequestEntity entity) {
        return http.post(entity);
    }

    @Override
    public ResponseObject postObject(RequestEntity entity) {
        return http.postObject(entity);
    }

    @Override
    public void getAsyn(RequestEntity entity) {
        http.getAsyn(entity);
    }

    @Override
    public void getObjectAsyn(RequestEntity entity) {
        http.getObjectAsyn(entity);
    }

    @Override
    public void postAsyn(RequestEntity entity) {
        http.postAsyn(entity);
    }

    @Override
    public void postObjectAsyn(RequestEntity entity) {
        http.postObjectAsyn(entity);
    }

    @Override
    public void cancle(Object tag) {
        http.cancle(tag);
    }

    private boolean isClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
