package com.xh.play.thread;

/**
 * com.thread
 * 2018/9/26 10:24
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class ThreadConstant {
    public static final int CPU_COUNT = Runtime.getRuntime()
            .availableProcessors();
    public static final int CPRE_POOL_SIZE = CPU_COUNT;
    public static final int MAXINUM_POOL_XIZE = (CPU_COUNT << 1);
    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = HOUR * 24;
}
