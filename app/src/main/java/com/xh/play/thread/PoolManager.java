package com.xh.play.thread;

import android.os.Looper;


import com.xh.play.thread.pool.CPUThreadPool;
import com.xh.play.thread.pool.IOThreadPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PoolManager {
    private static volatile ThreadPoolExecutor singleThread = new CPUThreadPool(1);
    private static volatile ThreadPoolExecutor shortTimeThread = new CPUThreadPool();
    //    private static volatile ThreadPoolExecutor longTimeThread = new CPUThreadPool();
    private static volatile ThreadPoolExecutor longTimeThread = new CPUThreadPool();
//    private static volatile ThreadPoolExecutor ioThread = new IOThreadPool();//统计和广告统计太平凡去掉
    private static volatile ThreadPoolExecutor ioThread = new IOThreadPool();
    private static volatile ScheduledThreadPoolExecutor scheduledThread = new ScheduledThreadPoolExecutor(0);
    private static volatile Handler handler = new Handler();

    public static Future single(java.lang.Runnable runnable) {
        return singleThread.submit(runnable);
    }

    public static void singleRemove(java.lang.Runnable runnable) {
        singleThread.remove(runnable);
    }

    public static void runUiThread(java.lang.Runnable runnable) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            runnable.run();
            return;
        }
        handler.post(runnable);
    }

    public static void runUiThread(Collection<java.lang.Runnable> runnables) {
        if (runnables == null || runnables.size() <= 0)
            return;
        for (java.lang.Runnable runnable : runnables)
            runUiThread(runnable);
    }

    public static void runUiThread(java.lang.Runnable runnable, long delay) {
        handler.postDelayed(runnable, delay);
    }

    public static void runUiThread(Collection<java.lang.Runnable> runnables, long delay) {
        if (delay <= 0) {
            runUiThread(runnables);
            return;
        }
        if (runnables == null || runnables.size() <= 0)
            return;
        for (java.lang.Runnable runnable : runnables) {
            runUiThread(runnable, delay);
        }
    }

    public static void runUiThreadAtTime(java.lang.Runnable runnable, long uptimeMillis) {
        handler.postAtTime(runnable, uptimeMillis);
    }

    public static void runUiThreadAtTime(Collection<java.lang.Runnable> runnables, long uptimeMillis) {
        if (uptimeMillis <= System.currentTimeMillis()) {
            runUiThread(runnables);
            return;
        }
        if (runnables == null || runnables.size() <= 0)
            return;
        for (java.lang.Runnable runnable : runnables)
            runUiThreadAtTime(runnable, uptimeMillis);
    }

    public static void removeUi(java.lang.Runnable runnable) {
        handler.removeCallbacks(runnable);
    }

    public static void removeUi(Collection<java.lang.Runnable> runnables) {
        if (runnables == null || runnables.size() <= 0)
            return;
        for (java.lang.Runnable runnable : runnables)
            handler.removeCallbacks(runnable);
    }

    public static void clearUi() {
        handler.removeCallbacksAndMessages(null);
    }

    public static List<Future> single(Collection<java.lang.Runnable> runnables) {
        if (runnables == null || runnables.size() <= 0)
            return null;
        List<Future> futures = new ArrayList<>(runnables.size());
        for (java.lang.Runnable runnable : runnables)
            futures.add(single(runnable));
        return futures;
    }

    public static void singleRemove(Collection<java.lang.Runnable> runnables) {
        if (runnables.size() <= 0)
            return;
        for (java.lang.Runnable runnable : runnables)
            singleRemove(runnable);
    }

    public static Future shortTime(java.lang.Runnable runnable) {
        return shortTimeThread.submit(runnable);
    }

    public static void shortTimeRemove(java.lang.Runnable runnable) {
        shortTimeThread.remove(runnable);
    }

    public static List<Future> shortTime(Collection<java.lang.Runnable> runnables) {
        if (runnables == null || runnables.size() <= 0)
            return null;
        List<Future> futures = new ArrayList<>(runnables.size());
        for (java.lang.Runnable runnable : runnables)
            futures.add(shortTime(runnable));
        return futures;
    }

    public static void shortTimeRemove(Collection<java.lang.Runnable> runnables) {
        if (runnables == null || runnables.size() <= 0)
            return;
        for (java.lang.Runnable runnable : runnables)
            shortTimeThread.remove(runnable);
    }

    public static Future longTime(java.lang.Runnable runnable) {
        return longTimeThread.submit(runnable);
    }

    public static void longTimeRemove(java.lang.Runnable runnable) {
        longTimeThread.remove(runnable);
    }

    public static List<Future> longTime(Collection<java.lang.Runnable> runnables) {
        if (runnables == null || runnables.size() <= 0)
            return null;
        List<Future> futures = new ArrayList<>(runnables.size());
        for (java.lang.Runnable runnable : runnables)
            futures.add(longTime(runnable));
        return futures;
    }

    public static void longTimeRemove(Collection<java.lang.Runnable> runnables) {
        if (runnables == null || runnables.size() <= 0)
            return;
        for (java.lang.Runnable runnable : runnables)
            longTimeThread.remove(runnable);
    }

    public static Future scheduled(java.lang.Runnable runnable, long delay, long period) {
        if (delay <= 0)
            delay = 0;
        if (period <= 0)
            return scheduledThread.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        else
            return scheduledThread.scheduleAtFixedRate(runnable, delay, period, TimeUnit.MILLISECONDS);
    }

    public static void scheduledRemove(java.lang.Runnable runnable) {
        scheduledThread.remove(runnable);
    }

    public static void scheduledRemove(Collection<java.lang.Runnable> runnables) {
        if (runnables == null || runnables.size() <= 0)
            return;
        for (java.lang.Runnable runnable : runnables)
            scheduledThread.remove(runnable);
    }

    public static Future scheduled(java.lang.Runnable runnable, long delay) {
        return scheduled(runnable, delay, 0);
    }

    public static Future scheduledEnd(java.lang.Runnable runnable, long delay, long period) {
        if (period <= 0)
            return scheduled(runnable, delay, period);
        else {
            return scheduledThread.scheduleWithFixedDelay(runnable, delay, period, TimeUnit.MILLISECONDS);
        }
    }

    public static Future io(java.lang.Runnable runnable) {
        return ioThread.submit(runnable);
    }

    public static List<Future> io(Collection<java.lang.Runnable> runnables) {
        if (runnables == null || runnables.size() <= 0)
            return null;
        List<Future> futures = new ArrayList<>(runnables.size());
        for (java.lang.Runnable runnable : runnables)
            futures.add(io(runnable));
        return futures;
    }

    public static void ioRemove(java.lang.Runnable runnable) {
        ioThread.remove(runnable);
    }

    public static void ioRemove(Collection<java.lang.Runnable> runnables) {
        if (runnables == null || runnables.size() <= 0)
            return;
        for (java.lang.Runnable runnable : runnables)
            ioRemove(runnable);
    }

    public static void shutdown() {
        singleShutdown();
        shortTimeShutdown();
        longTimeShutdown();
        ioShutdown();
        scheduledShutdown();
    }

    public static List<java.lang.Runnable> shutdownNow() {
        List<java.lang.Runnable> now = new ArrayList<>();
        List<java.lang.Runnable> single = singleShutdownNow();
        if (single != null && single.size() > 0)
            now.addAll(single);
        List<java.lang.Runnable> s = shortTimeShutdownNow();
        if (s != null && s.size() > 0)
            now.addAll(s);
        List<java.lang.Runnable> l = longTimeShutdownNow();
        if (l != null && l.size() > 0)
            now.addAll(l);
        List<java.lang.Runnable> io = ioShutdownNow();
        if (io != null && io.size() > 0)
            now.addAll(io);
        List<java.lang.Runnable> scheduled = scheduledShutdownNow();
        if (scheduled != null && scheduled.size() > 0)
            now.addAll(scheduled);
        return now;
    }

    public static void singleShutdown() {
        singleThread.shutdown();
    }

    public static List<java.lang.Runnable> singleShutdownNow() {
        return singleThread.shutdownNow();
    }

    public static void shortTimeShutdown() {
        shortTimeThread.shutdown();
    }

    public static List<java.lang.Runnable> shortTimeShutdownNow() {
        return shortTimeThread.shutdownNow();
    }

    public static void longTimeShutdown() {
        longTimeThread.shutdown();
    }

    public static List<java.lang.Runnable> longTimeShutdownNow() {
        return longTimeThread.shutdownNow();
    }

    public static void ioShutdown() {
        ioThread.shutdown();
    }

    public static List<java.lang.Runnable> ioShutdownNow() {
        return ioThread.shutdownNow();
    }

    public static void scheduledShutdown() {
        scheduledThread.shutdown();
    }

    public static List<java.lang.Runnable> scheduledShutdownNow() {
        return scheduledThread.shutdownNow();
    }

    public static void race(java.lang.Runnable runnable, java.lang.Runnable... runnables) {
        if (runnable == null) {
            if (runnables == null || runnables.length <= 0)
                return;
            for (java.lang.Runnable r : runnables)
                longTime(r);
            return;
        }
        if (runnables == null || runnables.length <= 0) {
            longTime(runnable);
            return;
        }
        longTime(new RaceRefereeRunnable(runnable, runnables));
    }

    private static class RaceRefereeRunnable implements java.lang.Runnable {
        CountDownLatch countDownLatch;
        private java.lang.Runnable runnable;
        private List<java.lang.Runnable> runnables;

        public RaceRefereeRunnable(java.lang.Runnable runnable, java.lang.Runnable... runnables) {
            int len = runnables.length;
            countDownLatch = new CountDownLatch(len);
            this.runnables = new ArrayList(len);
            for (int i = 0; i < len; i++) {
                this.runnables.add(new RaceRunnable(countDownLatch, runnables[i]));
            }
            this.runnable = runnable;
        }

        @Override
        public void run() {
            try {
                for (java.lang.Runnable runnable : runnables)
                    longTime(runnable);
                countDownLatch.await();
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RaceRunnable implements java.lang.Runnable {
        CountDownLatch countDownLatch;
        java.lang.Runnable runnable;

        RaceRunnable(CountDownLatch countDownLatch, java.lang.Runnable runnable) {
            this.countDownLatch = countDownLatch;
            this.runnable = runnable;
        }

        @Override
        public void run() {
            runnable.run();
            countDownLatch.countDown();
        }
    }
}
