package com.xh.play;

import android.util.Log;

import com.xh.paser.IVip;
import com.xh.paser.VipParsListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VideoInputStream extends InputStream {
    private static final String TAG = "VideoInputStream";
    private String url;
    private List<String> urls = new ArrayList<>();
    private String playUrl;
    private InputStream is;
    private Object lock = new Object();

    public VideoInputStream(PlayApplication application, String url) {
        this.url = url;
        IVip vip = application.vip;
        if (vip != null) {
            vip.parse(url, new VipParsListener() {
                @Override
                public void onListener(String url) {
                    Log.e(TAG, url);
                    addUrl(url);
                }
            });
        }
    }

    private synchronized void addUrl(String url) {
        if (!urls.contains(url))
            urls.add(url);
        if (playUrl == null || playUrl.isEmpty()) {
            playUrl = url;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setConnectTimeout(5000);
//                connection.
                        is = connection.getInputStream();
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Override
    public int read() throws IOException {
        if (is == null)
            synchronized (lock) {
                try {
                    lock.wait(25000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        if (is == null)
            return -1;
        return is.read();
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (is != null)
            is.close();
    }
}
