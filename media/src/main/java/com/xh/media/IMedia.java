package com.xh.media;

import android.content.Context;
import android.view.SurfaceHolder;

import java.lang.reflect.Proxy;

public interface IMedia {
    /**
     * 设置监听
     *
     * @param listener
     */
    void setMedialistener(MediaListener listener);

    /**
     * 设置画布
     *
     * @param holder
     */
    void setDisplay(SurfaceHolder holder);

    /**
     * 暂停
     */
    void pause();

    /**
     * 判断播放状态
     */
    boolean isPlay();

    /**
     * 播放
     */
    void play();


    /**
     * 设置地址
     *
     * @param path
     */
    void setPath(String path);

    /**
     * 获取播放时长
     *
     * @return
     */
    long getDuration();

    /**
     * 获取播放进度
     *
     * @return
     */
    long getCurrentPosition();

    /**
     * 设置进度
     *
     * @param seek
     */
    void seekTo(long seek);

    /**
     * 设置播放速度
     *
     * @param speed
     */
    void setSpeed(float speed);

    /**
     * 释放资源
     */
    void destroy();

    /**
     * 停止
     */
    void stop();

    /**
     * 重置
     */
    void reset();

    enum MediaType {
        ANDROID, IJK, EXO,TVBUS,NODE
    }

    class Build {
        protected MediaType type;
        protected Context mContext;

        public Build setMediaType(MediaType type) {
            if (type == null)
                this.type = MediaType.ANDROID;
            else
                this.type = type;
            return this;
        }

        public Build setContext(Context context) {
            this.mContext = context;
            return this;
        }

        public IMedia build() {
            if (mContext == null)
                throw new RuntimeException("you context is null");
            return (IMedia) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{IMedia.class}, new MediaInvocationHandler(this));
        }
    }
}
