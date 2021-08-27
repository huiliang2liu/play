package com.xh.movies;

import android.os.Handler;
import android.os.Looper;

public class ThreadManager {
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static void runUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}
