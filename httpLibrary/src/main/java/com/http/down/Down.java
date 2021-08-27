package com.http.down;

/**
 * com.http.down
 * 2018/11/2 15:03
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public interface Down {
    public void down();

    public void pause();

    public boolean isPause();

    public boolean isDown();

    public boolean isEnd();

    public float progress();
}
