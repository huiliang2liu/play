package com.xh.base.log;


import java.util.ArrayList;
import java.util.List;

public class Logger {
    public static final String TAG = "Logger";
    private static List<ILog> logs = new ArrayList<>();

    public static void addLog(ILog log){
        logs.add(log);
    }


    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        for (ILog log : logs)
            log.v(tag, msg);
    }


    public static void v(String msg, Throwable tr) {
        v(TAG, msg, tr);
    }

    public static void v(String tag, String msg, Throwable tr) {
        for (ILog log : logs)
            log.v(tag, msg, tr);
    }


    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        for (ILog log : logs)
            log.d(tag, msg);
    }


    public static void d(String msg, Throwable tr) {
        d(TAG, msg, tr);
    }

    public static void d(String tag, String msg, Throwable tr) {
        for (ILog log : logs)
            log.d(tag, msg, tr);
    }


    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        for (ILog log : logs)
            log.i(tag, msg);
    }


    public static void i(String msg, Throwable tr) {
        i(TAG, msg, tr);
    }

    public static void i(String tag, String msg, Throwable tr) {
        for (ILog log : logs)
            log.i(tag, msg, tr);
    }


    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        for (ILog log : logs)
            log.w(tag, msg);
    }


    public static void w(String tag, String msg, Throwable tr) {
        for (ILog log : logs)
            log.w(tag, msg, tr);
    }


    public static void w(String tag, Throwable tr) {
        for (ILog log : logs)
            log.w(tag, tr);
    }


    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        for (ILog log : logs)
            log.e(tag, msg);
    }


    public static void e(String msg, Throwable tr) {
        e(TAG, msg, tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        for (ILog log : logs)
            log.e(tag, msg, tr);
    }
}
