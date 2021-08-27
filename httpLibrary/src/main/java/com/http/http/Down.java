package com.http.http;

import android.os.Build;

import com.http.http.listen.ProgressListen;
import com.http.http.request.DownRequest;
import com.http.http.response.Response;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * threadPool com.xh.http 2018 2018-4-28 上午11:39:46 instructions：
 * author:liuhuiliang email:825378291@qq.com
 **/

public class Down {
    private long fileLen;
    private long downLen = 0;
    private int downCode = 0;// 0下载中，1下载成功，-1下载失败
    private File saveFile;
    private File temporaryFile;
    private float progress;
    private ProgressListen listen;
    private int threadSize;
    private String path;
    private boolean pause = false;
    Map<Integer, Long> size;

    public ProgressListen getListen() {
        return listen;
    }

    public void setListen(ProgressListen listen) {
        this.listen = listen;
    }

    public synchronized void setPause(boolean pause) {
        if (this.pause && !pause) {
            this.pause = pause;
            connection();
        } else
            this.pause = pause;
    }

    private long time;
    private long downSecond = 0;

    private synchronized void setDownLen(long down) {
        long systemTime = System.currentTimeMillis();
        if (time > 0) {
            long dxTime = systemTime - time;
            downSecond += down;
            if (dxTime > 100) {
                System.out.println(downSecond * 1000.0f / 1024 / 1024 / dxTime
                        + "m/s");
                time = systemTime;
                downSecond = 0;
            }
        } else
            time = systemTime;
        downLen += down;
        if (downLen >= fileLen) {
            progress = 100.00f;
            downCode = 1;
            if (temporaryFile.renameTo(saveFile))
                if (listen != null) {
                    listen.progress(progress);
                    listen.end(saveFile);
                }
        } else {
            long p = downLen * 10000 / fileLen;
            if (p == 10000)
                p -= 1;
            progress = p * 1.0f / 100;
            if (listen != null)
                listen.progress(progress);
        }
    }

    public int getDownCode() {
        return downCode;
    }

    public float getProgress() {
        return progress;
    }

    public Down(String path, int threadSize, File saveFile) {
        // TODO Auto-generated constructor stub
        File parent = saveFile.getParentFile();
        if (saveFile.exists())
            saveFile.delete();
        synchronized (Down.class) {
            if (!parent.exists())
                parent.exists();
        }
        this.saveFile = saveFile;
        this.threadSize = threadSize;
        size = new HashMap<Integer, Long>();
        this.path = path;
        temporaryFile = new File(parent, path.hashCode()+"");

    }

    public synchronized void connection() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(path)
                    .openConnection();
            if (Build.VERSION.SDK_INT >= 24)
                fileLen = connection.getContentLengthLong();
            else
                fileLen = connection.getContentLength();
            long len = fileLen / threadSize;
            if (pause)
                return;
            for (int i = 0; i < threadSize; i++) {
                long start = len * i;
                long end = start + len;
                if (i == threadSize - 1) {
                    end = fileLen;
                }
                if (size.containsKey(i))
                    start += size.get(i);
                new Thread(new DownRun(start, end, i)).start();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private class DownRun implements Runnable {
        private long startPos;
        private long endPos;
        private long mDownLen = 0;
        private int thread;

        public DownRun(long startPos, long endPos, int thread) {
            // TODO Auto-generated constructor stub
            this.startPos = startPos;
            this.endPos = endPos;
            this.thread = thread;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                RandomAccessFile randAF = new RandomAccessFile(temporaryFile,
                        "rwd");
                randAF.seek(startPos);
                DownRequest downRequest = new DownRequest();
                downRequest.setPath(path);
                downRequest.setStartAndEnd(startPos, endPos);
                System.out.println("down" + thread + "   " + startPos + "  "
                        + endPos);
                Response ris = HttpManage.response(downRequest);
                if (ris.getCode() == 206 || ris.getCode() == 200) {
                    InputStream is = ris.getInputStream();
                    if (is == null)
                        return;
                    if (ris.getCode() == 200) {
                        is.skip(startPos);
                    }
                    if (ris.getCode() == 206) {
                        int buffLen = 1024 * 1024;
                        byte[] buff = new byte[buffLen];
                        int readLen = -1;
                        while (!pause) {
                            readLen = is.read(buff);
                            if (readLen <= 0)
                                break;
                            randAF.write(buff, 0, readLen);
                            setDownLen(readLen);
                            mDownLen += readLen;
                            if (pause)
                                break;
                        }
                    } else {
                        long len = (endPos - startPos);
                        int buffLen = 1024 * 1024;
                        byte[] buff = new byte[buffLen];
                        int readLen = -1;
                        while (!pause) {
                            if (buffLen - len > 0) {
                                readLen = is.read(buff, 0, (int) len);
                            } else
                                readLen = is.read(buff);
                            if (readLen <= 0)
                                break;
                            randAF.write(buff, 0, readLen);
                            setDownLen(readLen);
                            len -= readLen;
                            mDownLen += readLen;
                            if (len <= 0)
                                break;
                        }
                    }
                    if (pause) {
                        size.put(thread, mDownLen);
                        System.out.println("pause" + thread + "   " + mDownLen
                                + "   " + (endPos - startPos));
                    }
                    is.close();
                } else {
                    synchronized (Down.this) {
                        downCode = -1;
                    }
                }
                randAF.close();
                setDownLen(0);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                downCode = -1;
            }
        }
    }
}
