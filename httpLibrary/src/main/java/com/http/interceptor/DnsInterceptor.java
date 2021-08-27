package com.http.interceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;

public interface DnsInterceptor {
    List<InetAddress> interceptor(String hostname) throws UnknownHostException;
    HostnameVerifier verifer();
}
