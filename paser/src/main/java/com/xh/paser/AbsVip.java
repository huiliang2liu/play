package com.xh.paser;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public abstract class AbsVip implements IVip {
    private WebView webView;
    private VipParsListener listener;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static final long TIME_OUT = 2000;

    public AbsVip(Context context) {
        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if (parentMoive(url) || isMoive(url)) {
                    if (listener != null)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onListener(url);
                            }
                        });
                    webView.loadUrl("");
                    return;
                }
                super.onLoadResource(view, url);
            }
        });
    }

    @Override
    public void parse(String url, VipParsListener listener) {
        this.listener = listener;
        webView.loadUrl(String.format("%s%s", base(), url));
    }

    private boolean parentMoive(String url) {
        if (url.endsWith(".mp3") || url.contains(".mp3?"))
            return true;
        if (url.endsWith(".m3u8") || url.contains(".m3u8?"))
            return true;
        if (url.endsWith(".flv") || url.contains(".flv?"))
            return true;
        if (url.endsWith(".mp4") || url.contains(".mp4?"))
            return true;
        return false;
    }

    protected abstract String base();

    protected abstract boolean isMoive(String url);
}
