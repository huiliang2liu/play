package com.http.service;

import android.content.Context;
import android.content.Intent;

import java.util.Map;

public class DefaultResponse implements HttpServiceResponse {
    @Override
    public void init(Context context,String hostName, int port) {

    }

    @Override
    public Response get( String url, Map<String, String> headers, Map<String, String> params, HttpService.IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        msg += "<p>We serve " + url + " !</p>";
//        Intent intent=new Intent();
//        intent.setAction("play.activity.url");
//        intent.putExtra("url",params.get("url"));
//        context.sendBroadcast(intent);
        return HttpService.newFixedLengthResponse( msg + "</body></html>\n" );
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
