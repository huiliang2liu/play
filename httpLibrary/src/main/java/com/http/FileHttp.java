package com.http;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.http.interceptor.RequestInterceptor;
import com.http.listen.ResponseObjectListener;
import com.http.listen.ResponseStringListener;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * com.http
 * 2018/10/18 14:47
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public interface FileHttp {
    ResponseString file(FileRequestEntity entity);

    ResponseObject fileObject(FileRequestEntity entity);

    void fileAsyn(FileRequestEntity entity);

    void fileAsynObject(FileRequestEntity entity);

    class FileRequestEntity {
        public String url;
        public Map<String, Object> heard;
        public Map<String, Object> params;
        public Class cls;
        public String fileKey;
        public Object object;
        public ResponseObjectListener objectListener;
        public ResponseStringListener stringListener;
        public Object tag = toString();
    }

    class Build {
        private Context context;

        public FileHttp build(final Context context) {
            this.context = context;
            return (FileHttp) Proxy.newProxyInstance(FileHttp.class.getClassLoader(), new Class[]{FileHttp.class}, new InvocationHandler() {
                private RequestInterceptor interceptor;

                {
                    try {
                        interceptor = (RequestInterceptor) Class.forName(context.getPackageManager().getApplicationInfo(context.getPackageName(),
                                PackageManager.GET_META_DATA).metaData.getString("http_intercept")).newInstance();
                        Log.d("Http.Build", "获取到拦截器");
                    } catch (Exception e) {
                        interceptor = new RequestInterceptor() {
                            @Override
                            public Http.RequestEntity intercept(Http.RequestEntity entity, Context context) {
                                return entity;
                            }
                        };
                    }
                }

                FileHttp http = new FileHttpImpl(context);

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (args == null || args.length <= 0) {
                        throw new RuntimeException("you RequestEntity is null");
                    }
                    Http.RequestEntity o = (Http.RequestEntity) args[0];
                    if (o == null)
                        throw new RuntimeException("you RequestEntity is null");
                    o = interceptor.intercept(o, context);
                    if (o == null)
                        throw new RuntimeException("you RequestEntity is null");
                    String name = method.getName();
                    boolean failure = false;
                    if (!isNetConnected())
                        failure = true;
                    if (o.url == null || o.url.isEmpty())
                        failure = true;
                    boolean isObject = name.contains("Object");
                    boolean isAsyn = name.contains("Asyn");
                    if (isObject && o.cls == null)
                        failure = true;
                    if (failure) {
                        if (isAsyn) {
                            if (isObject) {
                                if (o.objectListener != null)
                                    o.objectListener.failure();
                            } else {
                                if (o.stringListener != null)
                                    o.stringListener.failure();
                            }
                        } else {
                            if (isObject)
                                return new ResponseObject();
                            return new ResponseString();
                        }
                        return null;
                    }
                    if (isAsyn) {
                        if (isObject) {
                            if (o.objectListener == null)
                                return null;
                        } else {
                            if (o.stringListener == null)
                                return null;
                        }
                    }
                    return method.invoke(http, o);
                }
            });
        }

        /**
         * 判断网络是否连接
         *
         * @return true/false
         */
        public boolean isNetConnected() {
            if (android.os.Build.VERSION.SDK_INT >= 23 && context.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
                return false;
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager
                    .getActiveNetworkInfo();
            if (info == null || !info.isConnected())
                return false;
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }

            return false;
        }
    }
}
