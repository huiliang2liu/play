package com.xh.play.image.picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.squareup.picasso.Transformation;
import com.xh.play.image.transform.ITransform;

public class PicassoTransformation implements Transformation {
    private ITransform transform;
    private View view;
    private String path;
    private Bitmap bitmap;
    private Handler handler = new Handler(Looper.getMainLooper());

    public PicassoTransformation(ITransform transform, View view, String path) {
        this.transform = transform;
        this.view = view;
        this.path = path;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        bitmap = source;
        int mWidth = source.getWidth();
        int mHeigth = source.getHeight();
        bitmap = Bitmap.createBitmap(mWidth, mHeigth, source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(source,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        canvas.drawRect(0, 0, mWidth, mHeigth, paint);
        source.recycle();
        if (transform != null)
            bitmap = transform.transform(bitmap);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    if (Build.VERSION.SDK_INT > 15) {
                        view.setBackground(new BitmapDrawable(view.getResources(), bitmap));
                    } else
                        view.setBackgroundDrawable(new BitmapDrawable(view.getResources(), bitmap));
                }
            }
        });
        return bitmap;
    }

    @Override
    public String key() {
        return path;
    }
}
