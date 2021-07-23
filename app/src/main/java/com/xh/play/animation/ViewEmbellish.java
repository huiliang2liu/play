package com.xh.play.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import java.util.List;

/**
 * 2018/5/22 9:41
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class ViewEmbellish implements Animator.AnimatorListener, Interpolator {
    private static final String TAG = "ViewEmbellish";
    private static final long DEFAULT_TIME = 300;
    private View view;
    private ViewGroup.LayoutParams params;
    private ObjectAnimator animator;

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

    @Override
    public float getInterpolation(float input) {
        return input;
    }

    public ViewEmbellish(View view) {
        // TODO Auto-generated constructor stub
        if (view == null)
            throw new RuntimeException("you view is null");
        this.view = view;
        params = view.getLayoutParams();
        if (params == null)
            throw new RuntimeException("this view no params");
        if (params instanceof ViewGroup.MarginLayoutParams)
            Log.e(TAG, "is margin");
    }


    public int getWidth() {
        return view.getWidth();
    }

    public void setWidth(int width) {
        params.width = width;
        view.requestLayout();
    }

    public int getTopMargin() {
        if (params instanceof ViewGroup.MarginLayoutParams)
            return ((ViewGroup.MarginLayoutParams) params).topMargin;
        return 0;
    }

    public void setTopMargin(int topMargin) {
        if (params instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) params).topMargin = topMargin;
            view.requestLayout();
        }
    }

    public int getBottomMargin() {
        if (params instanceof ViewGroup.MarginLayoutParams)
            return ((ViewGroup.MarginLayoutParams) params).bottomMargin;
        return 0;
    }

    public void setBottomMargin(int bottomMargin) {
        if (params instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) params).bottomMargin = bottomMargin;
            view.requestLayout();
        }
    }

    public int getLeftMargin() {
        if (params instanceof ViewGroup.MarginLayoutParams)
            return ((ViewGroup.MarginLayoutParams) params).leftMargin;
        return 0;
    }

    public void setLeftMargin(int leftMargin) {
        if (params instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) params).leftMargin = leftMargin;
            view.requestLayout();
        }
    }

    public int getRightMargin() {
        if (params instanceof ViewGroup.MarginLayoutParams)
            return ((ViewGroup.MarginLayoutParams) params).rightMargin;
        return 0;
    }

    public void setRightMargin(int rightMargin) {
        if (params instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) params).rightMargin = rightMargin;
            view.requestLayout();
        }
    }

    public int getHeight() {
        return view.getHeight();
    }

    public void setHeight(int height) {
        params.height = height;
        view.requestLayout();
    }

    public float getX() {
        return view.getX();
    }

    public void setX(float x) {
        view.setX(x);
    }

    public float getY() {
        return view.getY();
    }

    public void setY(float y) {
        view.setY(y);
    }

    public float getAlpha() {
        return view.getAlpha();
    }

    public void setAlpha(float alpha) {
        view.setAlpha(alpha);
    }

    public float getTranslationY() {
        return view.getTranslationY();
    }

    public void setTranslationY(float translationY) {
        view.setTranslationY(translationY);
    }

    public float getTranslationX() {
        return view.getTranslationX();
    }

    public void setTranslationX(float translationX) {
        view.setTranslationX(translationX);
    }

    public float getScaleX() {
        return view.getScaleX();
    }

    public void setScaleX(float scaleX) {
        view.setScaleX(scaleX);
    }

    public float getScaleY() {
        return view.getScaleY();
    }

    public void setScaleY(float scaleY) {
        view.setScaleY(scaleY);
    }

    public void setBackgroundColor(int color) {
        view.setBackgroundColor(color);
    }

    public int getBackgroundColor() {
        Drawable drawable = view.getBackground();
        if (drawable instanceof ColorDrawable) {
            return ((ColorDrawable) drawable).getColor();
        } else
            return Color.argb(0x00, 0x00, 0x00, 0x00);
    }

    public float getRotationX() {
        return view.getRotationX();
    }

    public void setRotationX(float rotationX) {
        view.setRotationX(rotationX);
    }

    public float getRotationY() {
        return view.getRotationY();
    }

    public void setRotationY(float rotationY) {
        view.setRotationY(rotationY);
    }

    public float getRotation() {
        return view.getRotation();
    }

    public void setRotation(float rotation) {
        view.setRotation(rotation);
    }

    public ObjectAnimator alphaAnimator(float... values) {
        return alphaAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator alphaAnimator(long time, float... values) {
        return ofFloat("alpha", time, values);
    }

    public void alpha(float... values) {
        alpha(DEFAULT_TIME, values);
    }

    public void alpha(long time, float... values) {
        alphaAnimator(time, values).start();
    }

    public ObjectAnimator yAnimator(float... values) {
        return yAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator yAnimator(long time, float... values) {
        return ofFloat("y", time, values);
    }

    public void y(float... value) {
        y(DEFAULT_TIME, value);
    }

    public void y(long time, float... value) {
        yAnimator(time, value).start();
    }


    public ObjectAnimator xAnimator(float... values) {
        return xAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator xAnimator(long time, float... values) {
        return ofFloat("x", time, values);
    }

    public void x(float... value) {
        x(DEFAULT_TIME, value);
    }

    public void x(long time, float... value) {
        xAnimator(time, value).start();
    }


    public ObjectAnimator translationYAnimator(float... values) {
        return translationYAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator translationYAnimator(long time, float... values) {
        return ofFloat("translationY", time, values);
    }

    public void translationY(float... value) {
        translationY(DEFAULT_TIME, value);
    }

    public void translationY(long time, float... value) {
        translationYAnimator(time, value).start();
    }


    public ObjectAnimator translationXAnimator(float... values) {
        return translationXAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator translationXAnimator(long time, float... values) {
        return ofFloat("translationX", time, values);
    }

    public void translationX(float... value) {
        translationX(DEFAULT_TIME, value);
    }

    public void translationX(long time, float... value) {
        translationXAnimator(time, value).start();
    }

    public ObjectAnimator scaleYAnimator(float... values) {
        return scaleYAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator scaleYAnimator(long time, float... values) {
        return ofFloat("scaleY", time, values);
    }

    public void scaleY(float... value) {
        scaleY(DEFAULT_TIME, value);
    }

    public void scaleY(long time, float... value) {
        scaleYAnimator(time, value).start();
    }

    public ObjectAnimator scaleXAnimator(float... values) {
        return scaleXAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator scaleXAnimator(long time, float... values) {
        return ofFloat("scaleX", time, values);
    }

    public void scaleX(float... value) {
        scaleX(DEFAULT_TIME, value);
    }

    public void scaleX(long time, float... value) {
        scaleXAnimator(time, value).start();
    }

    public ObjectAnimator widthAnimator(int... values) {
        return widthAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator widthAnimator(long time, int... values) {
        return ofInt("width", time, values);
    }

    public void width(int... value) {
        width(DEFAULT_TIME, value);
    }

    public void width(long time, int... value) {
        widthAnimator(time, value).start();
    }

    public ObjectAnimator heightAnimator(int... values) {
        return heightAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator heightAnimator(long time, int... values) {
        return ofInt("height", time, values);
    }

    public void height(int... value) {
        height(DEFAULT_TIME, value);
    }

    public void height(long time, int... value) {
        heightAnimator(time, value).start();
    }

    public ObjectAnimator backgroundColorAnimator(int... values) {
        return backgroundColorAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator backgroundColorAnimator(long time, int... values) {
        return ofInt("backgroundColor", time, values);
    }

    public void backgroundColor(int... value) {
        backgroundColor(DEFAULT_TIME, value);
    }

    public void backgroundColor(long time, int... value) {
        backgroundColorAnimator(time, value).start();
    }

    public ObjectAnimator topMarginAnimator(int... values) {
        return topMarginAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator topMarginAnimator(long time, int... values) {
        return ofInt("topMargin", time, values);
    }

    public void topMargin(int... value) {
        topMargin(DEFAULT_TIME, value);
    }

    public void topMargin(long time, int... value) {
        topMarginAnimator(time, value).start();
    }

    public ObjectAnimator bottomMarginAnimator(int... values) {
        return bottomMarginAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator bottomMarginAnimator(long time, int... values) {
        return ofInt("bottomMargin", time, values);
    }

    public void bottomMargin(int... value) {
        bottomMargin(DEFAULT_TIME, value);
    }

    public void bottomMargin(long time, int... value) {
        bottomMarginAnimator(time, value).start();
    }

    public ObjectAnimator leftMarginAnimator(int... values) {
        return leftMarginAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator leftMarginAnimator(long time, int... values) {
        return ofInt("leftMargin", time, values);
    }

    public void leftMargin(int... value) {
        leftMargin(DEFAULT_TIME, value);
    }

    public void leftMargin(long time, int... value) {
        leftMarginAnimator(time, value).start();
    }

    public ObjectAnimator rightMarginAnimator(int... values) {
        return rightMarginAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator rightMarginAnimator(long time, int... values) {
        return ofInt("rightMargin", time, values);
    }

    public void rightMargin(int... value) {
        rightMargin(DEFAULT_TIME, value);
    }

    public void rightMargin(long time, int... value) {
        rightMarginAnimator(time, value).start();
    }

    public ObjectAnimator rotationAnimator(float... values) {
        return rotationAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator rotationAnimator(long time, float... values) {
        return ofFloat("rotation", time, values);
    }

    public void rotation(float... value) {
        rotation(DEFAULT_TIME, value);
    }

    public void rotation(long time, float... value) {
        rotationAnimator(time, value).start();
    }

    public ObjectAnimator rotationYAnimator(float... values) {
        return rotationYAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator rotationYAnimator(long time, float... values) {
        return ofFloat("rotationY", time, values);
    }

    public void rotationY(float... value) {
        rotationY(DEFAULT_TIME, value);
    }

    public void rotationY(long time, float... value) {
        rotationYAnimator(time, value).start();
    }


    public ObjectAnimator rotationXAnimator(float... values) {
        return rotationXAnimator(DEFAULT_TIME, values);
    }

    public ObjectAnimator rotationXAnimator(long time, float... values) {
        return ofFloat("rotationX", time, values);
    }

    public void rotationX(float... value) {
        rotationX(DEFAULT_TIME, value);
    }

    public void rotationX(long time, float... value) {
        rotationXAnimator(time, value).start();
    }


    public void start(Animator animator, List<Animator> befores, List<Animator> withs, List<Animator> afters) {
        if ((befores == null || befores.size() <= 0) && (withs == null || withs.size() <= 0) && (afters == null && afters.size() <= 0)) {
            animator.start();
            return;
        }
        AnimatorSet set = new AnimatorSet();
        AnimatorSet.Builder builder = set.play(animator);
        if (befores != null && befores.size() > 0)
            for (Animator animator1 : befores)
                builder.before(animator1);
        if (withs != null && withs.size() > 0)
            for (Animator animator1 : withs)
                builder.with(animator1);
        if (afters != null && afters.size() > 0)
            for (Animator animator1 : afters)
                builder.after(animator1);
        set.start();
    }

    private void setAnimator(Animator animator, long time) {
        animator.setDuration(time);
        animator.addListener(this);
        animator.setInterpolator(this);
//        animator.set
    }

    private ObjectAnimator ofFloat(String name, long time, float... values) {
        ObjectAnimator objectAnimator = ofFloat(name, values);
        setAnimator(objectAnimator, time);
        return objectAnimator;
    }

    private ObjectAnimator ofFloat(String name, float... values) {
        return ObjectAnimator.ofFloat(this, name, values);
    }

    private ObjectAnimator ofInt(String name, long time, int... values) {
        ObjectAnimator animator = ObjectAnimator.ofInt(this, name, values);
        setAnimator(animator, time);
        return animator;
    }

    private ObjectAnimator ofInt(String name, int... values) {
        return ObjectAnimator.ofInt(this, name, values);
    }
}
