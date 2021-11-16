package com.xh.image.glide;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawableViewTarget extends GlideViewTarget<Drawable>{
    public DrawableViewTarget(View view) {
        super(view);
    }

    /**
     * @deprecated Use {@link #waitForLayout()} instead.
     */
    // Public API.
    @SuppressWarnings({"unused", "deprecation"})
    @Deprecated
    public DrawableViewTarget(View view, boolean waitForLayout) {
        super(view, waitForLayout);
    }

    @Override
    protected void setResource(@Nullable Drawable resource) {
        view.setBackground(resource);
    }
}
