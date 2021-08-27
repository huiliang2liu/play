// IMediaListener.aidl
package com.media.bind;

// Declare any non-default types here with import statements

interface IMediaListener {
     /**
        * 播放完成
        */
       void onCompletion();

       /**
        * 播放失败
        */
       void onError(int what, int extra);

       /**
        * 准备完成
        */
       void onPrepared();

       boolean onInfo(int what, int extra);

       void onBufferingUpdate(int percent);

       void onVideoSizeChanged(int width, int height);
}
