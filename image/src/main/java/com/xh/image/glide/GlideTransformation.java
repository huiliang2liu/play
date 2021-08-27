package com.xh.image.glide;//package com.xh.play.image.glide;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//
//import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
//import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
//import com.image.transform.ITransform;
//
//public class GlideTransformation extends BitmapTransformation {
//    private Context context;
//    private String path;
//    private ITransform transform;
//
//    public GlideTransformation(Context context, String path, ITransform transform) {
//        super(context);
//        this.context = context;
//        this.path = path;
//        this.transform = transform;
//    }
//
//    @Override
//    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//        if (transform != null)
//            toTransform = transform.transform(toTransform);
//        return toTransform;
//    }
//
//    @Override
//    public String getId() {
//        return hashCode() + "";
//    }
//}
