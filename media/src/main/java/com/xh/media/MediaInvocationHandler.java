package com.xh.media;

import android.util.Log;

import com.xh.media.exoplayer.ExoMediaImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


class MediaInvocationHandler implements InvocationHandler {
    IMedia iMedia;

    MediaInvocationHandler(IMedia.Build build) {
        switch (build.type) {
            case ANDROID:
                iMedia = new MediaImpl(new AndroidMedia(build.mContext));
                break;
            case IJK:
                iMedia = new MediaImpl(new IjkMedia(build.mContext));
                break;
            case EXO:
                iMedia = new MediaImpl(new ExoMedia(build.mContext));
                break;
            case TVBUS:
                iMedia = new MediaImpl(new ExoMediaImpl(build.mContext));
                break;

        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d("IMedia",method.getName());
        return method.invoke(iMedia, args);
    }
}
