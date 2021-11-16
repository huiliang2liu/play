package com.xh.image.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class BitmapViewTarget extends GlideViewTarget<Bitmap>{
    @SuppressWarnings("WeakerAccess")
    public BitmapViewTarget(View view) {
        super(view);
    }

    /**
     * @deprecated Use {@link #waitForLayout()} instead.
     */
    // Public API.
    @SuppressWarnings({"unused", "deprecation"})
    @Deprecated
    public BitmapViewTarget(View view, boolean waitForLayout) {
        super(view, waitForLayout);
    }

    /**
     * Sets the {@link android.graphics.Bitmap} on the view using {@link
     * android.widget.ImageView#setImageBitmap(android.graphics.Bitmap)}.
     *
     * @param resource The bitmap to display.
     */
    @Override
    protected void setResource(Bitmap resource) {
        view.setBackground(new BitmapDrawable(view.getResources(),resource));
    }
}
