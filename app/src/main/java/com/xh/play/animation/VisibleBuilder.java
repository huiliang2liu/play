package com.xh.play.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewAnimationUtils;

import java.math.BigDecimal;


/**
 * 2018/4/17 14:39
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
@SuppressLint("NewApi")
public class VisibleBuilder {
    private View mAnimView, mTriggerView;

    private Float mStartRadius, mEndRadius;

    private long mDurationMills = CircularAnim.getPerfectMills();

    private boolean isShow;

    private CircularAnim.OnAnimatorDeployListener mOnAnimatorDeployListener;

    private CircularAnim.OnAnimationEndListener mOnAnimationEndListener;

    public VisibleBuilder(View animView, boolean isShow) {
        mAnimView = animView;
        this.isShow = isShow;
        if (isShow) {
            mStartRadius = CircularAnim.MINI_RADIUS + 0F;
        } else {
            mEndRadius = CircularAnim.MINI_RADIUS + 0F;
        }
    }

    public VisibleBuilder triggerView(View triggerView) {
        mTriggerView = triggerView;
        return this;
    }

    public VisibleBuilder startRadius(float startRadius) {
        mStartRadius = startRadius;
        return this;
    }

    public VisibleBuilder endRadius(float endRadius) {
        mEndRadius = endRadius;
        return this;
    }

    public VisibleBuilder duration(long durationMills) {
        mDurationMills = durationMills;
        return this;
    }

    public VisibleBuilder deployAnimator(CircularAnim.OnAnimatorDeployListener onAnimatorDeployListener) {
        mOnAnimatorDeployListener = onAnimatorDeployListener;
        return this;
    }

    @Deprecated //You can use method - go(OnAnimationEndListener onAnimationEndListener).
    public VisibleBuilder onAnimationEndListener(CircularAnim.OnAnimationEndListener onAnimationEndListener) {
        mOnAnimationEndListener = onAnimationEndListener;
        return this;
    }

    public void go() {
        go(null);
    }

    public void go(CircularAnim.OnAnimationEndListener onAnimationEndListener) {
        mOnAnimationEndListener = onAnimationEndListener;
        // 版本判断
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            doOnEnd();
            return;
        }
        int rippleCX, rippleCY, maxRadius;
        if (mTriggerView != null) {
            int[] tvLocation = new int[2];
            mTriggerView.getLocationInWindow(tvLocation);
            final int tvCX = tvLocation[0] + mTriggerView.getWidth() / 2;
            final int tvCY = tvLocation[1] + mTriggerView.getHeight() / 2;
            int[] avLocation = new int[2];
            mAnimView.getLocationInWindow(avLocation);
            final int avLX = avLocation[0];
            final int avTY = avLocation[1];
            int triggerX = Math.max(avLX, tvCX);
            triggerX = Math.min(triggerX, avLX + mAnimView.getWidth());
            int triggerY = Math.max(avTY, tvCY);
            triggerY = Math.min(triggerY, avTY + mAnimView.getHeight());
            // 以上全为绝对坐标
            int avW = mAnimView.getWidth();
            int avH = mAnimView.getHeight();
            rippleCX = triggerX - avLX;
            rippleCY = triggerY - avTY;
            // 计算水波中心点至 @mAnimView 边界的最大距离
            int maxW = Math.max(rippleCX, avW - rippleCX);
            int maxH = Math.max(rippleCY, avH - rippleCY);
            maxRadius = (int) Math.sqrt(maxW * maxW + maxH * maxH) + 1;
        } else {
            rippleCX = (mAnimView.getLeft() + mAnimView.getRight()) / 2;
            rippleCY = (mAnimView.getTop() + mAnimView.getBottom()) / 2;
            int w = mAnimView.getWidth();
            int h = mAnimView.getHeight();
            // 勾股定理 & 进一法
            maxRadius = BigDecimal.valueOf(Math.sqrt(w*w+h*h)).setScale(0, BigDecimal.ROUND_CEILING).intValue();
        }

        if (isShow && mEndRadius == null)
            mEndRadius = maxRadius + 0F;
        else if (!isShow && mStartRadius == null)
            mStartRadius = maxRadius + 0F;
        try {
            Animator anim = ViewAnimationUtils.createCircularReveal(
                    mAnimView, rippleCX, rippleCY, mStartRadius, mEndRadius);
            mAnimView.setVisibility(View.VISIBLE);
            anim.setDuration(mDurationMills);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    doOnEnd();
                }
            });
            if (mOnAnimatorDeployListener != null)
                mOnAnimatorDeployListener.deployAnimator(anim);
            anim.start();
        } catch (Exception e) {
            e.printStackTrace();
            doOnEnd();
        }
    }

    private void doOnEnd() {
        if (isShow)
            mAnimView.setVisibility(View.VISIBLE);
        else
            mAnimView.setVisibility(View.INVISIBLE);
        if (mOnAnimationEndListener != null)
            mOnAnimationEndListener.onAnimationEnd();
    }
}
