package com.xh.base.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 2018/4/17 14:15
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
@SuppressLint("NewApi")
public class FullActivityBuilder {
    private Activity mActivity;
    private View mTriggerView;
    private float mStartRadius = CircularAnim.MINI_RADIUS;
    private int mColorOrImageRes = CircularAnim.getColorOrImageRes();
    private Long mDurationMills;
    private CircularAnim.OnAnimatorDeployListener mStartAnimatorDeployListener;
    private CircularAnim.OnAnimatorDeployListener mReturnAnimatorDeployListener;
    private CircularAnim.OnAnimationEndListener mOnAnimationEndListener;
    private int mEnterAnim = android.R.anim.fade_in, mExitAnim = android.R.anim.fade_out;

    public FullActivityBuilder(Activity activity, View triggerView) {
        mActivity = activity;
        mTriggerView = triggerView;
    }

    public FullActivityBuilder startRadius(float startRadius) {
        mStartRadius = startRadius;
        return this;
    }

    public FullActivityBuilder colorOrImageRes(int colorOrImageRes) {
        mColorOrImageRes = colorOrImageRes;
        return this;
    }

    public FullActivityBuilder duration(long durationMills) {
        mDurationMills = durationMills;
        return this;
    }

    public FullActivityBuilder overridePendingTransition(int enterAnim, int exitAnim) {
        mEnterAnim = enterAnim;
        mExitAnim = exitAnim;
        return this;
    }

    public FullActivityBuilder deployStartAnimator(CircularAnim.OnAnimatorDeployListener onAnimatorDeployListener) {
        mStartAnimatorDeployListener = onAnimatorDeployListener;
        return this;
    }

    public FullActivityBuilder deployReturnAnimator(CircularAnim.OnAnimatorDeployListener onAnimatorDeployListener) {
        mReturnAnimatorDeployListener = onAnimatorDeployListener;
        return this;
    }

    public void go(CircularAnim.OnAnimationEndListener onAnimationEndListener) {
        mOnAnimationEndListener = onAnimationEndListener;
        // 版本判断,小于5.0则无动画.
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            doOnEnd();
            return;
        }

        int[] location = new int[2];
        mTriggerView.getLocationInWindow(location);
        final int cx = location[0] + mTriggerView.getWidth() / 2;
        final int cy = location[1] + mTriggerView.getHeight() / 2;
        final ImageView view = new ImageView(mActivity);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setImageResource(mColorOrImageRes);
        final ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
        int w = decorView.getWidth();
        int h = decorView.getHeight();
        decorView.addView(view, w, h);

        // 计算中心点至view边界的最大距离
        int maxW = Math.max(cx, w - cx);
        int maxH = Math.max(cy, h - cy);
        final int finalRadius = (int) Math.sqrt(maxW * maxW + maxH * maxH) + 1;

        try {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, mStartRadius, finalRadius);

            int maxRadius = (int) Math.sqrt(w * w + h * h) + 1;
            // 若未设置时长，则以PERFECT_MILLS为基准根据水波扩散的距离来计算实际时间
            if (mDurationMills == null) {
                // 算出实际边距与最大边距的比率
                double rate = 1d * finalRadius / maxRadius;
                // 为了让用户便于感触到水波，速度应随最大边距的变小而越慢，扩散时间应随最大边距的变小而变小，因此比率应在 @rate 与 1 之间。
                mDurationMills = (long) (CircularAnim.getFullActivityMills() * Math.sqrt(rate));
            }
            final long finalDuration = mDurationMills;
            // 由于thisActivity.startActivity()会有所停顿，所以进入的水波动画应比退出的水波动画时间短才能保持视觉上的一致。
            anim.setDuration((long) (finalDuration * 0.9));
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    doOnEnd();

                    mActivity.overridePendingTransition(mEnterAnim, mExitAnim);

                    // 默认显示返回至当前Activity的动画.
                    mTriggerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mActivity.isFinishing())
                                return;
                            try {
                                Animator returnAnim = ViewAnimationUtils.createCircularReveal(view, cx, cy,
                                        finalRadius, mStartRadius);
                                returnAnim.setDuration(finalDuration);
                                returnAnim.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        try {
                                            decorView.removeView(view);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                if (mReturnAnimatorDeployListener != null)
                                    mReturnAnimatorDeployListener.deployAnimator(returnAnim);
                                returnAnim.start();
                            } catch (Exception e) {
                                e.printStackTrace();
                                try {
                                    decorView.removeView(view);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }, 1000);

                }
            });
            if (mStartAnimatorDeployListener != null)
                mStartAnimatorDeployListener.deployAnimator(anim);
            anim.start();
        } catch (Exception e) {
            e.printStackTrace();
            doOnEnd();
        }
    }

    private void doOnEnd() {
        mOnAnimationEndListener.onAnimationEnd();
    }
}
