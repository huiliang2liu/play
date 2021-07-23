package com.media.bind;
import com.media.bind.IMediaListener;
interface IPlayMusic{
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
         * 设置监听速度
         *
         * @param speed
         */
    void setMediaListener(in IMediaListener listener);
}