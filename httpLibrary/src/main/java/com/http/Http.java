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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * com.http
 * 2018/10/17 15:35
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public interface Http {
    ResponseString get(RequestEntity entity);

    ResponseObject getObject(RequestEntity entity);

    ResponseString post(RequestEntity entity);

    ResponseObject postObject(RequestEntity entity);

    void getAsyn(RequestEntity entity);

    void getObjectAsyn(RequestEntity entity);

    void postAsyn(RequestEntity entity);

    void postObjectAsyn(RequestEntity entity);

    void cancle(Object tag);


    class Build {
        private Context context;

        public Build context(Context context) {
            this.context = context;
            return this;
        }

        public Http build() {
            if (context == null)
                throw new NullPointerException("context is null");
//            if(true)
//                return new HttpImpl(context);
            return (Http) Proxy.newProxyInstance(Http.class.getClassLoader(), new Class[]{Http.class}, new InvocationHandler() {
                Http http = new HttpImpl(context);
                private RequestInterceptor interceptor;

                {
                    try {
                        interceptor = (RequestInterceptor) Class.forName(context.getPackageManager().getApplicationInfo(context.getPackageName(),
                                PackageManager.GET_META_DATA).metaData.getString("http_intercept")).newInstance();
                        Log.d("Http.Build", "获取到拦截器");
                    } catch (Exception e) {
                        interceptor = new RequestInterceptor() {
                            @Override
                            public RequestEntity intercept(RequestEntity entity, Context context) {
                                return entity;
                            }
                        };
                    }
                }

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    String name = method.getName();
                    if ("cancle".equals(name))
                        return method.invoke(http, args);
                    if (args == null || args.length <= 0) {
                        throw new RuntimeException("you RequestEntity is null");
                    }
                    RequestEntity o = (RequestEntity) args[0];
                    o = interceptor.intercept(o, context);
                    if (o == null)
                        throw new RuntimeException("you RequestEntity is null");
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

    class RequestEntity {
        public String url;
        public Map<String, Object> heard;
        public Map<String, Object> params;
        public byte[] raw;
        public String type = "json";
        public Class cls;
        public ResponseObjectListener objectListener;
        public ResponseStringListener stringListener;
        public Object tag = toString();

        public RequestEntity url(String url) {
            this.url = url;
            return this;
        }

        public RequestEntity tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public RequestEntity heard(Map<String, Object> heard) {
            this.heard = heard;
            return this;
        }

        public RequestEntity params(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public RequestEntity raw(byte[] raw) {
            this.raw = raw;
            return this;
        }

        public RequestEntity type(String type) {
            this.type = type;
            return this;
        }

        public RequestEntity cls(Class cls) {
            this.cls = cls;
            return this;
        }

        public RequestEntity objectListener(ResponseObjectListener objectListener) {
            this.objectListener = objectListener;
            return this;
        }

        public RequestEntity stringListener(ResponseStringListener stringListener) {
            this.stringListener = stringListener;
            return this;
        }
    }
}
