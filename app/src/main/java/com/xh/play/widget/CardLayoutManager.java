package com.xh.play.widget;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CardLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "swipecard";
    public static int MAX_SHOW_COUNT = 4;
    public static float SCALE_GAP = 0.05f;
    public final int transYGap;
    private RenRenCallback renRenCallback;
    private RecyclerView recyclerView;
    private boolean removeItem = false;
    private int size = -1;

    public CardLayoutManager(RecyclerView recyclerView) {
        //平移时, 需要用到的参考值
        this.recyclerView = recyclerView;
        transYGap = (int) (20 * recyclerView.getContext().getResources().getDisplayMetrics().density);
        renRenCallback = new RenRenCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(renRenCallback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(this);
    }

    public void setSwipeListener(OnSwipeListener swipeListener) {
        renRenCallback.setSwipeListener(swipeListener);
    }

    public void resetSize() {
        size = -1;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        //必须要实现的方法
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //在这个方法中进行View的布局操作.此方法会被调用多次.
        detachAndScrapAttachedViews(recycler);
        if (!removeItem) {
            if (size < 0)
                size = getItemCount();
        } else {
            removeItem = false;
            size--;
        }
        int itemCount = getItemCount();
        Log.e(TAG, String.format("size:%d", size));
        if (size < 1) {
            Log.e(TAG, "删除完毕");
            return;
        }
        //top-3View的position
        int bottomPosition;
        //边界处理
        if (size < MAX_SHOW_COUNT) {
            bottomPosition = 0;
        } else {
            bottomPosition = size - MAX_SHOW_COUNT;
        }
        //从可见的最底层View开始layout，依次层叠上去
        for (int position = bottomPosition; position < size; position++) {
            //1:重recycler的缓存机制中拿到一个View
            View view = recycler.getViewForPosition(position);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            //4:和自定义ViewGroup的onLayout一样, 需要layout View.对View进行布局
            //我们在布局时，将childView居中处理，这里也可以改为只水平居中
            layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view));
            /**
             * TopView的Scale 为1，translationY 0
             * 每一级Scale相差0.05f，translationY相差7dp左右
             *
             * 观察人人影视的UI，拖动时，topView被拖动，Scale不变，一直为1.
             * top-1View 的Scale慢慢变化至1，translation也慢慢恢复0
             * top-2View的Scale慢慢变化至 top-1View的Scale，translation 也慢慢变化只top-1View的translation
             * top-3View的Scale要变化，translation岿然不动
             */
            //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
            int level = size - position - 1;
            //如果不需要缩放平移, 那么下面的代码可以注释掉...
            //除了顶层不需要缩小和位移
            if (level > 0 /*&& level < mShowCount - 1*/) {
                //每一层都需要X方向的缩小
                view.setScaleX(1 - SCALE_GAP * level);
                //前N层，依次向下位移和Y方向的缩小
//                if (level < MAX_SHOW_COUNT - 1) {
                view.setTranslationY(transYGap * level);
                view.setScaleY(1 - SCALE_GAP * level);
//                } else {//第N层在 向下位移和Y方向的缩小的成都与 N-1层保持一致
//                    view.setTranslationY(transYGap * (level - 1));
//                    view.setScaleY(1 - SCALE_GAP * (level - 1));
//                }
            } else {
                view.setScaleX(1);
                view.setScaleY(1);
                view.setTranslationY(0);
            }
        }
    }

    public class RenRenCallback extends ItemTouchHelper.SimpleCallback {

        private static final String TAG = "RenRen";
        private static final int MAX_ROTATION = 15;
        OnSwipeListener mSwipeListener;
        boolean isSwipeAnim = false;

        public RenRenCallback() {
            //第一个参数决定可以拖动排序的方向, 这里由于不需要拖动排序,所以传0
            //第二个参数决定可以支持滑动的方向,这里设置了上下左右都可以滑动.
            super(0, ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        public void setSwipeListener(OnSwipeListener swipeListener) {
            mSwipeListener = swipeListener;
        }

        //水平方向是否可以被回收掉的阈值
        public float getThreshold(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //2016 12 26 考虑 探探垂直上下方向滑动，不删除卡片，这里参照源码写死0.5f
            return recyclerView.getWidth() * /*getSwipeThreshold(viewHolder)*/ 0.5f;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //由于不支持滑动排序, 所以不需要处理此方法
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //当view需要滑动的时候,会回调此方法
            //但是这个方法只是告诉你View需要滑动, 并不是对View和Adapter进行额外的操作,
            //所以, 如果你需要实现滑动删除, 那么需要在此方法中remove item等.

            //我们这里需要对滑动过后的View,进行恢复操作.
            viewHolder.itemView.setRotation(0);//恢复最后一次的旋转状态
            removeItem = true;
            recyclerView.removeView(viewHolder.itemView);
            if (mSwipeListener != null) {
                mSwipeListener.onSwipeTo(viewHolder, 0);
            }
            notifyListener(viewHolder.getAdapterPosition(), direction);
        }

        private void notifyListener(int position, int direction) {
            Log.w(TAG, "onSwiped: " + position + " " + direction);
            if (mSwipeListener != null) {
                mSwipeListener.onSwiped(position, direction);
            }
        }

        @Override
        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            //滑动的比例达到多少之后, 视为滑动
            return 0.3f;
        }


        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            //当你在滑动的过程中, 此方法一直会被回调, 就跟onTouch事件一样...
            //先根据滑动的dx dy 算出现在动画的比例系数fraction
            if (Math.abs(dX) >= recyclerView.getWidth() || Math.abs(dY) >= recyclerView.getHeight()) {
                Log.w(TAG, "重新设置不需要旋转");
                return;
            }
            float swipeValue = (float) Math.sqrt(dX * dX + dY * dY);
            final float threshold = getThreshold(recyclerView, viewHolder);
            float fraction = swipeValue / threshold;
            //边界修正 最大为1
            if (fraction > 1) {
                fraction = 1;
            } else if (fraction < -1) {
                fraction = -1;
            }
//            Log.e(TAG, String.format("dx:%f,dy:%f,width:%d,height:%d,fraction:%f", dX, dY, recyclerView.getWidth(), recyclerView.getHeight(), fraction));
            //对每个ChildView进行缩放 位移
            int childCount = recyclerView.getChildCount();
//            recyclerView.gV
//            Log.e(TAG, String.format("childCount:%d", childCount));
            for (int i = 0; i < childCount; i++) {
                View child = recyclerView.getChildAt(i);
                //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
                int level = childCount - i - 1;
                if (level > 0) {
                    child.setScaleX(1 - SCALE_GAP * level + fraction * SCALE_GAP);
//                    if (level < MAX_SHOW_COUNT - 1) {
                    child.setScaleY(1 - SCALE_GAP * level + fraction * SCALE_GAP);
                    child.setTranslationY(transYGap * level - fraction * transYGap);
//                    } else {
//                        //child.setTranslationY((float) (mTranslationYGap * (level - 1) - fraction * mTranslationYGap));
//                    }
                } else {
                    //最上层
                    //rotate
                    float dRotation = dX < -50 ? -fraction * MAX_ROTATION : dX > 50 ? fraction * MAX_ROTATION : 0;
                    child.setRotation(dRotation);
                    if (mSwipeListener != null) {
                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                        final int adapterPosition = params.getViewAdapterPosition();
                        mSwipeListener.onSwipeTo(recyclerView.findViewHolderForAdapterPosition(adapterPosition), dX);
                    }
                }
            }
        }

        //扩展实现:点击按钮实现左滑效果
        public void toLeft(RecyclerView recyclerView) {
            if (check(recyclerView)) {
                animTo(recyclerView, false);
            }
        }

        //扩展实现:点击按钮实现右滑效果
        public void toRight(RecyclerView recyclerView) {
            if (check(recyclerView)) {
                animTo(recyclerView, true);
            }
        }

        private void animTo(final RecyclerView recyclerView, final boolean right) {
            final int position = recyclerView.getAdapter().getItemCount() - 1;
            final View view = recyclerView.findViewHolderForAdapterPosition(position).itemView;

            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, right ? 1f : -1f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1.3f);
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(100);
            translateAnimation.setInterpolator(new DecelerateInterpolator());
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isSwipeAnim = false;
                    Log.e(TAG, "结束");
                    recyclerView.removeView(view);

                    notifyListener(position,
                            right
                                    ?
                                    ItemTouchHelper.RIGHT : ItemTouchHelper.LEFT);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(translateAnimation);
        }

        private boolean check(RecyclerView recyclerView) {
            if (isSwipeAnim) {
                return false;
            }
            if (recyclerView == null || recyclerView.getAdapter() == null) {
                return false;
            }
            if (recyclerView.getAdapter().getItemCount() == 0) {
                return false;
            }
            isSwipeAnim = true;
            return true;
        }
    }

    public interface OnSwipeListener {
        /**
         * @param direction {@link ItemTouchHelper#LEFT} / {@link ItemTouchHelper#RIGHT}
         *                  {@link ItemTouchHelper#UP} or {@link ItemTouchHelper#DOWN}).
         */
        void onSwiped(int adapterPosition, int direction);

        /**
         * 最上层View滑动时回调.
         *
         * @param viewHolder 最上层的ViewHolder
         * @param offset     距离原始位置的偏移量
         */
        void onSwipeTo(RecyclerView.ViewHolder viewHolder, float offset);
    }

}
