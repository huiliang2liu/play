package com.xh.play.thread;

/**
 * com.thread
 * 2018/9/21 17:05
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public abstract class Runnable implements java.lang.Runnable {

    public void single() {
        PoolManager.single(this);
    }

    public void singleRemove() {
        PoolManager.singleRemove(this);
    }


    public void shortTime() {
        PoolManager.shortTime(this);
    }

    public  void shortTimeRemove() {
        PoolManager.shortTimeRemove(this);
    }

    public  void longTime() {
        PoolManager.longTime(this);
    }

    public  void longTimeRemove() {
        PoolManager.longTimeRemove(this);
    }

    public  void scheduled(long delay, long period) {
        PoolManager.scheduled(this,delay,period);
    }

    public  void scheduledRemove() {
        PoolManager.scheduledRemove(this);
    }
    public  void scheduled( long delay) {
        scheduled(delay, 0);
    }

    public  void scheduledEnd(long delay, long period) {
        PoolManager.scheduledEnd(this,delay,period);
    }

    public  void runUiThread() {
        PoolManager.runUiThread(this);
    }


    public  void runUiThread( long delay) {
        PoolManager.runUiThread(this, delay);
    }


    public  void runUiThreadAtTime( long uptimeMillis) {
        PoolManager.runUiThreadAtTime(this,uptimeMillis);
    }

    public  void removeUi() {
        PoolManager.removeUi(this);
    }

}
