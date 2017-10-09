package com.iwillow.app.android.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by iwillow on 2017/10/9.
 */

public class ClipViewPager extends ViewPager {
    private final static float MIN_DISTANCE = 10;
    private float downX;
    private float downY;


    public ClipViewPager(Context context) {
        super(context);
    }

    public ClipViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getX();
            downY = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {

            float upX = ev.getX();
            float upY = ev.getY();

            if (Math.abs(upX - downX) > MIN_DISTANCE || Math.abs(upY - downY) > MIN_DISTANCE) {
                return super.dispatchTouchEvent(ev);
            }

            View view = getOnClickChildView(ev);
            if (view != null) {
                int index = (Integer) view.getTag();
                if (getCurrentItem() != index) {
                    setCurrentItem(index);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private View getOnClickChildView(MotionEvent ev) {
        int childCount = getChildCount();
        int currentIndex = getCurrentItem();
        int[] location = new int[2];
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            int position = (Integer) v.getTag();
            v.getLocationOnScreen(location);
            int minX = location[0];
            int minY = location[1];

            int maxX = location[0] + v.getWidth();
            int maxY = location[1] + v.getHeight();

            if (position < currentIndex) {
                maxX -= v.getWidth() * (1 - ScaleTransformer.MIN_SCALE_VALUE) * 0.5 + v.getWidth() * (Math.abs(1 - ScaleTransformer.MAX_SCALE_VALUE)) * 0.5;
                minX -= v.getWidth() * (1 - ScaleTransformer.MIN_SCALE_VALUE) * 0.5 + v.getWidth() * (Math.abs(1 - ScaleTransformer.MAX_SCALE_VALUE)) * 0.5;
            } else if (position == currentIndex) {
                minX += v.getWidth() * (Math.abs(1 - ScaleTransformer.MAX_SCALE_VALUE));
            } else if (position > currentIndex) {
                maxX -= v.getWidth() * (Math.abs(1 - ScaleTransformer.MAX_SCALE_VALUE)) * 0.5;
                minX -= v.getWidth() * (Math.abs(1 - ScaleTransformer.MAX_SCALE_VALUE)) * 0.5;
            }
            float x = ev.getRawX();
            float y = ev.getRawY();

            if ((x > minX && x < maxX) && (y > minY && y < maxY)) {
                return v;
            }
        }
        return null;
    }


    public static class ScaleTransformer implements ViewPager.PageTransformer {

        public static final float MAX_SCALE_VALUE = 0.85f;
        public static final float MIN_SCALE_VALUE = 0.65f;

        @Override
        public void transformPage(View page, float position) {

            //position [-1,1]才是需要变化的位置
            if (position < -1) {
                position = -1;
            } else if (position > 1) {
                position = 1;
            }
            float tempFraction = position < 0 ? 1 + position : 1 - position;
            float fraction = (MAX_SCALE_VALUE - MIN_SCALE_VALUE) / 1;
            float scaleValue = MIN_SCALE_VALUE + tempFraction * fraction;
            page.setScaleX(scaleValue);
            page.setScaleY(scaleValue);

        }
    }
}
