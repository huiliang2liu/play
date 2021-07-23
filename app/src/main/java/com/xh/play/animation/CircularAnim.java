package com.xh.play.animation;

import android.animation.Animator;
import android.app.Activity;
import android.view.View;

/**
 * 2018/4/17 14:14
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class CircularAnim {
    public static final long PERFECT_MILLS = 618;
    public static final int MINI_RADIUS = 0;

    private static Long sPerfectMills;
    private static Long sFullActivityPerfectMills;
    private static Integer sColorOrImageRes;

    public static long getPerfectMills() {
        if (sPerfectMills != null)
            return sPerfectMills;
        else
            return PERFECT_MILLS;
    }

    public static long getFullActivityMills() {
        if (sFullActivityPerfectMills != null)
            return sFullActivityPerfectMills;
        else
            return PERFECT_MILLS;
    }

    public static int getColorOrImageRes() {
        if (sColorOrImageRes != null)
            return sColorOrImageRes;
        else
            return android.R.color.white;
    }


    public interface OnAnimationEndListener {
        void onAnimationEnd();
    }

    public interface OnAnimatorDeployListener {
        void deployAnimator(Animator animator);
    }




    /* 上面为实现逻辑，下面为外部调用方法 */


    /* 伸展并显示@animView */
    public static VisibleBuilder show(View animView) {
        return new VisibleBuilder(animView, true);
    }

    /* 收缩并隐藏@animView */
    public static VisibleBuilder hide(View animView) {
        return new VisibleBuilder(animView, false);
    }

    /* 以@triggerView 为触发点铺满整个@activity */
    public static FullActivityBuilder fullActivity(Activity activity, View triggerView) {
        return new FullActivityBuilder(activity, triggerView);
    }

    /* 设置默认时长，设置充满activity的默认颜色或图片资源 */
    public static void init(long perfectMills, long fullActivityPerfectMills, int colorOrImageRes) {
        sPerfectMills = perfectMills;
        sFullActivityPerfectMills = fullActivityPerfectMills;
        sColorOrImageRes = colorOrImageRes;
    }
}
