package com.http.down;

import java.io.InputStream;

/**
 * com.http.down
 * 2018/11/1 17:33
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
 interface Listener {
     void success(InputStream is, DownEntity entity);
}
