package com.iwillow.app.android.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.iwillow.app.android.R;

import java.util.ArrayList;
import java.util.List;

import static com.iwillow.app.android.util.DimenUtil.dp2px;
import static com.iwillow.app.android.util.DimenUtil.sp2px;

/**
 * Created by https://github.com/iwillow/ on 2017/4/17.
 */

public class StepView extends View {
    private float mMaxPadding;
    private List<String> mItems = new ArrayList<>();
    private int mCurrentStep;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private float mLineWidth;
    private float mCircleRadius;
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private float mDistance;
    private ValueAnimator mStepAnimator;
    private float mStepRadius;
    private float mMaxStepRadius;
    private float mMinStepRadius;
    private boolean mRunning;
    private int mTextColor;
    private int mLineColor;
    private int mCurrentStepColor;
    private int mNextStepColor;
    private int mAnimatorColor;

    public StepView(Context context) {
        super(context);
        init();
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLineColor = getResources().getColor(R.color.colorGray);
        mCurrentStepColor = getResources().getColor(R.color.colorOk);
        mNextStepColor = getResources().getColor(R.color.colorGray);
        mAnimatorColor = getResources().getColor(R.color.colorOk1);
        mPaint.setColor(getResources().getColor(R.color.colorGray));
        mPaint.setAntiAlias(true);
        mLineWidth = dp2px(getResources(), 1f);
        mCircleRadius = dp2px(getResources(), 5f);
        mMaxStepRadius = mCircleRadius + dp2px(getResources(), 6f);
        mMinStepRadius = mCircleRadius + dp2px(getResources(), 3f);
        mDistance = dp2px(getResources(), 10f);
        mCurrentStep = 0;
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextColor = getResources().getColor(R.color.colorGray);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(sp2px(getResources(), 12f));

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_finished);
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            width = (int) dp2px(getResources(), 250);
        } else {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            height = (int) dp2px(getResources(), 50);
        } else {
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int p = getPaddingLeft();
        p = Math.max(p, getPaddingRight());
        p = Math.max(p, getPaddingBottom());
        mMaxPadding = dp2px(getResources(), 30) + Math.max(p, getPaddingTop());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawStepItems(canvas);
        drawSpringAnimation(canvas);
    }

    public void setTextColor(@ColorInt int textColor) {
        if (mTextColor != textColor) {
            mTextColor = textColor;
            invalidate();
        }
    }

    public void setLineColor(@ColorInt int lineColor) {
        if (mLineColor != lineColor) {
            mLineColor = lineColor;
            invalidate();
        }
    }

    public void setLineWidth(float lineWidth) {
        if (mLineWidth != lineWidth && lineWidth > 0) {
            mLineWidth = lineWidth;
            invalidate();
        }
    }

    public void setCurrentStepColor(@ColorInt int currentStepColor) {
        if (mCurrentStepColor != currentStepColor) {
            mCurrentStepColor = currentStepColor;
            invalidate();
        }
    }

    public void setNextStepColor(@ColorInt int nextStepColor) {
        if (mNextStepColor != nextStepColor) {
            mNextStepColor = nextStepColor;
            invalidate();
        }
    }

    private void drawLine(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float startX = mMaxPadding - 0.5f * getWidth();
        float endX = 0.5f * getWidth() - mMaxPadding;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(mLineWidth);
        canvas.drawLine(startX, -mDistance, endX, -mDistance, mPaint);
        canvas.restore();
    }

    private void drawStepItems(Canvas canvas) {
        if (mItems == null || mItems.size() < 2) {
            return;
        }
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float startX = mMaxPadding - 0.5f * getWidth();
        float segment = (getWidth() - 2f * mMaxPadding) / (mItems.size() - 1);
        mPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float height = fontMetrics.descent - fontMetrics.ascent + mDistance;
        for (int i = 0; i < mItems.size(); i++) {
            String item = mItems.get(i);
            float cx = startX + i * segment;
            if (i < mCurrentStep) {
                canvas.drawBitmap(mBitmap, cx - 0.5f * mBitmap.getWidth(), -0.5f * mBitmap.getHeight() - mDistance, mBitmapPaint);
            } else if (i == mCurrentStep) {
                mPaint.setColor(mCurrentStepColor);
                canvas.drawCircle(cx, -mDistance, mCircleRadius, mPaint);
            } else {
                mPaint.setColor(mNextStepColor);
                canvas.drawCircle(cx, -mDistance, mCircleRadius, mPaint);
            }
            canvas.drawText(item, cx, height, mTextPaint);
        }
        canvas.restore();
    }

    public void setItems(List<String> items) {
        if (items == null) {
            throw new NullPointerException("StepItems cannot be null");
        } else if (items.size() < 2) {
            throw new IllegalArgumentException("The amount of items cannot be less than 2");
        } else {
            mItems.clear();
            mItems.addAll(items);
            resetAnim();
            invalidate();
        }
    }

    private void drawSpringAnimation(Canvas canvas) {
        if (!mRunning) {
            return;
        }
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float startX = mMaxPadding - 0.5f * getWidth();
        float segment = (getWidth() - 2f * mMaxPadding) / (mItems.size() - 1);
        float cx = startX + mCurrentStep * segment;
        mPaint.setColor(mAnimatorColor);
        canvas.drawCircle(cx, -mDistance, mStepRadius, mPaint);
        canvas.restore();
    }

    public void setCurrentStep(int step) {
        if (step < 0 || step > mItems.size() - 1) {
            throw new IllegalArgumentException("invalid step index:" + step);
        } else if (step != mCurrentStep) {
            mCurrentStep = step;
            resetAnim();
            invalidate();
        }
    }

    private void resetAnim() {
        if (mStepAnimator != null) {
            mStepAnimator.cancel();
        } else {
            mStepAnimator = ValueAnimator.ofFloat(mMinStepRadius, mMaxStepRadius);
        }
        mStepAnimator.setDuration(1000);
        mStepAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mStepAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mStepAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStepRadius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        mStepAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mRunning = false;
                invalidate();
            }
        });
        mRunning = true;
        mStepAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mStepAnimator != null) {
            if (mStepAnimator.isRunning()) {
                mStepAnimator.cancel();
            }
            mStepAnimator = null;
        }
    }
}