package com.iwillow.app.android.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

/**
 * Created by ddx on 2017/3/30.
 */

public class CircleRadioView extends View {
    private boolean mChecked;
    private float mCircleRadius;
    private float mInnerCircleRadius;
    private float mTransitRadius;
    private boolean mTransition;
    private float mStokeWidth;
    private int mPaintColor = Color.GRAY;
    private Paint mPaint;
    private ValueAnimator mAnimator;
    private boolean mEnableControl = true;

    public CircleRadioView(Context context) {
        super(context);
        init();
    }

    public CircleRadioView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleRadioView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleRadioView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mStokeWidth = dp2px(getResources(), 3f);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            width = (int) dp2px(getResources(), 20);
        } else {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            height = (int) dp2px(getResources(), 20);
        } else {
            height = heightSize;
        }
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int p = getPaddingLeft();
        p = Math.max(p, getPaddingRight());
        p = Math.max(p, getPaddingBottom());
        p = Math.max(p, getPaddingTop());
        mCircleRadius = 1.0f * (1.0f * getWidth() / 2 - p) * 0.98f;
        mInnerCircleRadius = mCircleRadius * 2 / 3;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        if (mEnableControl) {
            mPaint.setColor(mPaintColor);
        } else {
            mPaint.setColor(Color.GRAY);
        }
        mPaint.setStrokeWidth(mStokeWidth);
        canvas.drawCircle(0, 0, mCircleRadius, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        if (mTransition) {
            canvas.drawCircle(0, 0, mTransitRadius, mPaint);
        } else {
            if (mChecked) {
                canvas.drawCircle(0, 0, mInnerCircleRadius, mPaint);
            }
        }
        canvas.restore();
    }

    public void setPaintColor(@ColorInt int paintColor) {
        if (mPaintColor != paintColor) {
            mPaintColor = paintColor;
            invalidate();
        }
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        if (mChecked == checked) {
            return;
        }
        this.mChecked = checked;
       /* invalidate();*/
        if (mAnimator != null) {
            if (mAnimator.isRunning()) {
                mAnimator.cancel();
            }
            mAnimator = null;
        }
        final float startValue;
        final float stopValue;
        if (checked) {
            startValue = 0;
            stopValue = mInnerCircleRadius;
        } else {
            startValue = mInnerCircleRadius;
            stopValue = 0;
        }
        mTransition = true;
        mAnimator = ValueAnimator.ofFloat(startValue, stopValue);
        mAnimator.setDuration(300);
        mAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTransitRadius = (float) animation.getAnimatedValue();
                mTransition = true;
                invalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mTransitRadius = startValue;
                mAnimator = null;
                mTransition = false;
                invalidate();

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTransitRadius = stopValue;
                mAnimator = null;
                mTransition = false;
                invalidate();

            }
        });
        mAnimator.start();
    }

    private float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public void enableControl(boolean enableControl) {
        if (mEnableControl != enableControl) {
            mEnableControl = enableControl;
            invalidate();
        }
    }

    public boolean isEnableControl() {
        return mEnableControl;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null) {
            if (mAnimator.isRunning()) {
                mAnimator.cancel();
            }
            mAnimator = null;
        }
    }
}
