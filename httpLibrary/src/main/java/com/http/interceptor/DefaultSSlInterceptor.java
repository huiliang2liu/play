package com.http.interceptor;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DefaultSSlInterceptor implements SSLContextInterceptor {
    SSLContext sslcontext;

    X509TrustManager trustManager;

    {
        try {
            trustManager = new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) {
                }

            };
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            kmf.init(trustStore, "password".toCharArray());
            // 设置SSLContext
            sslcontext = SSLContext.getInstance("SSL",
                    "AndroidOpenSSL");
            sslcontext.init(kmf.getKeyManagers(),
                    new TrustManager[]{trustManager},
                    new java.security.SecureRandom());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public SSLContext interceptor() {
        return sslcontext;
    }

    public X509TrustManager trustManager() {
        return trustManager;
    }
}
