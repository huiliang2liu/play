package com.http.down;

import android.content.Context;

import java.io.File;
import java.util.concurrent.ExecutorService;

/**
 * com.http.down
 * 2018/11/2 10:29
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class DownImpl implements Down {
    private ExecutorService mService;
    private int state = 0;//0表示没有下载，1表示下载中，2表示下载暂停了，3表示下载完成
    private DownRunnable runnable;

    public DownImpl(String url, int threads, Context context, ExecutorService service, File
            file, final DownListener listener) {
        mService = service;
        if (threads <= 0)
            threads = 3;
        runnable = new DownRunnable(url, threads, context, service,
                file, new DownListener() {
            @Override
            public void downed(String url, File file) {
                state = 3;
                if (listener != null)
                    listener.downed(url, file);
            }

            @Override
            public void downFailure(String url, File file) {
                listener.downFailure(url,file);
            }
        });
    }

    @Override
    public void down() {
        if (state == 3 || state == 1)
            return;
        state = 1;
        runnable.pause(false);
        mService.execute(runnable);
    }

    @Override
    public void pause() {
        if (state == 2 || state == 0 || state == 3)
            return;
        state = 2;
        runnable.pause(true);
    }

    @Override
    public boolean isPause() {
        return state == 2;
    }

    @Override
    public boolean isDown() {
        return state == 1;
    }

    @Override
    public boolean isEnd() {
        if (state == 3)
            return true;
        if (state == 0 || !runnable.down())
            return false;
        return runnable.isEnd();
    }

    public float progress() {
        if (state == 0 || !runnable.down())
            return 0.00f;
        return runnable.progress();
    }
}
