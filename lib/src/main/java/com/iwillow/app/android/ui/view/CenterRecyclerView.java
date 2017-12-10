package com.iwillow.app.android.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 一款可以让被点击的item滑动到中间的接口
 * Created by iwillow on 2017/12/10.
 */

public class CenterRecyclerView extends RecyclerView {

    private final static String TAG = "CenterRecyclerView";

    private int mLastPosition = 0;

    private ValueAnimator mValueAnimator;

    public CenterRecyclerView(Context context) {
        this(context, null);
    }

    public CenterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void smoothScrollItemToCenter(final int position) {
        scrollItemToCenter(position, true);
    }


    public void scrollItemToCenter(final int position, boolean smooth) {

        if (mLastPosition == position) {
            return;
        }


        if (getLayoutManager() != null && getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            View itemView = layoutManager.findViewByPosition(position);
            if (itemView == null) {
                return;
            }
            if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {

                if (!canScrollVertically(1) && !canScrollVertically(-1)) {
                    return;
                }

                int scrollY = computeVerticalScrollOffset();
                Log.d(TAG, "scrollY=" + scrollY);
                int centerY = getMeasuredHeight() / 2;
                mLastPosition = position;
                final int endOffset = centerY - itemView.getMeasuredHeight() / 2;
                int itemViewCenterY = (int) itemView.getY() + itemView.getMeasuredHeight() / 2;
                if (itemViewCenterY < centerY) { //向下滑动
                    Log.d(TAG, "向下滑动");
                    if (scrollY == 0) {
                        Log.d(TAG, "已经滑动到最顶部");
                        return;
                    }
                } else if (itemViewCenterY > centerY) {//向上滑动
                    Log.d(TAG, "向上滑动");
                    int bottomDistance = computeVerticalScrollRange() - computeVerticalScrollExtent() - scrollY;
                    if (bottomDistance <= 0) {
                        Log.d(TAG, "已经滑动到最底部");
                        return;
                    }
                } else {
                    Log.d(TAG, "已经居中");
                }

                if (smooth) {
                    stopInternalScrollAnimator();
                    startInternalScrollAnimator(layoutManager, position, itemViewCenterY, endOffset);
                } else {
                    layoutManager.scrollToPositionWithOffset(position, endOffset);
                }


            } else if (layoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {

                if (!canScrollHorizontally(1) && !canScrollHorizontally(-1)) {
                    return;
                }

                int scrollX = computeHorizontalScrollOffset();
                Log.d(TAG, "scrollX=" + scrollX);
                int centerX = getMeasuredWidth() / 2;
                mLastPosition = position;
                final int endOffset = centerX - itemView.getMeasuredWidth() / 2;
                int itemViewCenterX = (int) itemView.getX() + itemView.getMeasuredWidth() / 2;
                if (itemViewCenterX < centerX) { //向右滑动
                    Log.d(TAG, "向右滑动");
                    if (scrollX == 0) {
                        Log.d(TAG, "已经滑动到最左边缘");
                        return;
                    }
                } else if (itemViewCenterX > centerX) {//向左滑动
                    Log.d(TAG, "向左滑动");
                    int rightDistance = computeHorizontalScrollRange() - computeHorizontalScrollExtent() - scrollX;
                    if (rightDistance <= 0) {
                        Log.d(TAG, "已经滑动到最右边缘");
                        return;
                    }

                } else {
                    Log.d(TAG, "已经居中");
                }
                if (smooth) {
                    stopInternalScrollAnimator();
                    startInternalScrollAnimator(layoutManager, position, itemViewCenterX, endOffset);
                } else {
                    layoutManager.scrollToPositionWithOffset(position, endOffset);
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopInternalScrollAnimator();
    }

    private void stopInternalScrollAnimator() {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    private void startInternalScrollAnimator(final LinearLayoutManager layoutManager, final int position, final int startOffset, final int endOffset) {
        mValueAnimator = ValueAnimator.ofInt(startOffset, endOffset);
        mValueAnimator.setDuration(250);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int offsetValue = (int) animation.getAnimatedValue();
                layoutManager.scrollToPositionWithOffset(position, offsetValue);
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                layoutManager.scrollToPositionWithOffset(position, endOffset);

            }
        });
        mValueAnimator.start();
    }
}
