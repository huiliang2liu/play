package com.xh.base.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import java.util.List;

/**
 * 2018/5/22 9:42
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class AnimatorFactory {
    // public static <T> AnimatorFactory alpha(Object object,
    // Interpolator interpolator, TypeEvaluator<T> evaluator) {
    // // 第一个参数为 view对象，第二个参数为 动画改变的类型，第三，第四个参数依次是开始透明度和结束透明度。
    // AnimatorFactory alpha = AnimatorFactory.ofFloat(object, "alpha", 0f, 1f);
    // alpha.setDuration(2000);// 设置动画时间
    // if (interpolator != null)
    // alpha.setInterpolator(interpolator);// 设置动画插入器，减速
    // alpha.setRepeatCount(-1);// 设置动画重复次数，这里-1代表无限
    // alpha.setRepeatMode(Animation.REVERSE);// 设置动画循环模式。
    // /*
    // * ArgbEvaluator：这种评估者可以用来执行类型之间的插值整数值代表ARGB颜色。
    // * FloatEvaluator：这种评估者可以用来执行浮点值之间的插值。
    // * IntEvaluator：这种评估者可以用来执行类型int值之间的插值。
    // * RectEvaluator：这种评估者可以用来执行类型之间的插值矩形值。
    // *
    // * 由于本例是改变View的backgroundColor属性的背景颜色所以此处使用ArgbEvaluator
    // */
    // if (evaluator != null)
    // alpha.setEvaluator(evaluator);
    // return alpha;
    // // alpha.start();// 启动动画。
    // }
    public static ObjectAnimator alpha(ViewEmbellish embellish, long time, float... values) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(embellish, "alpha",
                values);
        setAnimationTime(alpha, time);
        return alpha;
    }

    public static ObjectAnimator Y(ViewEmbellish embellish, long time, float... values) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(embellish, "Y", values);
        setAnimationTime(translationY, time);
        return translationY;
    }

    public static ObjectAnimator X(ViewEmbellish embellish, long time, float... values) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(embellish, "X",
                values);
        setAnimationTime(translationX, time);
        return translationX;
    }

    public static ObjectAnimator translationY(ViewEmbellish embellish, long time,
                                              float... values) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(embellish,
                "translationY", values);
        setAnimationTime(translationY, time);
        return translationY;
    }

    public static ObjectAnimator translationX(ViewEmbellish embellish, long time,
                                              float... values) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(embellish,
                "translationX", values);
        setAnimationTime(translationX, time);
        return translationX;
    }

    public static ObjectAnimator scaleY(ViewEmbellish embellish, long time, float... values) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(embellish,
                "scaleY", values);
        setAnimationTime(translationY, time);
        return translationY;
    }

    public static ObjectAnimator scaleX(ViewEmbellish embellish, long time, float... values) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(embellish,
                "scaleX", values);
        setAnimationTime(translationX, time);
        return translationX;
    }

    public static ObjectAnimator width(ViewEmbellish embellish, long time, int... values) {
        ObjectAnimator translationY = ObjectAnimator.ofInt(embellish, "width",
                values);
        setAnimationTime(translationY, time);
        return translationY;
    }

    public static ObjectAnimator height(ViewEmbellish embellish, long time, int... values) {
        ObjectAnimator translationX = ObjectAnimator.ofInt(embellish, "height",
                values);
        setAnimationTime(translationX, time);
        return translationX;
    }

    public static ObjectAnimator backgroundColor(ViewEmbellish embellish, long time,
                                                 int... values) {
        ObjectAnimator translationX = ObjectAnimator.ofInt(embellish,
                "backgroundColor", values);
        setAnimationTime(translationX, time);
        return translationX;
    }

    public static ObjectAnimator topMargin(ViewEmbellish embellish, long time, int... values) {
        ObjectAnimator topMargin = ObjectAnimator.ofInt(embellish, "topMargin", values);
        setAnimationTime(topMargin, time);
        return topMargin;
    }

    public static ObjectAnimator bottomMargin(ViewEmbellish embellish, long time, int... values) {
        ObjectAnimator bottomMargin = ObjectAnimator.ofInt(embellish, "bottomMargin", values);
        setAnimationTime(bottomMargin, time);
        return bottomMargin;
    }

    public static ObjectAnimator leftMargin(ViewEmbellish embellish, long time, int... values) {
        ObjectAnimator leftMargin = ObjectAnimator.ofInt(embellish, "leftMargin", values);
        setAnimationTime(leftMargin, time);
        return leftMargin;
    }

    public static ObjectAnimator rightMargin(ViewEmbellish embellish, long time, int... values) {
        ObjectAnimator rightMargin = ObjectAnimator.ofInt(embellish, "rightMargin", values);
        setAnimationTime(rightMargin, time);
        return rightMargin;
    }

    public static void setAnimationTime(ObjectAnimator objectAnimator, long time) {
        if (time <= 0)
            time = 300;
        objectAnimator.setDuration(time);
    }

    public static ObjectAnimator rotation(ViewEmbellish embellish, long time,
                                          float... values) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(embellish,
                "rotation", values);
        setAnimationTime(translationX, time);
        return translationX;
    }

    public static ObjectAnimator rotatioY(ViewEmbellish embellish, long time,
                                          float... values) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(embellish,
                "rotationY", values);
        setAnimationTime(translationY, time);
        return translationY;
    }

    public static ObjectAnimator rotatioX(ViewEmbellish embellish, long time,
                                          float... values) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(embellish,
                "rotationX", values);
        setAnimationTime(translationX, time);
        return translationX;
    }



    public static void startAnimation(List<ObjectAnimator> withs) {
        if (withs == null)
            return;
        if (withs.size() <= 0)
            return;
        if (withs.size() == 1) {
            withs.get(0).start();
        }
        AnimatorSet set = new AnimatorSet();
        AnimatorSet.Builder builder = set.play(withs.get(0));
        for (int i = 1; i < withs.size(); i++)
            builder.with(withs.get(i));
        set.start();
    }

    public static void startAnimation(ObjectAnimator animator, List<ObjectAnimator> befores, List<ObjectAnimator> withs, List<ObjectAnimator> afters) {
        if ((befores != null && befores.size() > 0) || (withs != null && withs.size() > 0) || (afters != null && afters.size() > 0)) {
            AnimatorSet set = new AnimatorSet();
            AnimatorSet.Builder builder = set.play(animator);
            if (befores != null && befores.size() > 0)
                for (ObjectAnimator before : befores)
                    builder.before(before);
            if (withs != null && withs.size() > 0) {
                for (ObjectAnimator with : withs)
                    builder.with(with);
            }
            if (afters != null && afters.size() > 0) {
                for (ObjectAnimator after : afters)
                    builder.after(after);
            }
            set.start();
        } else {
            animator.start();
        }
    }
}
