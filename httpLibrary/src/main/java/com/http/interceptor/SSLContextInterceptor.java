package com.http.interceptor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

public interface SSLContextInterceptor {
    SSLContext interceptor();
    X509TrustManager trustManager();
}
