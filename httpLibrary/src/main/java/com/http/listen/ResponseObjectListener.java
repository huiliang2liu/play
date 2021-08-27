package com.http.listen;

import com.http.ResponseObject;

/**
 * com.http.listen
 * 2018/10/17 15:52
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public interface ResponseObjectListener extends ResponseListener{

    /**
     * 加载成功
     */
     void success(ResponseObject response);
}
