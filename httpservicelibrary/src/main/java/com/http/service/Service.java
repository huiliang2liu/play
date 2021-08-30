package com.http.service;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

class Service extends HttpService {
    private static final String TAG = "Service";
    private final static String HOST_NAME = "http_service_host_name";
    private final static String PORT = "http_service_port";
    public final static String H_S_R = "http_service_response";
    private HttpServiceResponse response;
    private Context context;

    protected Service(Context context, String hostName, int port) {
        super(hostName, port);
        Log.d(TAG, "开启服务");
//        Log.d(TAG, );
        this.context = context;
        try {
            Class cl = Class.forName(context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData.getString(H_S_R));
            if (HttpServiceResponse.class.isAssignableFrom(cl)) {
                response = (HttpServiceResponse) cl.newInstance();
                response.init(context,getIPAddress(context),port);
                start();
            } else {
                Log.d(TAG, "相应类型不对");
            }

        } catch (Exception e) {
            Log.d(TAG, "开启服务失败", e);
        }
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {    // 当前使用2G/3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {    // 当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());    // 得到IPV4地址
                return ipAddress;
            }
        } else {
            // 当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    @Override
    public Response serve(IHTTPSession session) {
        if (response != null) {
            String uri = session.getUri();
            String method = session.getMethod().toString();
            Log.d(TAG, String.format("接收到请求：uri=%s,method=%s", uri, method));
            if ("GET".equals(method))
                return response.get(uri, session.getHeaders(), session.getParms(), session);
            if ("POST".equals(method))
                return response.post(uri, session.getHeaders(), session.getParms(), session);
            return response.other(method, uri, session.getHeaders(), session.getParms(), session);
        }
        return super.serve(session);
    }

    @Override
    public void stop() {
        super.stop();
        Log.d(TAG, "停止服务");
    }


}
