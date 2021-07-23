package com.xh.play.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xh.play.R;


/**
 * com.witget
 * 2019/1/2 14:59
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class ViewPager extends androidx.viewpager.widget.ViewPager {
    private boolean isCanScroll = true; // 是否需要滑动

    public ViewPager(@NonNull Context context) {
        super(context);
    }

    public ViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getResources().obtainAttributes(attrs, R.styleable.ViewPager);
        isCanScroll = a.getBoolean(R.styleable.ViewPager_scroll, true);
        a.recycle();
    }


    /**
     * 设置是否需要滑动
     *
     * @param isCanScroll
     */
    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if (isCanScroll) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }
}
