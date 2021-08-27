package com.http.okhttp;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.http.AbsHttp;
import com.http.FileHttp;
import com.http.Http;
import com.http.ResponseObject;
import com.http.ResponseString;
import com.http.interceptor.DnsInterceptor;
import com.http.interceptor.SSLContextInterceptor;
import com.http.listen.ResponseObjectListener;
import com.http.listen.ResponseStringListener;
import com.json.Json;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dns;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * com.http.okhttp
 * 2018/10/17 17:46
 * instructions：okhttp网络请求
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class OkHttp extends AbsHttp implements FileHttp {
    private final static String TAG = "OkHttp";
    private static final long cacheSize = 1024 * 1024 * 20;// 缓存文件最大限制大小20M
    private String cacheDirectory = Environment.getExternalStorageDirectory() + "/okttpcaches"; // 设置缓存文件路径
    private Cache cache = new Cache(new File(cacheDirectory), cacheSize);  //
    private OkHttpClient okHttp;
    private Map<Object, List<Call>> callMap = new HashMap<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private DnsInterceptor dnsInterceptor;

    {
        //如果无法生存缓存文件目录，检测权限使用已经加上，检测手机是否把文件读写权限禁止了
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(8, TimeUnit.SECONDS); // 设置连接超时时间
        builder.writeTimeout(8, TimeUnit.SECONDS);// 设置写入超时时间
        builder.readTimeout(8, TimeUnit.SECONDS);// 设置读取数据超时时间
        builder.retryOnConnectionFailure(true);// 设置进行连接失败重试
//        builder.addInterceptor(new LogInterceptor());
//        builder.cache(cache);// 设置缓存
        builder.dns(new Dns() {
            @NotNull
            @Override
            public List<InetAddress> lookup(@NotNull String s) throws UnknownHostException {
                Log.e(TAG, "dns 拦截");
                if (dnsInterceptor != null) {
                    List<InetAddress> list = dnsInterceptor.interceptor(s);
                    if (list != null && list.size() > 0)
                        return list;
                }
                return Dns.SYSTEM.lookup(s);
            }
        });
        okHttp = builder.build();
    }

    public void setSSLInterceptor(SSLContextInterceptor interceptor) {
        if (interceptor != null) {
            okHttp = okHttp.newBuilder().sslSocketFactory(interceptor.interceptor().getSocketFactory(), interceptor.trustManager()).build();
        }

    }

    public void setDNS(final DnsInterceptor interceptor) {
        dnsInterceptor = interceptor;
    }


    @Override
    public ResponseString get(Http.RequestEntity entity) {
        Request request = builder(url(entity.url, entity.params), entity.heard).build();
        StringCallback callback = new StringCallback(entity.tag, this, null);
        Call call = okHttp.newCall(request);
        put(entity.tag, call);
        call.enqueue(callback);
        return callback.get();
    }

    @Override
    public ResponseObject getObject(Http.RequestEntity entity) {
        Request request = builder(url(entity.url, entity.params), entity.heard).build();
        ObjectCallback callback = new ObjectCallback(entity.tag, this, null, entity.cls);
        Call call = okHttp.newCall(request);
        put(entity.tag, call);
        call.enqueue(callback);
        return callback.get();
    }

    @Override
    public ResponseString post(Http.RequestEntity entity) {
        Request request = null;
        if (entity.raw != null)
            request = builder(entity.url, entity.heard).post(requestBody(entity.raw, entity.type)).build();
        else
            request = builder(entity.url, entity.heard).post(requestBody(entity.params)).build();
        StringCallback callback = new StringCallback(entity.tag, this, null);
        Call call = okHttp.newCall(request);
        put(entity.tag, call);
        call.enqueue(callback);
        return callback.get();
    }

    @Override
    public ResponseObject postObject(Http.RequestEntity entity) {
        Request request = null;
        if (entity.raw != null)
            request = builder(entity.url, entity.heard).post(requestBody(entity.raw, entity.type)).build();
        else
            request = builder(entity.url, entity.heard).post(requestBody(entity.params)).build();
        ObjectCallback callback = new ObjectCallback(entity.tag, this, null, entity.cls);
        Call call = okHttp.newCall(request);
        put(entity.tag, call);
        call.enqueue(callback);
        return callback.get();
    }

    @Override
    public void getAsyn(Http.RequestEntity entity) {
        Request request = builder(url(entity.url, entity.params), entity.heard).build();
        StringCallback callback = new StringCallback(entity.tag, this, entity.stringListener);
        Call call = okHttp.newCall(request);
        put(entity.tag, call);
        call.enqueue(callback);
    }

    @Override
    public void getObjectAsyn(Http.RequestEntity entity) {
        Request request = builder(url(entity.url, entity.params), entity.heard).build();
        ObjectCallback callback = new ObjectCallback(entity.tag, this, entity.objectListener, entity.cls);
        Call call = okHttp.newCall(request);
        put(entity.tag, call);
        call.enqueue(callback);
    }

    @Override
    public void postAsyn(Http.RequestEntity entity) {
        Request request = null;
        if (entity.raw != null)
            request = builder(entity.url, entity.heard).post(requestBody(entity.raw, entity.type)).build();
        else
            request = builder(entity.url, entity.heard).post(requestBody(entity.params)).build();
        StringCallback callback = new StringCallback(entity.tag, this, entity.stringListener);
        Call call = okHttp.newCall(request);
        put(entity.tag, call);
        call.enqueue(callback);
    }

    @Override
    public void postObjectAsyn(Http.RequestEntity entity) {
        Request request = null;
        if (entity.raw != null)
            request = builder(entity.url, entity.heard).post(requestBody(entity.raw, entity.type)).build();
        else
            request = builder(entity.url, entity.heard).post(requestBody(entity.params)).build();
        ObjectCallback callback = new ObjectCallback(entity.tag, this, entity.objectListener, entity.cls);
        Call call = okHttp.newCall(request);
        put(entity.tag, call);
        call.enqueue(callback);
    }

    synchronized void put(Object tag, Call call) {
        List<Call> calls = callMap.get(tag);
        if (calls != null) {
            calls.add(call);
            return;
        }
        calls = new ArrayList<>();
        calls.add(call);
        callMap.put(tag, calls);
    }

    synchronized void remove(Object tag, Call call) {
        List<Call> calls = callMap.get(tag);
        if (calls == null || calls.size() <= 0)
            return;
        int index = calls.indexOf(call);
        if (index >= 0)
            calls.remove(index);
        if (calls.size() <= 0)
            callMap.remove(tag);
    }

    @Override
    public synchronized void cancle(Object tag) {
        List<Call> calls = callMap.get(tag);
        if (calls == null || calls.size() <= 0)
            return;
        for (Call call : calls) {
            Log.i(TAG, "取消请求");
            call.cancel();
        }

        callMap.remove(tag);
    }

    @Override
    public ResponseObject fileObject(FileRequestEntity entity) {
        Request.Builder builder = builder(entity.url, entity.heard);
        if (entity.object instanceof Map)
            builder.post(requestBody(entity.params, (Map<String, File>) entity.object));
        else if (entity.object instanceof List)
            builder.post(requestBody(entity.params, entity.fileKey, (List<File>) entity.object));
        else
            builder.post(requestBody(entity.params, entity.fileKey, (File) entity.object));
        ObjectCallback callback = new ObjectCallback(null, null, null, entity.cls);
        Call call = okHttp.newCall(builder.build());
        call.enqueue(callback);
        return callback.get();
    }

    @Override
    public ResponseString file(FileRequestEntity entity) {
        Request.Builder builder = builder(entity.url, entity.heard);
        if (entity.object instanceof Map)
            builder.post(requestBody(entity.params, (Map<String, File>) entity.object));
        else if (entity.object instanceof List)
            builder.post(requestBody(entity.params, entity.fileKey, (List<File>) entity.object));
        else
            builder.post(requestBody(entity.params, entity.fileKey, (File) entity.object));
        StringCallback callback = new StringCallback(null, null, null);
        Call call = okHttp.newCall(builder.build());
        call.enqueue(callback);
        return callback.get();
    }

    @Override
    public void fileAsyn(FileRequestEntity entity) {
        Request.Builder builder = builder(entity.url, entity.heard);
        if (entity.object instanceof Map)
            builder.post(requestBody(entity.params, (Map<String, File>) entity.object));
        else if (entity.object instanceof List)
            builder.post(requestBody(entity.params, entity.fileKey, (List<File>) entity.object));
        else
            builder.post(requestBody(entity.params, entity.fileKey, (File) entity.object));
        Callback callback = new StringCallback(null, null, entity.stringListener);
        Call call = okHttp.newCall(builder.build());
        call.enqueue(callback);
    }

    @Override
    public void fileAsynObject(FileRequestEntity entity) {
        Request.Builder builder = builder(entity.url, entity.heard);
        if (entity.object instanceof Map)
            builder.post(requestBody(entity.params, (Map<String, File>) entity.object));
        else if (entity.object instanceof List)
            builder.post(requestBody(entity.params, entity.fileKey, (List<File>) entity.object));
        else
            builder.post(requestBody(entity.params, entity.fileKey, (File) entity.object));
        Callback callback = new ObjectCallback(null, null, entity.objectListener, entity.cls);
        Call call = okHttp.newCall(builder.build());
        call.enqueue(callback);
    }

    private ResponseString execute(Request request) {
        ResponseString rs = new ResponseString();
        try {
            response2string(new OkHttpClient().newCall(request).execute(), rs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void runUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    private void execute(Request request, final ResponseStringListener listener) {
        Log.i(TAG, "执行网络请求");
        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (listener != null)
                    listener.start();
            }
        });
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            WeakReference<ResponseStringListener> listenerWeakReference = new WeakReference<>(listener);

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure");
                runUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listenerWeakReference.get() != null)
                            listenerWeakReference.get().failure();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.i(TAG, "onResponse");
                final ResponseString rs = new ResponseString();
                response2string(response, rs);
                runUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listenerWeakReference.get() != null) {
                            listenerWeakReference.get().success(rs);
                        }
                    }
                });
            }
        });
    }

    private ResponseObject execute(Request request, Class cls) {
        ResponseObject ro = new ResponseObject();
        try {
            response2object(new OkHttpClient().newCall(request).execute(), ro, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ro;
    }

    private void execute(Request request, final Class cls, final ResponseObjectListener listener) {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (listener != null)
                    listener.start();
            }
        });
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.failure();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final ResponseObject ro = new ResponseObject();
                response2object(response, ro, cls);
                runUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.success(ro);
                    }
                });
            }
        });
    }

    private RequestBody requestBody(byte[] raw, String type) {
        MediaType mt = MediaType.parse(String.format("application/%s; charset=utf-8", type));
        return RequestBody.create(mt, raw);
    }

    private RequestBody requestBody(Map<String, Object> params, String fileKey, File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM).addFormDataPart(fileKey, file.getName(), RequestBody.create(MediaType.parse("file/*"), file));
        if (params != null && params.size() > 0)
            for (String key : params.keySet())
                builder.addFormDataPart(key, String.valueOf(params.get(key)));
        return builder.build();
    }

    private RequestBody requestBody(Map<String, Object> params, String fileKey, List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params != null && params.size() > 0)
            for (String key : params.keySet())
                builder.addFormDataPart(key, String.valueOf(params.get(key)));
        if (files != null && files.size() > 0)
            for (File file : files)
                builder.addFormDataPart(fileKey, file.getName(), RequestBody.create(MediaType.parse("file/*"), file));
        return builder.build();
    }

    private RequestBody requestBody(Map<String, Object> params, Map<String, File> fileMap) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params != null && params.size() > 0)
            for (String key : params.keySet())
                builder.addFormDataPart(key, String.valueOf(params.get(key)));
        if (fileMap != null && fileMap.size() > 0)
            for (String key : fileMap.keySet())
                builder.addFormDataPart(key, fileMap.get(key).getName(), RequestBody.create(MediaType.parse("file/*"), fileMap.get(key)));
        return builder.build();
    }

    private RequestBody requestBody(Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet())
                builder.add(key, String.valueOf(params.get(key)));
        }
        return builder.build();
    }

    private void response2string(Response response, ResponseString rs) {
        if (!response.isSuccessful())
            return;
        rs.code = response.code();
        try {
            rs.response = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int size = response.headers().size();
        if (size <= 0)
            return;
        Map<String, String> heard = new HashMap<>();
        for (int i = 0; i < size; i++) {
            heard.put(response.headers().name(i), response.headers().value(i));
        }
        rs.heard = heard;
    }

    private <T> void response2object(Response response, ResponseObject ro, Class<T> cls) {
        if (!response.isSuccessful())
            return;
        ro.code = response.code();
        ro.response = Json.parasJson(response.body().byteStream(), cls);
        int size = response.headers().size();
        if (size <= 0)
            return;
        Map<String, String> heard = new HashMap<>();
        for (int i = 0; i < size; i++) {
            heard.put(response.headers().name(i), response.headers().value(i));
        }
        ro.heard = heard;
    }

    private Request.Builder builder(String url, Map<String, Object> heard) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (heard != null && heard.size() > 0) {
            for (String key : heard.keySet())
                builder.addHeader(key, String.valueOf(heard.get(key)));
        }
        return builder;
    }
}
