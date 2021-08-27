package com.http;

import java.util.Map;

/**
 * com.http
 * 2018/10/17 16:02
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public abstract class AbsHttp implements Http {

    protected final String url(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0)
            return url;
        StringBuffer sb = new StringBuffer(url);
        if (!url.contains("?"))
            sb.append("?");
        for (String key : params.keySet()) {
            sb.append(key).append("=").append(params.get(key)).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
