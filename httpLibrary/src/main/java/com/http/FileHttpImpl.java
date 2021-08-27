package com.http;


import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.http.asyn.AsynHttp;
import com.http.interceptor.DefaultSSlInterceptor;
import com.http.interceptor.DnsInterceptor;
import com.http.interceptor.SSLContextInterceptor;
import com.http.listen.ResponseObjectListener;
import com.http.listen.ResponseStringListener;
import com.http.okhttp.OkHttp;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * com.http.volley
 * 2018/11/1 16:57
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class FileHttpImpl implements FileHttp {
    private FileHttp http;
    private final static String TAG = "FileHttpImpl";
    private Context context;

    public FileHttpImpl(Context context) {
        this.context = context;
        SSLContextInterceptor interceptor;
        try {
            interceptor = (SSLContextInterceptor) Class.forName(context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA).metaData.getString("http_ssl_interceptor")).newInstance();
            Log.d("Http.Build", "获取到SSLContext拦截器");
        } catch (Exception e) {
            interceptor = new DefaultSSlInterceptor();
        }
        if (isClass("okhttp3.OkHttpClient")) {
            OkHttp http = new OkHttp();
            try {
                http.setDNS((DnsInterceptor) Class.forName(context.getPackageManager().getApplicationInfo(context.getPackageName(),
                        PackageManager.GET_META_DATA).metaData.getString("http_dns_interceptor")).newInstance());
            } catch (Exception e) {
            }
            http.setSSLInterceptor(interceptor);
            this.http = http;
            Log.i(TAG, "okhttp");
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
    public ResponseString file(FileRequestEntity entity) {
        return http.file(entity);
    }

    @Override
    public void fileAsyn(FileRequestEntity entity) {
        http.fileAsyn(entity);
    }

    @Override
    public ResponseObject fileObject(FileRequestEntity entity) {
        return http.fileObject(entity);
    }

    @Override
    public void fileAsynObject(FileRequestEntity entity) {
        http.fileAsynObject(entity);
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
