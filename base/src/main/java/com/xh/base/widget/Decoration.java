package com.xh.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * com.view
 * 2018/9/28 15:02
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class Decoration extends RecyclerView.ItemDecoration {
    private static final int HORIZONTAL = 1;
    private static final int VERTICAL = HORIZONTAL << 1;
    private Context mContext;
    //    private Drawable mDivider;
    private Drawable verticalDrawable;
    private Drawable horizontalDrawable;
    private int mWidth;
    private int mHeight;
    private Paint paint;
    private Orientation mOrientation;

    public static enum Orientation {
        HORIZONTAL(Decoration.HORIZONTAL), VERTICAL(Decoration.VERTICAL), GRID(Decoration.VERTICAL | Decoration.HORIZONTAL);
        private int value = 0;

        private Orientation(int value) {
            this.value = value;
        }
    }

    //我们通过获取系统属性中的listDivider来添加，在系统中的AppTheme中设置
    public static final int[] ATRRS = new int[]{
            android.R.attr.listDivider
    };

    private Decoration(Context context) {
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(ATRRS);
        verticalDrawable = horizontalDrawable = ta.getDrawable(0);
        mWidth = mHeight = verticalDrawable.getIntrinsicWidth();
        ta.recycle();
    }

    public Decoration(Context context, Orientation orientation) {
        this(context);
        mOrientation = orientation;
    }

    public Decoration(Context context, Orientation orientation, Drawable drawable) {
        this(context);
        verticalDrawable = horizontalDrawable = drawable;
        mContext = context;
        mOrientation = orientation;
    }

    public void setLine(int line) {
        if (line > 0)
            this.mWidth = mHeight = line;
    }

    public void setWidth(int width) {
        if (width > 0)
            this.mWidth = width;
    }

    public void setHeight(int height) {
        if (height > 0)
            this.mHeight = height;
    }

    public void setDrawable(Drawable drawable) {
        if (drawable != null)
            verticalDrawable = horizontalDrawable = drawable;
    }

    public void setVerticalDrawable(Drawable drawable) {
        if (drawable != null)
            verticalDrawable = drawable;
    }

    public void setHorizontalDrawable(Drawable drawable) {
        if (drawable != null)
            horizontalDrawable = drawable;
    }

    public Decoration(Context context, Orientation orientation, int color, int width) {
        this(context, orientation);
        if (width > 0)
            mWidth = mHeight = width;
        paint = new Paint();
        paint.setStrokeWidth(width);
        paint.setColor(color);
        paint.setAntiAlias(true);
    }

    public Decoration(Context context, Orientation orientation, int color) {
        this(context, orientation, color, -1);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            //获得child的布局信息
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if ((HORIZONTAL & mOrientation.value) == HORIZONTAL) {
                int left1 = child.getLeft() - params.leftMargin - mWidth;
                int right1 = child.getRight() + params.rightMargin;
                drawHorizonTalLine(c, left1, child.getTop() - params.topMargin, left1 + mWidth, child.getBottom() + params.bottomMargin);
                drawHorizonTalLine(c, right1, child.getTop() - params.topMargin, right1 + mWidth, child.getBottom() + params.bottomMargin);
            }
            if ((VERTICAL & mOrientation.value) == VERTICAL) {
                int top1 = child.getTop() - params.topMargin - mHeight;
                int bottom1 = child.getBottom() + params.bottomMargin;
                drawVerticalLine(c, child.getLeft() - params.leftMargin - mWidth, top1, child.getRight() + params.rightMargin + mWidth, top1 + mHeight);
                drawVerticalLine(c, child.getLeft() - params.leftMargin - mWidth, bottom1, child.getRight() + params.rightMargin + mWidth, bottom1 + mHeight);
            }
        }
    }


    private void drawHorizonTalLine(Canvas canvas, int left, int top, int right, int bottom) {
        if (paint == null) {
            horizontalDrawable.setBounds(left, top, right, bottom);
            horizontalDrawable.draw(canvas);
        } else {
            canvas.drawLine(left, top, right, bottom, paint);
        }
    }

    private void drawVerticalLine(Canvas canvas, int left, int top, int right, int bottom) {
        if (paint == null) {
            verticalDrawable.setBounds(left, top, right, bottom);
            verticalDrawable.draw(canvas);
        } else {
            canvas.drawLine(left, top, right, bottom, paint);
        }
    }

    //由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation.value == HORIZONTAL) {
            //画横线，就是往下偏移一个分割线的高度
            outRect.set(0, 0, mWidth, 0);
        } else if (mOrientation.value == VERTICAL) {
            //画竖线，就是往右偏移一个分割线的宽度
            outRect.set(0, 0, 0, mHeight);

        } else {
            outRect.set(0, 0, mWidth, mHeight);
        }
    }
}
