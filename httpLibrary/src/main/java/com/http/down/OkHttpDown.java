package com.http.down;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * com.http.down
 * 2018/11/1 17:32
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class OkHttpDown {
    OkHttpClient okHttpClient;

    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(8, TimeUnit.SECONDS); // 设置连接超时时间
        builder.readTimeout(20, TimeUnit.SECONDS);// 设置读取数据超时时间
        okHttpClient = builder.build();
    }

    OkHttpDown(final DownEntity downEntity, final Listener listener, String url) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.addHeader("Range", "bytes=" + downEntity.start + "-" + downEntity.end);
        final Request request = builder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                response.body().bytes();
                listener.success(response.body().byteStream(), downEntity);
            }
        });
    }
}
