package com.http.listen;

import com.http.ResponseString;

/**
 * com.http.listen
 * 2018/10/17 15:50
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public interface ResponseStringListener extends ResponseListener {
    /**
     * 加载成功
     */
    void success(ResponseString response);
}
