package com.xh.play;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.http.service.HttpService;
import com.http.service.HttpServiceResponse;
import com.http.service.Response;
import com.xh.base.thread.PoolManager;
import com.xh.paser.Detial;
import com.xh.paser.IVip;
import com.xh.paser.VipParsListener;
import com.xh.play.activitys.PlayActivity;

import java.util.Map;

public class HttpServer implements HttpServiceResponse {
    private static final String TAG = "HttpServer";

    @Override
    public void init(String hostName, int port) {
        Log.e(TAG, String.format("hostName:%s,port:%s", hostName, port));
    }

    @Override
    public Response get(Context context, String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session) {
        if (url.equals("/play")) {
            String name = params.get("name");
            String href = params.get("url");
            if (isMoive(href)) {
                Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                playUrl.title = name;
                playUrl.href = href;
                Intent intent = new Intent(context, PlayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(PlayActivity.DETAIL_PLAY_URL, playUrl);
                context.startActivity(intent);
            } else {
                PoolManager.io(new Runnable() {
                    @Override
                    public void run() {
                        PlayApplication application = (PlayApplication) context.getApplicationContext();
                        IVip vip = application.vip;
                        if (vip == null)
                            return;
                        vip.parse(href, new VipParsListener() {
                            boolean re = false;

                            @Override
                            public synchronized void onListener(String url) {
                                if (re)
                                    return;
                                re = true;
                                PoolManager.io(new Runnable() {
                                    @Override
                                    public void run() {
                                        Detial.DetailPlayUrl playUrl = new Detial.DetailPlayUrl();
                                        playUrl.title = name;
                                        playUrl.href = url;
                                        Intent intent = new Intent(context, PlayActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra(PlayActivity.DETAIL_PLAY_URL, playUrl);
                                        context.startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                });
            }

        }
        return null;
    }

    private boolean isMoive(String url) {
        if (url.endsWith(".mp3") || url.contains(".mp3?"))
            return true;
        if (url.endsWith(".m3u8") || url.contains(".m3u8?"))
            return true;
        if (url.endsWith(".flv") || url.contains(".flv?"))
            return true;
        return false;
    }

    @Override
    public Response post(Context context, String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session) {

        return null;
    }

    @Override
    public Response other(Context context, String method, String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session) {
        return null;
    }
}
