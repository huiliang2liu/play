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
import com.xh.play.utils.Base64;

import java.util.Map;

public class HttpServer implements HttpServiceResponse {
    private static final String TAG = "HttpServer";
    private Context context;
    private PlayApplication application;

    @Override
    public void init(Context context, String hostName, int port) {
        Log.e(TAG, String.format("hostName:%s,port:%s", hostName, port));
        this.context = context;
        application = (PlayApplication) context.getApplicationContext();
        application.setPort(port);
    }

    @Override
    public Response get(String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session) {
        if (url.equals("/play")) {
            return play(params.get("name"), params.get("url"));
        } else if (url.equals("/movie")) {
            return movie(params.get("url"));
        }
        return null;
    }

    private Response play(String name, String href) {
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
        return HttpService.newFixedLengthResponse("ok");
    }

    private Response movie(String url) {
        url = new String(Base64.decode(url.getBytes(), 0));
        Log.e(TAG, url);
        Response response = HttpService.newFixedLengthResponse(Response.Status.OK, "application/x-mpegURL", new VideoInputStream(application, url), 1000000);
        return response;
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
    public Response post(String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session) {

        return null;
    }

    @Override
    public Response other(String method, String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session) {
        return null;
    }
}
