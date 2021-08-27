package com.http.asyn;

import com.loopj.android.http.AsyncHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.message.BasicHeader;


/**
 * com.http.asyn
 * 2018/10/18 16:40
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class RawEntity implements HttpEntity {
    private static final UnsupportedOperationException ERR_UNSUPPORTED =
            new UnsupportedOperationException("Unsupported operation in this implementation.");
    private final Header heard;
    private final byte[] mRaw;

    public RawEntity(byte[] raw, String type) {
        heard = new BasicHeader(
                AsyncHttpClient.HEADER_CONTENT_TYPE,
                String.format("application/%s", type));
        mRaw = raw;
    }

    @Override
    public void consumeContent() throws IOException {

    }


    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public long getContentLength() {
        return -1;
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public Header getContentType() {
        return heard;
    }

    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        throw ERR_UNSUPPORTED;
    }

    @Override
    public void writeTo(OutputStream out) throws IOException, IllegalStateException {
        if (out == null) {
            throw new IllegalStateException("Output stream cannot be null.");
        }
        if (mRaw != null)
            out.write(mRaw);
    }

}
