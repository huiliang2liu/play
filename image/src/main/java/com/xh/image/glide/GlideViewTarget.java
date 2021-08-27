package com.xh.image.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

public class GlideViewTarget extends ViewTarget<View, Bitmap> implements Transition.ViewAdapter {

    public GlideViewTarget(View view) {
        super(view);
    }

    @Override
    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
        if (transition == null || !transition.transition(resource, this)) {
            setResourceInternal(resource);
        } else {
            maybeUpdateAnimatable(resource);
        }
    }

    @Nullable
    @Override
    public Drawable getCurrentDrawable() {
        return view.getBackground();
    }

    @Override
    public void setDrawable(Drawable drawable) {
        view.setBackground(drawable);
    }

    private void setResourceInternal(@Nullable Bitmap resource) {
        // Order matters here. Set the resource first to make sure that the Drawable has a valid and
        // non-null Callback before starting it.
        setResource(resource);
        maybeUpdateAnimatable(resource);
    }

    private void maybeUpdateAnimatable(@Nullable Bitmap resource) {

    }

    protected void setResource(Bitmap resource) {
        setDrawable(new BitmapDrawable(view.getResources(), resource));
    }
}
