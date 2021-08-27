package com.http.down;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * com.http.down
 * 2018/11/1 17:46
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class DownRunnable implements Runnable {
    private final static String TAG = "DownRunnable";
    private final static String START = "start";
    private final static String LEN = "len";
    private final static String END = "end";
    private boolean okhttp = false;
    private int mThreads;
    private File savePath;
    private String mUrl;
    private ExecutorService mService;
    private List<DownEntity> downEntities;
    private boolean mPause = false;
    private File mFile;
    private File temporaryFile;
    private DownListener mDownListener;
    private Object pauseLock = new Object();
    private Handler handler = new Handler(Looper.getMainLooper());

    {
        try {
            Class.forName("okhttp3.OkHttpClient");
            okhttp = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            okhttp = false;
        }
    }

    public void pause(boolean pause) {
        mPause = pause;
    }

    public DownRunnable(String url, int threads, Context context, ExecutorService service, File file, DownListener listener) {
        mService = service;
        File parent = context.getExternalFilesDir("down");
        if (!parent.exists())
            parent.mkdirs();
        savePath = new File(context.getExternalFilesDir("down"), url.hashCode() + ".xml");
        parent = file.getParentFile();
        if (!parent.exists())
            parent.mkdirs();
        temporaryFile = new File(parent, url.hashCode() + "");
        mUrl = url;
        mThreads = threads;
        mFile = file;
        mDownListener = listener;
    }

    @Override
    public void run() {
        downEntities = url2entities(mUrl, mThreads);
        if (downEntities.size() <= 0) {
            Log.i(TAG, "下载出问题了");
            if (mDownListener != null)
                mDownListener.downFailure(mUrl, mFile);
            temporaryFile.delete();
        }
        for (DownEntity entity : downEntities) {
            Listener listener = new Listener() {
                @Override
                public void success(InputStream is, DownEntity entity1) {
                    if (mPause) {
                        entity1.pause = true;
                        save();
                        return;
                    }
                    saveFile(is, entity1);
                }
            };
            if (entity.end == entity.start)
                continue;
            if (okhttp)
                new OkHttpDown(entity, listener, mUrl);
            else
                new HttpDown(entity, listener, mUrl, mService);
        }
    }

    private void saveFile(InputStream is, DownEntity entity) {
        try {
            RandomAccessFile raf;
            synchronized (temporaryFile) {
                raf = new RandomAccessFile(temporaryFile, "rwd");
            }
            raf.seek(entity.start);
            int len = -1;
            byte[] buff = new byte[1024 * 1024];
            while ((len = is.read(buff)) > 0) {
                raf.write(buff, 0, len);
                entity.len += len;
                entity.start += len;
                if (mPause) {
                    synchronized (pauseLock) {
                        entity.pause = true;
                        save();
                    }
                    break;
                }
            }
            if (!mPause) {
                entity.start = entity.end;
                end();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mPause = true;
            synchronized (pauseLock) {
                entity.pause = true;
                save();
            }
        }
    }

    public boolean isPause() {
        boolean pause = true;
        for (DownEntity entity : downEntities) {
            pause &= entity.pause;
            if (!pause)
                return pause;
        }
        return pause;
    }

    public boolean isEnd() {
        boolean end = true;
        for (DownEntity entity : downEntities) {
            end &= entity.start == entity.end;
            if (!end)
                return end;
        }
        return end;
    }

    public boolean down() {
        return downEntities != null && downEntities.size() > 0;
    }

    public synchronized float progress() {
        long fileLen = 0;
        long len = 0;
        for (DownEntity entity : downEntities) {
            len += entity.len;
            if (fileLen < entity.end)
                fileLen = entity.end;
        }
        float progress = len * 100.0f / fileLen;
        return new BigDecimal(progress).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
    }

    private synchronized void end() {
        if (!isEnd())
            return;
        Log.e(TAG, "下载完成");
        temporaryFile.renameTo(mFile);
        if (savePath.exists())
            savePath.delete();

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mDownListener != null)
                    mDownListener.downed(mUrl, mFile);
            }
        });
    }

    private void save() {
        boolean save = true;
        for (DownEntity entity : downEntities) {
            save &= entity.pause;
            if (!save)
                return;
        }
        JSONArray array = new JSONArray();
        for (DownEntity entity : downEntities) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(START, entity.start);
                jo.put(END, entity.end);
                jo.put(LEN, entity.len);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(jo);
        }
        String json = array.toString();
        Log.e(TAG, "保存数据" + json);
        if (savePath.exists())
            savePath.delete();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(savePath);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<DownEntity> url2entities(String url, int threads) {
        if (downEntities != null && downEntities.size() > 0)
            return downEntities;
        List<DownEntity> de = new ArrayList<>();
        try {
            if (savePath.exists()) {
                String s = null;
                InputStream is = new FileInputStream(savePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buff = new byte[1024 * 1024];
                byte[] arr = null;
                try {
                    int len = is.read(buff);
                    while (len > 0) {
                        baos.write(buff, 0, len);
                        len = is.read(buff);
                    }
                    arr = baos.toByteArray();
                    s = new String(arr);
                    baos.flush();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    baos.close();
                    is.close();
                }
                if (s != null && !s.isEmpty()) {
                    JSONArray array = new JSONArray(s);
                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            DownEntity entity = new DownEntity();
                            entity.start = jo.getLong(START);
                            entity.end = jo.getLong(END);
                            entity.len = jo.getLong(LEN);
                            de.add(entity);
                        }
                        return de;
                    }
                }
            }
            HttpURLConnection connection = (HttpURLConnection) new URL(url)
                    .openConnection();
            long fileLen = -1;
            if (Build.VERSION.SDK_INT >= 24)
                fileLen = connection.getContentLengthLong();
            else
                fileLen = connection.getContentLength();
            long l = fileLen / threads;
            for (int i = 0; i < threads; i++) {
                DownEntity entity = new DownEntity();
                entity.start = l * i;
                if (threads - 1 == i) {
                    entity.end = fileLen;
                } else {
                    entity.end = entity.start + l;
                }
                de.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return de;
    }
}
