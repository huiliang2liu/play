package com.xh.play.thread.pool;

import androidx.annotation.NonNull;

import java.util.concurrent.ThreadFactory;


/**
 * com.thread.pool
 * 2018/9/26 10:13
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class Factory implements ThreadFactory {
    @Override
    public Thread newThread(@NonNull Runnable r) {
        Thread thread = new Thread(r,"Factory"+ System.currentTimeMillis());
        thread.setPriority(Thread.MAX_PRIORITY);
        return thread;
    }
}
