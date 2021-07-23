package com.xh.play.media.exoplayer;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.BaseDataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.xh.play.media.exoplayer.rtsp.RtspClient;

import java.io.IOException;

public class RtspDataSource extends BaseDataSource {
    private static final String TAG = "RtspDataSource";
    private Uri uri;
    private RtspClient channel;

    protected RtspDataSource() {
        super(true);
    }

    @Override
    public long open(DataSpec dataSpec) throws IOException {
        uri = dataSpec.uri;
        int port = uri.getPort();
        if (port < 0)
            port = 554;
        channel = new RtspClient(uri.getHost(), port, uri.toString());
        return C.LENGTH_UNSET;
    }


    @Override
    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        if (channel != null)
            return channel.read(buffer, offset, readLength);
        return 0;
    }

    @Nullable
    @Override
    public Uri getUri() {
        return uri;
    }


    @Override
    public void close() throws IOException {
        if (channel != null)
            channel.close();
    }
}
