package com.xh.base.thread.pool;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * com.thread.pool
 * 2018/9/26 10:16
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class ExecutionHandler implements RejectedExecutionHandler {
    private final static String TAG = "ExecutionHandler";
    private final static int MAX_SAVE = 500;
    List<Runnable> runnables = new ArrayList<>();

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        synchronized (this) {
            Log.e(TAG, "任务过多溢出，保存在队列中，用于下次使用");
            if (runnables.size() > MAX_SAVE)
                return;
            runnables.add(r);
        }
    }

    protected List<Runnable> copy(int size) {
        if (size <= 0 || size > runnables.size())
            size = runnables.size();
        if (size <= 0)
            return null;
        Log.e(TAG, "获取溢出的任务再次执行");
        List<Runnable> copys = new ArrayList<>(size);
        synchronized (this) {
            copys.addAll(runnables);
            runnables.removeAll(copys);
        }
        return copys;
    }

    protected void remove(Runnable runnable) {
        synchronized (this) {
            runnables.remove(runnable);
        }
    }
}
