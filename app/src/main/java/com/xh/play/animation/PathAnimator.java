package com.xh.play.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;
import android.view.animation.Interpolator;

public class PathAnimator implements Animator.AnimatorListener, Interpolator {
    private PathMeasure measure;
    private float[] mCurrentPosition = new float[2];
    private long duration = 300;
    private int repeatCount = 1;
    private View view;

    public PathAnimator(Path path, View view) {
        this(path, view, false);
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void setDuration(long duration) {
        if (duration > 0)
            this.duration = duration;
    }

    public PathAnimator(Path path, View view, boolean forceClosed) {
        if (path == null)
            throw new RuntimeException("you path is null");
        if (view == null)
            throw new RuntimeException("you view is null");
        this.view = view;
        measure = new PathMeasure(path, forceClosed);
    }

    public Animator pathAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, measure.getLength());
        animator.setDuration(duration);
        if (repeatCount > 1)
            animator.setRepeatCount(repeatCount);
        animator.setInterpolator(this);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                measure.getPosTan(value, mCurrentPosition, null);
                view.setTranslationX(mCurrentPosition[0]);
                view.setTranslationY(mCurrentPosition[1]);
            }
        });
        animator.addListener(this);
        return animator;
    }

    public void startAnimation() {
        pathAnimation().start();
    }

    @Override
    public float getInterpolation(float input) {
        return input;
    }

    @Override
    public void onAnimationStart(Animator animation, boolean isReverse) {

    }

    @Override
    public void onAnimationEnd(Animator animation, boolean isReverse) {

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
