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
    private List<IVip> vips = new ArrayList<>();

    public VipImpl(Context context) {
        vips.add(new Vip660e(context));
        vips.add(new Vip17kyun(context));
        vips.add(new VipJx(context));
        vips.add(new Vip8089g(context));
        vips.add(new VipPanGuJieXi(context));
        vips.add(new VipJieXi(context));
        vips.add(new VipH8jx(context));
        vips.add(new Vip618g(context));
        vips.add(new VipOkjx(context));
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
