package com.xh.paser;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class AbsVip implements IVip {
    private static final String TAG = "AbsVip";
    private final static List<String> INVALID_DOMAIN = new ArrayList<>();

    static {
        INVALID_DOMAIN.add("vod2.buycar5.cn");
    }

    private WebView webView;
    private VipParsListener listener;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static final long TIME_OUT = 5000;
    private Runnable timeOut = new Runnable() {
        @Override
        public void run() {
            listener("");
        }
    };

    public AbsVip(Context context) {
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (parentMoive(url) || isMoive(url)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener(url);
                        }
                    });
                    return false;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if (parentMoive(url) || isMoive(url)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener(url);
                        }
                    });
                    return;
                }
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "加载失败:" + errorCode + ",失败地址:" + failingUrl + ",description:" + description);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener("");
                    }
                });
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
    }

    private String urlFormat = "^(https?)://([^/^:]*)(:([0-9]+))?/.*?(\\.(m3u8|mp4|flv))+(\\?.*)?$";

    private synchronized void listener(String url) {
        if (url == null || url.isEmpty())
            Log.e(TAG, String.format("%s:%s", name(), "解析失败"));
        else {
            Pattern pattern = Pattern.compile(urlFormat);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                if (INVALID_DOMAIN.indexOf(matcher.group(2)) >= 0) {
                    Log.e(TAG, String.format("%s:%s", name(), "域名失效"));
                    url = "";
                } else {
                    Log.e(TAG, String.format("%s:%s", name(), "解析成功"));
                    Log.e(TAG, "播放地址：" + url);
                }
            } else {
                Log.e(TAG, String.format("%s:%s", name(), "不是http或https协议"));
                url = "";
            }

        }
        handler.removeCallbacks(timeOut);
        webView.loadUrl("http:127.0.0.1:8000/dad");
//        webView.stopLoading();
        if (listener == null)
            return;
        listener.onListener(url);
        listener = null;
    }

    @Override
    public void parse(String url, VipParsListener listener) {
        Log.e(TAG, String.format("%s:%s", name(), "开始解析"));
        this.listener = listener;
        handler.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(String.format("%s%s", base(), url));
            }
        });
        handler.postDelayed(timeOut, TIME_OUT);
    }

    private boolean parentMoive(String url) {
        String p = "^(https?)://([^/^:]*)(:([0-9]+))?/.*?(\\.(m3u8|mp4|flv))+(\\?.*)?$";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(url);
        return  matcher.find();
//        if(matcher.find())
//        Log.e(TAG,url);
//        if (url.endsWith(".mp3") || url.contains(".mp3?"))
//            return true;
//        if (url.endsWith(".m3u8") || url.contains(".m3u8?"))
//            return true;
//        if (url.endsWith(".flv") || url.contains(".flv?"))
//            return true;
//        if (url.endsWith(".mp4") || url.contains(".mp4?"))
//            return true;
//        return false;
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    protected abstract String base();

    protected abstract boolean isMoive(String url);
}
