package com.xh.media.exoplayer;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ext.rtmp.RtmpDataSource;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.DataSchemeDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class ExoplayerDataSource implements DataSource {
    private static final String TAG = "ExoplayerDataSource";
    private static final String SCHEME_ASSET = "asset";
    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_RTMP = "rtmp";
    private static final String SCHEME_RTSP = "rtsp";
    private static final String SCHEME_RAW = RawResourceDataSource.RAW_RESOURCE_SCHEME;

    private final Context context;
    private final List<TransferListener> transferListeners;
    private DataSource fileDataSource;
    private DataSource assetDataSource;
    private DataSource contentDataSource;
    private DataSource rtmpDataSource;
    private DataSource rtspDataSource;
    private DataSource dataSchemeDataSource;
    private DataSource rawResourceDataSource;
    private DataSource dataSource;
    private final DataSource baseDataSource;

    public ExoplayerDataSource(Context context) {
        this.context = context;
        transferListeners = new ArrayList<>();
        baseDataSource = new DefaultHttpDataSourceFactory("media/exoplayer", null).createDataSource();
    }

    @Override
    public void addTransferListener(TransferListener transferListener) {
        transferListeners.add(transferListener);
        baseDataSource.addTransferListener(transferListener);
        if (fileDataSource != null)
            fileDataSource.addTransferListener(transferListener);
        if (assetDataSource != null)
            assetDataSource.addTransferListener(transferListener);
        if (contentDataSource != null)
            contentDataSource.addTransferListener(transferListener);
        if (rtmpDataSource != null)
            rtmpDataSource.addTransferListener(transferListener);
        if (dataSchemeDataSource != null)
            dataSchemeDataSource.addTransferListener(transferListener);
        if (rawResourceDataSource != null)
            rawResourceDataSource.addTransferListener(transferListener);
    }

    @Override
    public long open(DataSpec dataSpec) throws IOException {
        String scheme = dataSpec.uri.getScheme();
        Log.d(TAG, scheme);

        if (Util.isLocalFileUri(dataSpec.uri)) {
            if (dataSpec.uri.getPath().startsWith("/android_asset/")) {
                dataSource = getAssetDataSource();
            } else {
                dataSource = getFileDataSource();
            }
        } else if (SCHEME_ASSET.equals(scheme)) {
            dataSource = getAssetDataSource();
        } else if (SCHEME_CONTENT.equals(scheme)) {
            dataSource = getContentDataSource();
        } else if (SCHEME_RTMP.equals(scheme)) {
            dataSource = getRtmpDataSource();
        } else if (DataSchemeDataSource.SCHEME_DATA.equals(scheme)) {
            dataSource = getDataSchemeDataSource();
        } else if (SCHEME_RAW.equals(scheme)) {
            dataSource = getRawResourceDataSource();
        } else if (SCHEME_RTSP.equals(scheme)) {
            dataSource = getRtspDataSource();
        } else {
            Log.d(TAG,"baseDataSource");
            dataSource = baseDataSource;
        }
        // Open the source and return.
        return dataSource.open(dataSpec);
    }
    private DataSource getRtspDataSource() {
        Log.d(TAG,"getRtspDataSource");
        if (rtspDataSource == null) {
            rtspDataSource = new RtspDataSource();
            addListenersToDataSource(rtspDataSource);
        }
        return rtspDataSource;
    }

    private DataSource getRtmpDataSource() {
        Log.d(TAG,"getRtmpDataSource");
        if (rtmpDataSource == null) {
            rtmpDataSource = new RtmpDataSource();
            addListenersToDataSource(rtmpDataSource);
        }
        return rtmpDataSource;
    }

    private DataSource getFileDataSource() {
        Log.d(TAG,"getFileDataSource");
        if (fileDataSource == null) {
            fileDataSource = new FileDataSource();
            addListenersToDataSource(fileDataSource);
        }
        return fileDataSource;
    }

    private DataSource getContentDataSource() {
        Log.d(TAG,"getContentDataSource");
        if (contentDataSource == null) {
            contentDataSource = new ContentDataSource(context);
            addListenersToDataSource(contentDataSource);
        }
        return contentDataSource;
    }

    private DataSource getAssetDataSource() {
        Log.d(TAG,"getAssetDataSource");
        if (assetDataSource == null) {
            assetDataSource = new AssetDataSource(context);
            addListenersToDataSource(assetDataSource);
        }
        return assetDataSource;
    }

    private DataSource getDataSchemeDataSource() {
        Log.d(TAG,"getDataSchemeDataSource");
        if (dataSchemeDataSource == null) {
            dataSchemeDataSource = new DataSchemeDataSource();
            addListenersToDataSource(dataSchemeDataSource);
        }
        return dataSchemeDataSource;
    }

    private DataSource getRawResourceDataSource() {
        Log.d(TAG,"getRawResourceDataSource");
        if (rawResourceDataSource == null) {
            rawResourceDataSource = new RawResourceDataSource(context);
            addListenersToDataSource(rawResourceDataSource);
        }
        return rawResourceDataSource;
    }

    private void addListenersToDataSource(DataSource dataSource) {
        for (int i = 0; i < transferListeners.size(); i++) {
            dataSource.addTransferListener(transferListeners.get(i));
        }
    }

    @Override
    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        if (dataSource != null)
            return dataSource.read(buffer, offset, readLength);
        return 0;
    }

    @Nullable
    @Override
    public Uri getUri() {
        if (dataSource != null)
            return dataSource.getUri();
        return null;
    }

    @Override
    public Map<String, List<String>> getResponseHeaders() {
        if (dataSource == null)
            return Collections.emptyMap();
        return dataSource.getResponseHeaders();
    }

    @Override
    public void close() throws IOException {
        if (dataSource != null)
            dataSource.close();
    }
}
