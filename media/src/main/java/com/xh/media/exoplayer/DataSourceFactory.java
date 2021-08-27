package com.xh.media.exoplayer;

import android.content.Context;

import com.google.android.exoplayer2.upstream.DataSource;

public class DataSourceFactory implements DataSource.Factory {
    private Context context;

    DataSourceFactory(Context context) {
        this.context = context;
    }

    @Override
    public DataSource createDataSource() {
        return new ExoplayerDataSource(context);
    }
}
