package com.xh.base.log;

import com.xh.base.thread.PoolManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileLog implements ILog {
    private int index = 0;
    private List<File> files = new ArrayList<>();
    private long fileTime;
    private long time;
    private List<String> logs = new ArrayList<>();

    public FileLog(File parent) {
        this(parent, 30 * 60 * 1000);
    }

    public FileLog(File parent, long maxTime) {
        fileTime = maxTime;
        for (int i = 0; i < 10; i++) {
            files.add(new File(parent, String.format("log%s", i)));
        }
        time = System.currentTimeMillis();
    }

    @Override
    public void v(String tag, String msg) {
        write("V", tag, msg, null);
    }

    @Override
    public void v(String tag, String msg, Throwable tr) {
        write("V", tag, msg, tr);
    }

    @Override
    public void d(String tag, String msg) {
        write("D", tag, msg, null);
    }

    @Override
    public void d(String tag, String msg, Throwable tr) {
        write("D", tag, msg, tr);
    }

    @Override
    public void i(String tag, String msg) {
        write("I", tag, msg, null);
    }

    @Override
    public void i(String tag, String msg, Throwable tr) {
        write("I", tag, msg, tr);
    }

    @Override
    public void w(String tag, String msg) {
        write("W", tag, msg, null);
    }

    @Override
    public void w(String tag, String msg, Throwable tr) {
        write("W", tag, msg, tr);
    }

    @Override
    public void w(String tag, Throwable tr) {
        write("W", tag, null, tr);
    }

    @Override
    public void e(String tag, String msg) {
        write("E", tag, msg, null);
    }

    @Override
    public void e(String tag, String msg, Throwable tr) {
        write("E", tag, msg, tr);
    }

    private synchronized void write(String type, String tag, String msg, Throwable tr) {
        String timeS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        StringBuilder sb = new StringBuilder(timeS);
        sb.append(" ").append(type).append("/").append(tag).append(": ").append(msg);
        if (tr != null) {
            sb.append(tr.getMessage());
        }
        sb.append("\n");
        logs.add(sb.toString());
        if (System.currentTimeMillis() - time > fileTime) {
            time = System.currentTimeMillis();
            write(logs);
            logs = new ArrayList<>();
            index++;
            index %= 10;
        }
    }

    private void write(List<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String log : strings)
            sb.append(log);
        File file = files.get(index);
        PoolManager.io(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    file.setLastModified(System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
