package com.xh.play.media.exoplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.xh.play.media.IReplayMedia;
import com.xh.play.media.MediaEntity;
import com.xh.play.media.MediaListener;

import java.util.ArrayList;
import java.util.List;

public class ExoMediaImpl implements IReplayMedia, VideoListener, Player.EventListener {
    private static final String TAG = "ExoMediaImpl";
    private MediaListener listener;
    private SimpleExoPlayer player;
    private Context context;
    private boolean prepared = false;

    public ExoMediaImpl(Context context) {
        this.context = context;
        DefaultRenderersFactory rendererFactory = new DefaultRenderersFactory(context,
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);
        TrackSelector trackSelector = new DefaultTrackSelector();
        DefaultLoadControl.Builder builder = new DefaultLoadControl.Builder();
        builder.setAllocator(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE));
        builder.setBufferDurationsMs(
                2000,
                15000,
                1500,
                0
        );
        final LoadControl loadControl = builder.createDefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(context, rendererFactory, trackSelector, loadControl);
        player.addVideoListener(this);
        player.addListener(this);
    }

    @Override
    public void setMedialistener(MediaListener listener) {
        this.listener = listener;
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        player.setVideoSurface(holder.getSurface());
    }

    @Override
    public void pause() {
        player.setPlayWhenReady(false);
    }

    @Override
    public boolean isPlay() {
        return player.getPlayWhenReady();
    }

    @Override
    public void play() {
        player.setPlayWhenReady(true);
    }

    @Override
    public void setPath(String path) {
        player.prepare(createSource(path));
    }

    @Override
    public void setPath(MediaEntity... entities) {
        if (entities == null || entities.length <= 0) {
            Log.d(TAG, "entities is empty");
            return;
        }
        MediaSource source = createSource(entities);
        if (source == null) {
            Log.d(TAG, "source is null");
            return;
        }
        player.prepare(source);
    }

    private MediaSource createSource(MediaEntity... entities) {
        if (entities.length == 1) {
            return entity2source(entities[0]);
        }
        List<MediaSource> list = new ArrayList<>(entities.length);
        for (MediaEntity entity : entities) {
            MediaSource source = entity2source(entity);
            if (source != null)
                list.add(source);
        }
        if (list.size() > 0) {
            return new ConcatenatingMediaSource(list.toArray(new MediaSource[list.size()]));
        }
        return null;
    }

    private MediaSource entity2source(MediaEntity entity) {
        String path = entity.getUrl();
        int time = entity.getPlayTimes();
        if (path == null || path.isEmpty()) {
            Log.d(TAG, "path is empty");
            return null;
        }
        MediaSource source = createSource(path);
        if (time <= 1) {
            return source;
        }
        return new LoopingMediaSource(source, time);
    }

    private MediaSource createSource(String path) {
        DataSource.Factory dataSourceFactory =
                new DataSourceFactory(context);
        Uri uri = Uri.parse(path);
        int type = Util.inferContentType(uri);
        MediaSource videoSource = null;
        if (type == C.TYPE_DASH)
            videoSource = new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        else if (type == C.TYPE_SS)
            videoSource = new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        else if (type == C.TYPE_HLS)
            videoSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        else
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        return videoSource;
    }

    @Override
    public long getDuration() {
        return player.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(long seek) {
        player.seekTo(seek);
    }

    @Override
    public void setSpeed(float speed) {
        player.setPlaybackParameters(new PlaybackParameters(speed));
    }

    @Override
    public void destroy() {
        player.stop();
        player.release();
    }

    @Override
    public void stop() {
        player.stop();
    }

    @Override
    public void reset() {
        player.retry();
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        if (listener != null)
            listener.onVideoSizeChanged(width, height);
    }

    @Override
    public void onSurfaceSizeChanged(int width, int height) {

    }

    @Override
    public void onRenderedFirstFrame() {
        if (listener != null)
            listener.onInfo(MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START, 0);
    }


    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }


    @Override
    public void onLoadingChanged(boolean isLoading) {
        if (listener != null && isLoading && !prepared) {
            prepared = true;
            listener.onPrepared();
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (!playWhenReady)
            return;
        if (playbackState == Player.STATE_BUFFERING) {
            if (listener != null)
                listener.onInfo(MediaPlayer.MEDIA_INFO_BUFFERING_START, 0);
        } else if (playbackState == Player.STATE_READY) {
            if (listener != null)
                listener.onInfo(MediaPlayer.MEDIA_INFO_BUFFERING_END, 0);
        } else if (playbackState == Player.STATE_ENDED) {
            if (listener != null)
                listener.onCompletion();
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (listener != null)
            listener.onError(error.type, error.rendererIndex);
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
