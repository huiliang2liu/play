package com.xh.play.thread;


import android.os.Looper;

/**
 * com.thread
 * 2018/9/21 16:21
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class Handler extends android.os.Handler {
    public Handler() {
        this(Looper.getMainLooper());
    }

    public Handler(Looper looper) {
        this(looper, null);
    }

    public Handler(Callback callback) {
        this(Looper.getMainLooper(), callback);
    }

    public Handler(Looper looper, Callback callback) {
        super(looper, callback);
    }

    public void removeAllMessage(){
        removeCallbacksAndMessages(null);
    }
}
