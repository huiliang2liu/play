package com.xh.base.thread.pool;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * com.thread.pool
 * 2018/9/26 10:12
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class IOThreadPool extends ThreadPoolExecutor {
    private final static String TAG = "CPUThreadPool";
    private ExecutionHandler handler;
    private final static int QUEUE_SIZE = 500;


    public IOThreadPool(){
        super(0, 100, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new Factory());
        handler = new ExecutionHandler();
        setRejectedExecutionHandler(handler);
    }


    @Override
    protected synchronized void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        BlockingQueue<Runnable> queue = getQueue();
        int size = QUEUE_SIZE - queue.size();
        if (size > 0 && size < QUEUE_SIZE) {
            submit(handler.copy(size));
        }
    }


    private void submit(Collection<Runnable> collection) {
        if (collection == null || collection.size() <= 0)
            return;
        for (Runnable runnable : collection) {
            submit(runnable);
        }
    }
}
