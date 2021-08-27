package com.http.http.request;

import com.http.http.util.Method;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * threadPool com.xh.http.util 2018 2018-4-27 下午6:12:21 instructions：
 * author:liuhuiliang email:825378291@qq.com
 **/

public class Request {
    protected Method method;
    protected Map<String, Object> head;
    private int connectTime = 5 * 1000;// 超时设置
    private int readTimeOut = 5 * 1000;// 读取超时
    protected Map<String, Object> params;
    private String path;
    private boolean isRedirect = false;
    private boolean isCaches = false;
    private String charset = "UTF-8";// 设置编码
    private SSLSocketFactory factory;
    private HostnameVerifier verifier;
    private List<String> hostNames = new ArrayList<>();
    static {
        CookieManager manager=new CookieManager();
        manager.setCookiePolicy(new CookiePolicy() {
            @Override
            public boolean shouldAccept(URI uri, HttpCookie cookie) {
//                HttpCookie.domainMatches(cookie.getDomain(),uri.getHost());
                return true;
            }
        });
        CookieHandler.setDefault(manager);
    }

    {
        params = new HashMap<String, Object>();
        head = new HashMap<String, Object>();
        head.put("Accept", "text/html");
        head.put("Accept-Charset", "utf-8");
        head.put("Accetpt-Encoding", "deflate");
        head.put("Accept-Language", "zh-cn");
        head.put("Cache-Control", "no-cache");
        head.put("Connection", "keep-Alive");
        head.put(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
        head.put("Content-Type", "application/x-www-from-urlencoded");
        head.put("Charset", "utf-8");
        method = Method.GET;
        verifier = new HostnameVerifier() {

            @Override
            public boolean verify(String hostName, SSLSession arg1) {
                // TODO Auto-generated method stub
                if (hostNames.size() <= 0)
                    return true;
                return hostNames.contains(hostName);
            }
        };
    }


    public Method getMethod() {
        return method;
    }

    public Request setMethod(Method method) {
        this.method = method;
        return this;
    }

    public Map<String, Object> getHead() {
        return head;
    }

    public Request addHead(Map<String, Object> head) {
        if (head != null && head.size() > 0)
            this.head.putAll(head);
        return this;
    }

    public Request addHead(String key, String value) {
        head.put(key, value);
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Request addParams(Map<String, Object> params) {
        if (params != null && params.size() > 0)
            this.params.putAll(params);
        return this;
    }

    public Request addParams(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public String getPath() {
        if (method == Method.GET) {
            String params = params2string();
            if (params != null) {
                StringBuffer stringBuffer = new StringBuffer(path);
                if (path.indexOf("?") > 1)
                    stringBuffer.append("&");
                else
                    stringBuffer.append("?");
                stringBuffer.append(params);
                return stringBuffer.toString();
            }
        }
        return path;
    }

    public String params2string() {
        if (params != null && params.size() > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            Iterator<Entry<String, Object>> iterator = params.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                Object object = entry.getValue();
                String value = object == null ? "" : object.toString();
                stringBuffer.append(entry.getKey()).append("=");
                try {
                    stringBuffer.append(URLEncoder.encode(value, charset));
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    stringBuffer.append(value);
                }
                stringBuffer.append("&");
            }
            return stringBuffer.substring(0, stringBuffer.length() - 1);
        }
        return null;
    }

    public SSLSocketFactory getFactory() {
        return factory;
    }

    public void setFactory(SSLSocketFactory factory) {
        this.factory = factory;
    }

    public HostnameVerifier getVerifier() {
        return verifier;
    }

    public Request addHostName(String hostName) {
        if (hostName == null || hostName.isEmpty())
            return this;
        hostNames.add(hostName);
        return this;
    }

    public void setVerifier(HostnameVerifier verifier) {
        this.verifier = verifier;
    }

    public Request setPath(String path) {
        this.path = path;
        return this;
    }

    public int getConnectTime() {
        return connectTime;
    }

    public Request setConnectTime(int connectTime) {
        this.connectTime = connectTime;
        return this;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public Request setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public boolean isRedirect() {
        return isRedirect;
    }

    public Request setRedirect(boolean isRedirect) {
        this.isRedirect = isRedirect;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Request setCharset(String charset) {
        this.charset = charset;
        head.put("Charset", charset);
        return this;
    }

    public Request setAccept(String accept) {
        head.put("Accept", accept);
        return this;
    }

    public Request setAcceptCharset(String charset) {
        head.put("Accept-Charset", charset);
        return this;
    }

    public Request setAcceptEncod(String encod) {
        head.put("Accetpt-Encoding", encod);
        return this;
    }

    public Request setAcceptLanguage(String language) {
        head.put("Accept-Language", language);
        return this;
    }

    public Request setCacheControl(String cacheControl) {
        head.put("Cache-Control", cacheControl);
        return this;
    }

    public Request setConnection(String connection) {
        head.put("Connection", connection);
        return this;
    }

    public Request setUserAgent(String userAgent) {
        head.put("User-Agent", userAgent);
        return this;
    }

    public Request setContentType(String type) {
        head.put("Content-Type", type);
        return this;
    }

    public Request setCookie(String cookie) {
        head.put("Cookie", cookie);
        return this;
    }

    public Request setContentLength(String contentLength) {
        head.put("Content-Length", contentLength);
        return this;
    }

    public Request setReferer(String referer) {
        head.put("Referer", referer);
        return this;
    }

    public Request setRange(String range) {
        head.put("Range", range);
        return this;
    }

    public Request putHead(String key, String value) {
        head.put(key, value);
        return this;
    }

    public boolean isCaches() {
        return isCaches;
    }

    public void setCaches(boolean isCaches) {
        this.isCaches = isCaches;
    }

    public void report(OutputStream os) {
        String params = params2string();
        if (params == null)
            return;
        try {
            os.write(params.getBytes());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
