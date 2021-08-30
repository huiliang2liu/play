package com.xh.movies.vip;

import android.content.Context;
import android.util.Log;


import com.xh.movies.ThreadManager;
import com.xh.paser.IVip;
import com.xh.paser.VipParsListener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class VipImpl implements IVip {
    private static final String TAG = "VipImpl";
    private List<IVip> vips;

    public VipImpl(List<IVip> vips) {
        this.vips = vips;
    }

    @Override
    public String name() {
        return "VipImpl";
    }

    @Override
    public void parse(String url, VipParsListener listener) {
        if (listener == null)
            return;
        VipParsListener l = (VipParsListener) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{VipParsListener.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("onListener"))
                    Log.e(TAG, String.valueOf(args[0]));
                return method.invoke(listener, args);
            }
        });
        if (url != null && !url.isEmpty())
            ThreadManager.runUiThread(new Runnable() {
                @Override
                public void run() {
                    for (IVip vip : vips)
                        vip.parse(url, l);
                }
            });

    }
}
