package com.http.down;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * com.http.down
 * 2018/11/1 17:45
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class DownFileImpl implements DownFile {
    private Context context;
    ExecutorService executorService;

    {
        int threadNum = Runtime.getRuntime().availableProcessors() * 3;
        threadNum = threadNum << 1;
        executorService = new ThreadPoolExecutor(threadNum, threadNum << 1, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    }

    DownFileImpl(Context context) {
        this.context = context;
    }

    /**
     * 判断网络是否连接
     *
     * @return true/false
     */
    private boolean isNetConnected() {
        if (context == null)
            return false;
        if (android.os.Build.VERSION.SDK_INT >= 23 && context.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
            return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager
                .getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return false;
        if (info.getState() == NetworkInfo.State.CONNECTED)
            return true;
        return false;
    }

    @Override
    public Down down(String url, int threads, File file) {
        return down(url, threads, file, null);
    }

    @Override
    public Down down(String url, int threads, File file, DownListener listener) {
        if (isNetConnected())
            return new DownImpl(url, threads, context, executorService, file, listener);
        else {
            if (listener != null)
                listener.downFailure(url, file);
            return new Down() {
                @Override
                public void down() {

                }

                @Override
                public void pause() {

                }

                @Override
                public boolean isPause() {
                    return true;
                }

                @Override
                public boolean isDown() {
                    return false;
                }

                @Override
                public boolean isEnd() {
                    return false;
                }

                @Override
                public float progress() {
                    return 0;
                }
            };
        }
    }

    @Override
    public Down down(String url, File file) {
        return down(url, file, null);
    }

    @Override
    public Down down(String url, File file, DownListener listener) {
        return down(url, 3, file, listener);
    }
}
