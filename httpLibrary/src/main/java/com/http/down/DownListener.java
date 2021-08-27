package com.http.down;

import java.io.File;

/**
 * com.http.down
 * 2018/11/1 18:54
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public interface DownListener {
    void downed(String url, File file);

    void downFailure(String url, File file);
}
