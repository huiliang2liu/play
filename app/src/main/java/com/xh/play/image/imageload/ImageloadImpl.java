package com.xh.play.image.imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.xh.play.image.AImageLoad;
import com.xh.play.image.transform.ITransform;

import java.io.File;

public class ImageloadImpl extends AImageLoad {
    private static final int CACHE_SIZE = 1024 * 1024 * 100;
    protected ImageLoader mImageLoader;
    private File cacheDir;
    private Bitmap.Config config;

    public ImageloadImpl(Bitmap.Config config, Context context, String path) {
        if (path == null || path.isEmpty())
            path = context.getExternalFilesDir(null).getParent() + "/imageload";
        cacheDir = new File(path);
        DisplayImageOptions.Builder builder11 = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(config).cacheInMemory(false)
                .cacheOnDisk(true);
        this.config = config;
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        builder.threadPoolSize(5); // 线程池大小
        builder.threadPriority(Thread.NORM_PRIORITY - 2); // 设置线程优先级
        builder.denyCacheImageMultipleSizesInMemory(); // 不允许在内存中缓存同一张图片的多个尺寸
        builder.tasksProcessingOrder(QueueProcessingType.LIFO); // 设置处理队列的类型，包括LIFO， FIFO
        builder.memoryCache(new LruMemoryCache(3 * 1024 * 1024)); // 内存缓存策略
        builder.memoryCacheSize(5 * 1024 * 1024);  // 内存缓存大小
        builder.memoryCacheExtraOptions(480, 800); // 内存缓存中每个图片的最大宽高
        builder.memoryCacheSizePercentage(50); // 内存缓存占总内存的百分比

        builder.diskCache(new UnlimitedDiscCache(cacheDir)); // 设置磁盘缓存策略
        builder.diskCacheSize(CACHE_SIZE); // 设置磁盘缓存的大小
        builder.diskCacheFileCount(50); // 磁盘缓存文件数量
        builder.diskCacheFileNameGenerator(new Md5FileNameGenerator()); // 磁盘缓存时图片名称加密方式
        builder.imageDownloader(new BaseImageDownloader(context)); // 图片下载器
        builder.defaultDisplayImageOptions(builder11.build());
        builder.writeDebugLogs(); // 打印日志
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(builder.build());
    }

    @Override
    public void load(int defaultImg, int error, String path, ImageView imageView, ITransform transform) {
        mImageLoader.displayImage(path, imageView, options(defaultImg, error, transform));
    }

    @Override
    public void loadBg(int defaultImg, int error, String path, View view, ITransform transform) {
        mImageLoader.displayImage(path, new ImageloadViewAware(view), options(defaultImg, error, transform));
    }

    private DisplayImageOptions options(int defaultImg, int error, ITransform transform) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        if (error > 0) {
            builder.showImageOnFail(error);
            builder.showImageForEmptyUri(error);
        }
        if (defaultImg > 0)
            builder.showImageOnLoading(defaultImg);
        builder.cacheOnDisk(true)
                .cacheInMemory(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(config);
        builder.displayer(new ImageloadTransform(transform));
        return builder.build();
    }
}
