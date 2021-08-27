package com.xh.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * 2018/7/2 10:44
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class ImageUtil {
    /**
     * 2018/7/2 10:46
     * annotation：获取布局的高度
     * author：liuhuiliang
     * email ：825378291@qq.com
     */
    public static int getViewHeight(View view, int defaultHeigth) {
        // TODO Auto-generated method stub
        if (view == null)
            return defaultHeigth;
        int width = 0;
        width = view.getHeight();
        if (width < 0) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params != null) {
                width = params.height;
                if (width < 0)
                    width = getValue(view, "mMaxHeight");
            }
        }
        if (width <= 0)
            width = defaultHeigth;
        if (width <= 0) {
            DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
            width = dm.heightPixels;
        }

        return width;
    }

    public static Bitmap src(int heigth, int width, int src, Context context) {
        if (heigth <= 0 && width <= 0)
            return src(src, context);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不加载图片，只是解析图片
        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(),
                src, opts);
        int bHeigth = opts.outHeight;
        int bWidth = opts.outWidth;
        int multiple = 0;
        if (heigth > 0 && width > 0) {
            int heightScale = bHeigth / heigth;
            int widthScale = bWidth / width;
            multiple = heightScale > widthScale ? widthScale : heightScale;
        } else if (heigth > 0)
            multiple = bHeigth / heigth;
        else
            multiple = bWidth / width;
        opts.inSampleSize = multiple > 1 ? multiple : 1;
        opts.inJustDecodeBounds = false;
        bitmap1 = BitmapFactory.decodeResource(context.getResources(), src,
                opts);

        return bitmap1;
    }

    /**
     * 获取 src中的图片
     *
     * @param src
     * @param context
     * @return
     */
    public static Bitmap src(int src, Context context) {
        return BitmapFactory.decodeResource(context.getResources(), src);
    }

    /**
     * 2018/7/2 10:47
     * annotation：获取布局的宽度
     * author：liuhuiliang
     * email ：825378291@qq.com
     */
    public static int getViewWidth(View view, int defaultWidth) {
        // TODO Auto-generated method stub
        if (view == null)
            return defaultWidth;
        int width = 0;
        width = view.getWidth();
        if (width < 0) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params != null) {
                width = params.width;
                if (width < 0)
                    width = getValue(view, "mMaxWidth");
            }
        }
        if (width <= 0)
            width = defaultWidth;
        if (width <= 0) {
            DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
            width = dm.widthPixels;
        }

        return width;
    }

    private static int getValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            if (!field.isAccessible())
                field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
                value = fieldValue;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return value;
    }

    /**
     * 2018/7/2 10:48
     * annotation：drawable转换位bitmap
     * author：liuhuiliang
     * email ：825378291@qq.com
     */
    public static Bitmap drawable2bitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        return drawable2bitmap(drawable, width, height);

    }
    /**
     * 用于图片精确缩放 不变形
     *
     * @param heigth
     *            缩放后的高度
     * @param width
     *            缩放后的宽度
     * @param bmp
     *            所要缩放的图片
     * @return
     */
    public static Bitmap zoom(int heigth, int width, Bitmap bmp) {
        if (heigth <= 0 && width <= 0)
            return bmp;
        if (bmp == null) {
            return null;
        }
        Matrix matrix = new Matrix(); // 矩阵，用于图片比例缩放
        float zoom = 0;
        if (heigth > 0 && width > 0) {
            float mW = width * 1.0f / bmp.getWidth();
            float mH = heigth * 1.0f / bmp.getHeight();
            zoom = mW > mH ? mW : mH;
        } else if (heigth > 0)
            zoom = heigth * 1.0f / bmp.getHeight();
        else
            zoom = width * 1.0f / bmp.getWidth();
        matrix.postScale(zoom, zoom); // 设置高宽
        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
                matrix, true);
        return bmp;
    }

    /**
     * 用于图片精确缩放 变形
     *
     * @param heigth
     *            缩放后的高度
     * @param width
     *            缩放后的宽度
     * @param bmp
     *            所要缩放的图片
     * @return
     */
    public static Bitmap zoom1(int heigth, int width, Bitmap bmp) {
        if (heigth <= 0 && width <= 0)
            return bmp;
        if (bmp == null) {
            return null;
        }
        bmp = zoom(heigth, width, bmp);
        Matrix matrix = new Matrix(); // 矩阵，用于图片比例缩放
        matrix.postScale(width * 1.0f / bmp.getWidth(),
                heigth * 1.0f / bmp.getHeight()); // 设置高宽
        bmp = Bitmap.createBitmap(bmp, 0, 0, width, heigth, matrix, true);
        return bmp;
    }
    /**
     * 2018/7/2 10:48
     * annotation：drawable转换位bitmap
     * author：liuhuiliang
     * email ：825378291@qq.com
     */
    public static Bitmap drawable2bitmap(Drawable drawable, int width,
                                         int heigth) {
        if (width <= 0)
            width = drawable.getIntrinsicWidth();
        if (heigth <= 0)
            heigth = drawable.getIntrinsicHeight();
        if (width <= 0 || heigth <= 0)
            throw new RuntimeException("width<=0 or heigth<=0");
        Bitmap bitmap = Bitmap.createBitmap(width, heigth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, heigth);
        drawable.draw(canvas);
        return bitmap;

    }
    /**
     * 获得带倒影的图片方法
     *
     * @param bitmap
     * @return
     */
    public static Bitmap reflection_with_origin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        // Paint deafalutPaint = new Paint();
        // canvas.drawRect(0, height, width, height + reflectionGap,
        // deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 怀旧处理
     *
     * @param bmp
     * @return
     */
    public static Bitmap nostalgia(Bitmap bmp) {
		/*
		 * 怀旧处理算法即设置新的RGB R=0.393r+0.769g+0.189b G=0.349r+0.686g+0.168b
		 * B=0.272r+0.534g+0.131b
		 */
        float[] fs = { 0.393f, 0.769f, 0.189f, 0, 0, 0.349f, 0.686f, 0.168f, 0,
                0, 0.272f, 0.534f, 0.131f, 0, 0, 0, 0, 0, 1, 0 };
        return fs(bmp, fs);
    }

    /**
     * 浮雕处理
     *
     * @param bmp
     * @return
     */
    public static Bitmap relief(Bitmap bmp) {
		/*
		 * 算法原理：(前一个像素点RGB-当前像素点RGB+127)作为当前像素点RGB值 在ABC中计算B点浮雕效果(RGB值在0~255)
		 * B.r = C.r - B.r + 127 B.g = C.g - B.g + 127 B.b = C.b - B.b + 127
		 */
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1; i < height - 1; i++) {
            for (int k = 1; k < width - 1; k++) {
                // 获取前一个像素颜色
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                // 获取当前像素
                pixColor = pixels[(width * i + k) + 1];
                newR = Color.red(pixColor) - pixR + 127;
                newG = Color.green(pixColor) - pixG + 127;
                newB = Color.blue(pixColor) - pixB + 127;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[width * i + k] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 模糊处理
     *
     * @param bmp
     * @return
     */
    public static Bitmap fuzzy(Bitmap bmp) {
		/*
		 * 算法原理： 简单算法将像素周围八个点包括自身共九个点RGB值分别相加后平均,当前像素点的RGB值 复杂算法采用高斯模糊 高斯矩阵
		 * int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
		 * 将九个点的RGB值分别与高斯矩阵中的对应项相乘的和,再除以一个相应的值作为当前像素点的RGB
		 */
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 }; // 高斯矩阵
        int delta = 16; // 除以值 值越小图片会越亮,越大则越暗
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR, newG, newB;
        int pos = 0; // 位置
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        // 循环赋值
        for (int i = 1; i < height - 1; i++) {
            for (int k = 1; k < width - 1; k++) {
                pos = 0;
                newR = 0;
                newG = 0;
                newB = 0;
                for (int m = -1; m <= 1; m++) // 宽不变
                {
                    for (int n = -1; n <= 1; n++) // 高先变
                    {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        // 3*3像素相加
                        newR = newR + (int) (pixR * gauss[pos]);
                        newG = newG + (int) (pixG * gauss[pos]);
                        newB = newB + (int) (pixB * gauss[pos]);
                        pos++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 光照效果
     *
     * @param bmp
     * @return
     */
    public static Bitmap sunshine(Bitmap bmp, int ligth_x, int ligth_y) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        // 围绕圆形光照
        int radius = Math.min(ligth_x, ligth_y);
        float strength = 150F; // 光照强度100-150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1; i < height - 1; i++) {
            for (int k = 1; k < width - 1; k++) {
                // 获取前一个像素颜色
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = pixR;
                newG = pixG;
                newB = pixB;
                // 计算当前点到光照中心的距离,平面坐标系中两点之间的距离
                int distance = (int) (Math.pow((ligth_y - i), 2) + Math.pow(
                        (ligth_x - k), 2));
                if (distance < radius * radius) {
                    // 按照距离大小计算增强的光照值
                    int result = (int) (strength * (1.0 - Math.sqrt(distance)
                            / radius));
                    newR = pixR + result;
                    newG = newG + result;
                    newB = pixB + result;
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[width * i + k] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 锐化处理
     *
     * @param bmp
     * @return
     */
    public static Bitmap sharpen(Bitmap bmp) {
		/*
		 * 锐化基本思想是加强图像中景物的边缘和轮廓,使图像变得清晰 而图像平滑是使图像中边界和轮廓变得模糊
		 *
		 * 拉普拉斯算子图像锐化 获取周围9个点的矩阵乘以模板9个的矩阵 卷积
		 */
        // 拉普拉斯算子模板 { 0, -1, 0, -1, -5, -1, 0, -1, 0 } { -1, -1, -1, -1, 9, -1,
        // -1, -1, -1 }
        int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int idx = 0;
        float alpha = 0.3F; // 图片透明度
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        // 图像处理
        for (int i = 1; i < height - 1; i++) {
            for (int k = 1; k < width - 1; k++) {
                idx = 0;
                newR = 0;
                newG = 0;
                newB = 0;
                for (int n = -1; n <= 1; n++) // 取出图像3*3领域像素
                {
                    for (int m = -1; m <= 1; m++) // n行数不变 m列变换
                    {
                        pixColor = pixels[(i + n) * width + k + m]; // 当前点(i,k)
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        // 图像像素与对应摸板相乘
                        newR = newR + (int) (pixR * laplacian[idx] * alpha);
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);
                        idx++;
                    }
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                // 赋值
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 冰冻处理
     *
     * @param bmp
     * @return
     */
    public static Bitmap ice(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                // 获取前一个像素颜色
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                // 红色
                newColor = pixR - pixG - pixB;
                newColor = newColor * 3 / 2;
                if (newColor < 0) {
                    newColor = -newColor;
                }
                if (newColor > 255) {
                    newColor = 255;
                }
                newR = newColor;
                // 绿色
                newColor = pixG - pixB - pixR;
                newColor = newColor * 3 / 2;
                if (newColor < 0) {
                    newColor = -newColor;
                }
                if (newColor > 255) {
                    newColor = 255;
                }
                newG = newColor;
                // 蓝色
                newColor = pixB - pixG - pixR;
                newColor = newColor * 3 / 2;
                if (newColor < 0) {
                    newColor = -newColor;
                }
                if (newColor > 255) {
                    newColor = 255;
                }
                newB = newColor;
                pixels[width * i + k] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 素描处理
     *
     * @param bmp
     * @return
     */
    public static Bitmap sketch(Bitmap bmp) {
        // 创建新Bitmap
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height]; // 存储变换图像
        int[] linpix = new int[width * height]; // 存储灰度图像
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        // 灰度图像
        for (int i = 1; i < height - 1; i++)
            // 拉普拉斯算子模板 { 0, -1, 0, -1, -5,
            for (int j = 1; j < width - 1; j++) {
                // -1, 0, -1, 0
                {
                    // 获取前一个像素颜色
                    pixColor = pixels[width * i + j];
                    pixR = Color.red(pixColor);
                    pixG = Color.green(pixColor);
                    pixB = Color.blue(pixColor);
                    // 灰度图像
                    int gray = (int) (0.3 * pixR + 0.59 * pixG + 0.11 * pixB);
                    linpix[width * i + j] = Color.argb(255, gray, gray, gray);
                    // 图像反向
                    gray = 255 - gray;
                    pixels[width * i + j] = Color.argb(255, gray, gray, gray);
                }
            }
        int[] copixels = gaussBlur(pixels, width, height, 10, 10 / 3); // 高斯模糊
        // 采用半径10
        int[] result = colorDodge(linpix, copixels); // 素描图像 颜色减淡
        bitmap.setPixels(result, 0, width, 0, 0, width, height);
        return bitmap;
    }

    // 高斯模糊
    public static int[] gaussBlur(int[] data, int width, int height,
                                  int radius, float sigma) {

        float pa = (float) (1 / (Math.sqrt(2 * Math.PI) * sigma));
        float pb = -1.0f / (2 * sigma * sigma);

        // generate the Gauss Matrix
        float[] gaussMatrix = new float[radius * 2 + 1];
        float gaussSum = 0f;
        for (int i = 0, x = -radius; x <= radius; ++x, ++i) {
            float g = (float) (pa * Math.exp(pb * x * x));
            gaussMatrix[i] = g;
            gaussSum += g;
        }

        for (int i = 0, length = gaussMatrix.length; i < length; ++i) {
            gaussMatrix[i] /= gaussSum;
        }

        // x direction
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                float r = 0, g = 0, b = 0;
                gaussSum = 0;
                for (int j = -radius; j <= radius; ++j) {
                    int k = x + j;
                    if (k >= 0 && k < width) {
                        int index = y * width + k;
                        int color = data[index];
                        int cr = (color & 0x00ff0000) >> 16;
                        int cg = (color & 0x0000ff00) >> 8;
                        int cb = (color & 0x000000ff);

                        r += cr * gaussMatrix[j + radius];
                        g += cg * gaussMatrix[j + radius];
                        b += cb * gaussMatrix[j + radius];

                        gaussSum += gaussMatrix[j + radius];
                    }
                }

                int index = y * width + x;
                int cr = (int) (r / gaussSum);
                int cg = (int) (g / gaussSum);
                int cb = (int) (b / gaussSum);

                data[index] = cr << 16 | cg << 8 | cb | 0xff000000;
            }
        }

        // y direction
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                float r = 0, g = 0, b = 0;
                gaussSum = 0;
                for (int j = -radius; j <= radius; ++j) {
                    int k = y + j;
                    if (k >= 0 && k < height) {
                        int index = k * width + x;
                        int color = data[index];
                        int cr = (color & 0x00ff0000) >> 16;
                        int cg = (color & 0x0000ff00) >> 8;
                        int cb = (color & 0x000000ff);

                        r += cr * gaussMatrix[j + radius];
                        g += cg * gaussMatrix[j + radius];
                        b += cb * gaussMatrix[j + radius];

                        gaussSum += gaussMatrix[j + radius];
                    }
                }

                int index = y * width + x;
                int cr = (int) (r / gaussSum);
                int cg = (int) (g / gaussSum);
                int cb = (int) (b / gaussSum);
                data[index] = cr << 16 | cg << 8 | cb | 0xff000000;
            }
        }

        return data;
    }

    // 颜色减淡
    public static int[] colorDodge(int[] baseColor, int[] mixColor) {

        for (int i = 0, length = baseColor.length; i < length; ++i) {
            int bColor = baseColor[i];
            int br = (bColor & 0x00ff0000) >> 16;
            int bg = (bColor & 0x0000ff00) >> 8;
            int bb = (bColor & 0x000000ff);

            int mColor = mixColor[i];
            int mr = (mColor & 0x00ff0000) >> 16;
            int mg = (mColor & 0x0000ff00) >> 8;
            int mb = (mColor & 0x000000ff);

            int nr = colorDodgeFormular(br, mr);
            int ng = colorDodgeFormular(bg, mg);
            int nb = colorDodgeFormular(bb, mb);

            baseColor[i] = nr << 16 | ng << 8 | nb | 0xff000000;
        }
        return baseColor;
    }

    private static int colorDodgeFormular(int base, int mix) {

        int result = base + (base * mix) / (255 - mix);
        result = result > 255 ? 255 : result;
        return result;

    }

    /**
     *
     * lhl 2017-12-20 下午12:38:49 说明：去色效果
     *
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap quse(Bitmap bitmap) {
        float[] fs = { 1.5f, 1.5f, 1.5f, 0, -1, 1.5f, 1.5f, 1.5f, 0, -1, 1.5f,
                1.5f, 1.5f, 0, -1, 0, 0, 0, 1, 0 };
        return fs(bitmap, fs);
    }

    /**
     *
     * lhl 2017-12-20 下午12:41:09 说明：高饱和度
     *
     * @return Bitmap
     */
    public static Bitmap highDegreeSaturation(Bitmap bitmap) {
        float[] fs = { 1.438f, -.122f, -.016f, 0, -.03f, -.062f, 1.378f,
                -.016f, 0, .05f, -.062f, -.122f, 1.438f, 0, -.02f, 0, 0, 0, 1,
                0 };
        return fs(bitmap, fs);
    }

    /**
     *
     * lhl 2017-12-20 下午12:41:09 说明：底片
     *
     * @return Bitmap
     */
    public static Bitmap film(Bitmap bitmap) {
        float[] fs = { -1, 0, 0, 0, 255, 0, -1, 0, 0, 255, 0, 0, -1, 0, 255, 0,
                0, 0, 1, 0 };
        return fs(bitmap, fs);
    }

    // /**
    // *
    // * lhl
    // * 2017-12-20 下午12:41:09
    // * 说明：底片
    // * @return Bitmap
    // */
    // public static Bitmap film1(Bitmap bitmap){
    // int width=bitmap.getWidth();
    // int height=bitmap.getHeight();
    // int len=width*height;
    // int oldPx[]=new int[len];
    // int newPx[]=new int[len];
    // int a;
    // int r;
    // int g;
    // int b;
    // bitmap.getPixels(oldPx, 0, width, 0, 0, width, height);
    // for (int i = 0; i < len; i++) {
    // int color=oldPx[i];
    // a=Color.alpha(color);
    // r=Color.red(color);
    // g=Color.green(color);
    // b=Color.blue(color);
    // r=255-r;
    // g=255-g;
    // b=255-b;
    // if(r>255)
    // r=255;
    // else if(r<0)
    // r=0;
    //
    // if(g>255)
    // g=255;
    // else if(g<0)
    // g=0;
    //
    // if(b>255)
    // b=255;
    // else if(b<0)
    // b=0;
    // newPx[i]=Color.argb(a, r, g, b);
    // }
    // Bitmap bmp=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    // bmp.setPixels(newPx, 0, width, 0, 0, width, height);
    // return bmp;
    // }
    private static Bitmap fs(Bitmap bmp, float[] fs) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(fs);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(bmp, 0, 0, paint);
        return bitmap;
    }

    /**
     * 图片合成
     *
     * @param bm
     * @param bmp
     */
    public static Bitmap addFrameToImage(Bitmap bm, Bitmap bmp) // bmp原图(前景)
    // bm资源图片(背景)
    {
        Bitmap drawBitmap = Bitmap.createBitmap(bmp.getWidth(),
                bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(drawBitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(bmp, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.LIGHTEN));
        // 对边框进行缩放
        int w = bm.getWidth();
        int h = bm.getHeight();
        // 缩放比 如果图片尺寸超过边框尺寸 会自动匹配
        float scaleX = bmp.getWidth() * 1F / w;
        float scaleY = bmp.getHeight() * 1F / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY); // 缩放图片
        Bitmap copyBitmap = Bitmap.createBitmap(bm, 0, 0, w, h, matrix, true);
        canvas.drawBitmap(copyBitmap, 0, 0, paint);
        return drawBitmap;
    }

    /**
     * 图片合成
     *
     * @param frameBitmap
     * @param bmp
     * @return
     */
    public static Bitmap addFrameToImageTwo(Bitmap frameBitmap, Bitmap bmp) // bmp原图
    // frameBitmap资源图片(边框)
    {
        // bmp原图 创建新位图
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap drawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 对边框进行缩放
        int w = frameBitmap.getWidth();
        int h = frameBitmap.getHeight();
        float scaleX = width * 1F / w; // 缩放比 如果图片尺寸超过边框尺寸 会自动匹配
        float scaleY = height * 1F / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY); // 缩放图片
        Bitmap copyBitmap = Bitmap.createBitmap(frameBitmap, 0, 0, w, h,
                matrix, true);

        int pixColor = 0;
        int layColor = 0;
        int newColor = 0;

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixA = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int newA = 0;

        int layR = 0;
        int layG = 0;
        int layB = 0;
        int layA = 0;

        float alpha = 0.8F;
        float alphaR = 0F;
        float alphaG = 0F;
        float alphaB = 0F;

        for (int i = 0; i < width; i++) {
            for (int k = 0; k < height; k++) {
                pixColor = bmp.getPixel(i, k);
                layColor = copyBitmap.getPixel(i, k);
                // 获取原图片的RGBA值
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                pixA = Color.alpha(pixColor);
                // 获取边框图片的RGBA值
                layR = Color.red(layColor);
                layG = Color.green(layColor);
                layB = Color.blue(layColor);
                layA = Color.alpha(layColor);
                // 颜色与纯黑色相近的点
                if (layR < 20 && layG < 20 && layB < 20) {
                    alpha = 1F;
                } else {
                    alpha = 0.3F;
                }
                alphaR = alpha;
                alphaG = alpha;
                alphaB = alpha;
                // 两种颜色叠加
                newR = (int) (pixR * alphaR + layR * (1 - alphaR));
                newG = (int) (pixG * alphaG + layG * (1 - alphaG));
                newB = (int) (pixB * alphaB + layB * (1 - alphaB));
                layA = (int) (pixA * alpha + layA * (1 - alpha));
                // 值在0~255之间
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                newA = Math.min(255, Math.max(0, layA));
                // 绘制
                newColor = Color.argb(newA, newR, newG, newB);
                drawBitmap.setPixel(i, k, newColor);
            }
        }
        return drawBitmap;
    }

    /**
     * 压缩图片到指定大小或以下
     *
     * @return
     */
    @SuppressLint("NewApi")
    public static Bitmap compression(long size, String url) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(url, opts);
        return compression(size, bitmap);
    }

    /**
     * 压缩图片到指定大小
     *
     * @param size
     * @param bitmap
     * @return
     */
    @SuppressLint("NewApi")
    public static Bitmap compression(long size, Bitmap bitmap) {
        int bs = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            bs = bitmap.getByteCount();
        } else
            bs = bitmap.getRowBytes() * bitmap.getHeight();
        int proportion = 1;
        while (bs >= size * proportion) {
            proportion = proportion << 1;
        }
        if (proportion == 1)
            return bitmap;
        Matrix matrix = new Matrix();
        float p = proportion * 0.5f;
        float sx = 1 / p;
        float sy = 1 / p;
        matrix.postScale(sx, sy);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }
    /**
     * 获取圆角图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap rounded_bitmap(Bitmap bitmap, float roundX, float roundY) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, roundX, roundY, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap circle_bitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int heigth = bitmap.getHeight();
        return circle_bitmap(bitmap, 0, 0,
                (width > heigth ? heigth : width) >> 1);
    }

    /**
     * 获得圆形图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap circle_bitmap(Bitmap bitmap, int start_x, int start_y,
                                       int r) {
        Bitmap out = load_figure(bitmap, start_x, start_y, r << 1, r << 1);
        int width = out.getWidth();
        Log.e("circle_bitmap","width="+out.getWidth()+"  heigth="+out.getHeight()+"   r="+r);
        Bitmap output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, width);
        paint.setAntiAlias(true);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 截图 xh 2017-3-7 上午11:26:59
     *
     * @param
     * @param start_x
     * @param start_y
     * @param width
     * @param height
     * @return
     */
    public static Bitmap load_figure(Bitmap bitmap, int start_x, int start_y,
                                     int width, int height) {
        int b_heigth = bitmap.getHeight();
        int b_width = bitmap.getWidth();
        if (start_x < 0 || start_x >= b_width)
            start_x = 0;
        if (start_y < 0 || start_y >= b_heigth)
            start_y = 0;
        if (width <= 0 || width + start_x > b_width)
            width = b_width - start_x;
        if (height <= 0 || height + start_y > b_heigth)
            height = b_heigth - start_y;
        return Bitmap.createBitmap(bitmap, start_x, start_y, width, height);
    }

    /**
     * 心形位图 xh 2017-3-7 下午12:06:40
     *
     * @param bitmap
     * @return
     */
    public static Bitmap heart_bitmap(Bitmap bitmap) {
        return heart_bitmap(bitmap, 0, 0,
                bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight()
                        : bitmap.getWidth());
    }

    public static Bitmap heart_bitmap(Bitmap bitmap, int start_x, int start_y,
                                      int width) {
        Bitmap bitmap2 = load_figure(bitmap, start_x, start_y, width, width);
        int height = bitmap2.getWidth();
        Bitmap out = Bitmap.createBitmap(height, height, Bitmap.Config.ARGB_8888);
        Path path = new Path();
        float px = height * 1.0f / 2;
        float py = height * 1.0f / 2;
        float rate = height / 34;
        // 路径的起始点
        path.moveTo(px, py - 5 * rate);
        // 根据心形函数画图
        for (double i = 0; i <= 2 * Math.PI; i += 0.001) {
            float x = (float) (16 * Math.sin(i) * Math.sin(i) * Math.sin(i));
            float y = (float) (13 * Math.cos(i) - 5 * Math.cos(2 * i) - 2
                    * Math.cos(3 * i) - Math.cos(4 * i));
            x *= rate;
            y *= rate;
            x = px - x;
            y = py - y;
            path.lineTo(x, y);
        }
        path.close();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(out);
        canvas.drawPath(path, paint);
        Rect src = new Rect(0, 0, height, height);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, src, src, paint);
        return out;
    }

    /**
     * 正多边形位图 xh 2017-3-12 下午5:53:35
     *
     * @param bitmap
     * @param variable
     * @return
     */
    public static Bitmap polygon_bitmap(Bitmap bitmap, int variable) {
        int wid = bitmap.getWidth();
        int hei = bitmap.getHeight();
        return polygon_bitmap(bitmap, variable, 0, 0, wid > hei ? hei : wid);
    }

    /**
     * 正多边形位图 xh 2017-3-7 下午2:07:06
     *
     * @param bitmap
     * @param variable
     * @param start_x
     * @param start_y
     * @param width
     * @return
     */
    public static Bitmap polygon_bitmap(Bitmap bitmap, int variable,
                                        int start_x, int start_y, int width) {
        Log.e("polygon_bitmap","variable="+variable);
        Bitmap bitmap2 = load_figure(bitmap, start_x, start_y, width, width);
        int b_width = bitmap2.getWidth();
        Bitmap out = Bitmap.createBitmap(b_width, b_width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        Paint paint = new Paint();
        Path path = new Path();
        float r = b_width * .5f;
        path.moveTo(width / 2, 0);
        for (int i = 1; i < variable; i++) {
            float[] xy = new float[2];
            double angle = i * 360 / variable + 90;
            double length = r;
            if (angle < 180) {
                angle = Math.toRadians(180 - angle);
                xy[0] = (float) (r + length * Math.cos(angle));
                xy[1] = (float) (r - length * Math.sin(angle));
            } else if (angle < 270) {
                angle = Math.toRadians(angle - 180);
                xy[0] = (float) (r + length * Math.cos(angle));
                xy[1] = (float) (r + length * Math.sin(angle));
            } else if (angle < 360) {
                angle = Math.toRadians(360 - angle);
                xy[1] = (float) (r + length * Math.sin(angle));
                xy[0] = (float) (r - length * Math.cos(angle));
            } else {
                angle = Math.toRadians(angle - 360);
                xy[1] = (float) (r - length * Math.sin(angle));
                xy[0] = (float) (r - length * Math.cos(angle));
            }
            path.lineTo(xy[0], xy[1]);
        }
        path.close();
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect src = new Rect(0, 0, b_width, b_width);
        canvas.drawBitmap(bitmap2, src, src, paint);
        return out;
    }

    /**
     * 五角星位图 xh 2017-3-12 下午5:53:48
     *
     * @param bitmap
     * @return
     */
    public static Bitmap star_bitmap(Bitmap bitmap) {
        return star_bitmap(bitmap, 0, 0,
                bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight()
                        : bitmap.getWidth());
    }

    /**
     * 五角星位图 xh 2017-3-7 下午2:13:55
     *
     * @param bitmap
     * @param start_x
     * @param start_y
     * @param width
     * @return
     */
    public static Bitmap star_bitmap(Bitmap bitmap, int start_x, int start_y,
                                     int width) {
        Bitmap bitmap2 = load_figure(bitmap, start_x, start_y, width, width);
        int b_width = bitmap2.getWidth();
        Bitmap out = Bitmap.createBitmap(b_width, b_width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        Paint paint = new Paint();
        Path path = new Path();
        float r = width * .5f;
        path.moveTo(width / 2, 0);
        for (int i = 1; i < 10; i++) {
            float[] xy = new float[2];
            double angle = i * 36 + 90;
            double length = 0;
            if (i % 2 == 0)
                length = r;
            else
                length = r * 0.618f;
            if (angle < 180) {
                angle = Math.toRadians(180 - angle);
                xy[0] = (float) (r + length * Math.cos(angle));
                xy[1] = (float) (r - length * Math.sin(angle));
            } else if (angle < 270) {
                angle = Math.toRadians(angle - 180);
                xy[0] = (float) (r + length * Math.cos(angle));
                xy[1] = (float) (r + length * Math.sin(angle));
            } else if (angle < 360) {
                angle = Math.toRadians(360 - angle);
                xy[1] = (float) (r + length * Math.sin(angle));
                xy[0] = (float) (r - length * Math.cos(angle));
            } else {
                angle = Math.toRadians(angle - 360);
                xy[1] = (float) (r - length * Math.sin(angle));
                xy[0] = (float) (r - length * Math.cos(angle));
            }
            path.lineTo(xy[0], xy[1]);
        }
        path.close();
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect src = new Rect(0, 0, b_width, b_width);
        canvas.drawBitmap(bitmap2, src, src, paint);
        return out;
    }

    /**
     * 旋转Bitmap
     *
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap rotate_bitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                b.getHeight(), matrix, false);
        int width = rotaBitmap.getWidth();
        int height = rotaBitmap.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(rotaBitmap, 0, 0, paint);
        return bitmap;
    }

    /**
     * LOMO特效
     *
     * @param bitmap
     *            原图片
     * @return LOMO特效图片
     */
    public static Bitmap lomoFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int dst[] = new int[width * height];
        bitmap.getPixels(dst, 0, width, 0, 0, width, height);

        int ratio = width > height ? height * 32768 / width : width * 32768
                / height;
        int cx = width >> 1;
        int cy = height >> 1;
        int max = cx * cx + cy * cy;
        int min = (int) (max * (1 - 0.8f));
        int diff = max - min;

        int ri, gi, bi;
        int dx, dy, distSq, v;

        int R, G, B;

        int value;
        int pos, pixColor;
        int newR, newG, newB;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pos = y * width + x;
                pixColor = dst[pos];
                R = Color.red(pixColor);
                G = Color.green(pixColor);
                B = Color.blue(pixColor);

                value = R < 128 ? R : 256 - R;
                newR = (value * value * value) / 64 / 256;
                newR = (R < 128 ? newR : 255 - newR);

                value = G < 128 ? G : 256 - G;
                newG = (value * value) / 128;
                newG = (G < 128 ? newG : 255 - newG);

                newB = B / 2 + 0x25;

                // ==========边缘黑暗==============//
                dx = cx - x;
                dy = cy - y;
                if (width > height)
                    dx = (dx * ratio) >> 15;
                else
                    dy = (dy * ratio) >> 15;

                distSq = dx * dx + dy * dy;
                if (distSq > min) {
                    v = ((max - distSq) << 8) / diff;
                    v *= v;

                    ri = (int) (newR * v) >> 16;
                    gi = (int) (newG * v) >> 16;
                    bi = (int) (newB * v) >> 16;

                    newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
                    newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
                    newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
                }
                // ==========边缘黑暗end==============//

                dst[pos] = Color.rgb(newR, newG, newB);
            }
        }

        Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
        return acrossFlushBitmap;
    }

    /**
     * 旧时光特效
     *
     * @param bmp
     *            原图片
     * @return 旧时光特效图片
     */
    public static Bitmap oldTimeFilter(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR,
                        newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 暖意特效
     *
     * @param bmp
     *            原图片
     * @param centerX
     *            光源横坐标
     * @param centerY
     *            光源纵坐标
     * @return 暖意特效图片
     */
    public static Bitmap warmthFilter(Bitmap bmp, int centerX, int centerY) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int radius = Math.min(centerX, centerY);

        final float strength = 150F; // 光照强度 100~150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = pixR;
                newG = pixG;
                newB = pixB;

                // 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(
                        centerX - k, 2));
                if (distance < radius * radius) {
                    // 按照距离大小计算增加的光照值
                    int result = (int) (strength * (1.0 - Math.sqrt(distance)
                            / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 根据饱和度、色相、亮度调整图片
     *
     * @param bm
     *            原图片
     * @param saturation
     *            饱和度
     * @param hue
     *            色相
     * @param lum
     *            亮度
     * @return 处理后的图片
     */
    public static Bitmap handleImage(Bitmap bm, int saturation, int hue, int lum) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        ColorMatrix mLightnessMatrix = new ColorMatrix();
        ColorMatrix mSaturationMatrix = new ColorMatrix();
        ColorMatrix mHueMatrix = new ColorMatrix();
        ColorMatrix mAllMatrix = new ColorMatrix();
        float mSaturationValue = saturation * 1.0F / 127;
        float mHueValue = hue * 1.0F / 127;
        float mLumValue = (lum - 127) * 1.0F / 127 * 180;
        mHueMatrix.reset();
        mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1);

        mSaturationMatrix.reset();
        mSaturationMatrix.setSaturation(mSaturationValue);
        mLightnessMatrix.reset();

        mLightnessMatrix.setRotate(0, mLumValue);
        mLightnessMatrix.setRotate(1, mLumValue);
        mLightnessMatrix.setRotate(2, mLumValue);

        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);
        mAllMatrix.postConcat(mSaturationMatrix);
        mAllMatrix.postConcat(mLightnessMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }
    /**
     * 字符数组转化为图片
     *
     * @param heigth
     * @param width
     * @param buff
     * @return
     */
    public static Bitmap bytes2Bitmap(int heigth, int width, byte[] buff) {
        if (width <= 0 && heigth <= 0)
            return bytes2Bitmap(buff);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap1 = BitmapFactory.decodeStream(new ByteArrayInputStream(
                buff), null, opts);
        int bHeigth = opts.outHeight;
        int bWidth = opts.outWidth;
        int multiple = 0;
        if (width > 0 && heigth > 0) {
            int heightScale = bHeigth / heigth;
            int widthScale = bWidth / width;
            // 获取缩略比例 保证最小的能满足要求
            multiple = heightScale > widthScale ? widthScale : heightScale;
        } else if (heigth > 0)
            multiple = bHeigth / heigth;
        else
            multiple = bWidth / width;
        opts.inSampleSize = multiple > 1 ? multiple : 1;
        opts.inJustDecodeBounds = false;
        bitmap1 = BitmapFactory.decodeStream(new ByteArrayInputStream(buff),
                null, opts);
        return zoom(heigth, width, bitmap1);
    }

    /**
     * 字符数组转化为图片
     *
     * @param buff
     * @return
     */
    public static Bitmap bytes2Bitmap(byte[] buff) {
        return BitmapFactory.decodeStream(new ByteArrayInputStream(buff));
    }

    /**
     * 将输入流转化为图片
     *
     * @param heigth
     * @param width
     * @param inputStream
     * @return
     */
    public static Bitmap inputStream2Bitmap(int heigth, int width,
                                            InputStream inputStream) {
        if (heigth <= 0 && width <= 0)
            return inputStream2Bitmap(inputStream);
        byte[] buff = stream2bytes(inputStream);
        return bytes2Bitmap(heigth, width, buff);
    }

    /**
     * 将输入流转化为图片
     *
     * @param inputStream
     * @return
     */
    public static Bitmap inputStream2Bitmap(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }
    /**
     *
     * instruction:将输入流转化为字节数组 2018-6-7 上午11:26:12
     *
     * @param is
     * @return
     */
    public static byte[] stream2bytes(InputStream is) {
        if (is==null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 1024];
        byte[] arr = null;
        try {
            int len = is.read(buff);
            while (len > 0) {
                baos.write(buff, 0, len);
                len = is.read(buff);
            }
            arr = baos.toByteArray();
            baos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try{
                baos.close();
            }catch (Exception e){

            }
            try{
                is.close();
            }catch (Exception e){

            }
        }
        return arr;
    }

    /**
     * 对图片进行毛玻璃化
     * @param sentBitmap 位图
     * @param radius 虚化程度
     * @param canReuseInBitmap 是否重用
     * @return 位图
     */
    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    /**
     * 对图片进行毛玻璃化
     * @param originBitmap 位图
     * @param scaleRatio 缩放比率
     * @param blurRadius 毛玻璃化比率，虚化程度
     * @return 位图
     */
    public static Bitmap doBlur(Bitmap originBitmap, int scaleRatio, int blurRadius){
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originBitmap,
                originBitmap.getWidth() / scaleRatio,
                originBitmap.getHeight() / scaleRatio,
                false);
        Bitmap blurBitmap = doBlur(scaledBitmap, blurRadius, false);
        scaledBitmap.recycle();
        return blurBitmap;
    }

    /**
     * 对图片进行 毛玻璃化，虚化
     * @param originBitmap 位图
     * @param width 缩放后的期望宽度
     * @param height 缩放后的期望高度
     * @param blurRadius 虚化程度
     * @return 位图
     */
    public static Bitmap doBlur(Bitmap originBitmap, int width, int height, int blurRadius){
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(originBitmap, width, height);
        Bitmap blurBitmap = doBlur(thumbnail, blurRadius, true);
        thumbnail.recycle();
        return blurBitmap;
    }
}
