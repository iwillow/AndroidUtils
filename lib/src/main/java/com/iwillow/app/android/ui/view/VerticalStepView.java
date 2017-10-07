package com.iwillow.app.android.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import com.iwillow.app.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwillow on 2017/10/5.
 */

public class VerticalStepView extends View implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = VerticalStepView.class.getSimpleName();
    private TextPaint mTextPaint;
    private Paint mPaint;
    private List<StaticLayout> mStepLayouts = new ArrayList<>();
    private float mStepRadius;
    private float mMaxStepRadius;
    private float mMinStepRadius;
    private float mRadius;
    private float mHorizontalGap;
    private float mVerticalGap;
    private int mFinishedStep = 0;
    private int mUnfinishedLineColor;
    private int mFinishedLineColor;
    private int mAnimatorColor;
    private float mLineWidth;
    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    private ValueAnimator mStepAnimator;
    private boolean mRunning;
    private float mSpringY;
    private boolean mOnGlobalLayoutCalled = false;
    private StepItemsListener mStepItemsListener;

    public VerticalStepView(Context context) {
        this(context, null);
    }

    public VerticalStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VerticalStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        // float width = StaticLayout.getDesiredWidth(charSequence, textPaint);
        // staticLayout = new StaticLayout(charSequence, textPaint, 300, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        mMaxStepRadius = mRadius * 1.6f;
        mMinStepRadius = mRadius * 1.3f;
        mHorizontalGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        mVerticalGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        mUnfinishedLineColor = getResources().getColor(R.color.colorGray);
        mFinishedLineColor = getResources().getColor(R.color.colorOk);
        mAnimatorColor = getResources().getColor(R.color.colorOk1);


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.colorGray));
        mPaint.setAntiAlias(true);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics()));
        mTextPaint.setColor(Color.RED);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_finished);
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setStyle(Paint.Style.STROKE);

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height;
        if (mStepLayouts.size() > 1) {
            height = getPaddingTop() + getPaddingBottom() + (int) (2 * mRadius);
            for (StaticLayout layout : mStepLayouts) {
                height += layout.getHeight() + mVerticalGap;
            }
        } else {
            height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        int suggestedWidth = super.getSuggestedMinimumWidth();
        int defaultWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        return Math.max(suggestedWidth, defaultWidth);
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        int suggestedHeight = super.getSuggestedMinimumHeight();
        int defaultHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        return Math.max(suggestedHeight, defaultHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawCircle(canvas);
    }


    private void drawLine(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineWidth);
        int startX = (getPaddingLeft() + (int) mRadius);
        int startY = getPaddingTop() + (int) mRadius;
        mPaint.setColor(mUnfinishedLineColor);
        int height = startY;
        int finishY = startY;
        for (int i = 1; i < mStepLayouts.size(); i++) {
            height += mStepLayouts.get(i - 1).getHeight() + mVerticalGap;
            if (i <= mFinishedStep) {
                finishY += mStepLayouts.get(i - 1).getHeight() + mVerticalGap;
            }
        }
        mPaint.setColor(mFinishedLineColor);
        canvas.drawLine(startX, startY, startX, finishY, mPaint);
        mPaint.setColor(mUnfinishedLineColor);
        canvas.drawLine(startX, finishY, startX, height, mPaint);
    }

    private void drawCircle(Canvas canvas) {
        if (mStepLayouts.size() < 2) {
            return;
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int startX = (int) (getPaddingLeft() + mRadius);
        int x = (int) (paddingLeft + 2 * mRadius + mHorizontalGap);
        int height = paddingTop + (int) mRadius;
        if (mFinishedStep == 0) {
            mPaint.setColor(mFinishedLineColor);
            mSpringY = height + mRadius;
            canvas.drawCircle(paddingLeft + mRadius, mSpringY, mRadius, mPaint);
        } else {
            canvas.drawBitmap(mBitmap, startX - mBitmap.getWidth() / 2, height - mRadius, mBitmapPaint);
        }
        canvas.save();
        canvas.translate(x, height);
        mStepLayouts.get(0).draw(canvas);
        canvas.restore();

        for (int i = 1; i < mStepLayouts.size(); i++) {
            height += mStepLayouts.get(i - 1).getHeight() + mVerticalGap;
            if (i < mFinishedStep) {
                canvas.drawBitmap(mBitmap, startX - mBitmap.getWidth() / 2, height - mRadius, mBitmapPaint);
            } else if (i == mFinishedStep) {
                mPaint.setColor(mFinishedLineColor);
                mSpringY = height + mRadius;
                canvas.drawCircle(paddingLeft + mRadius, mSpringY, mRadius, mPaint);
            } else {
                mPaint.setColor(mUnfinishedLineColor);
                canvas.drawCircle(paddingLeft + mRadius, height + mRadius, mRadius, mPaint);
            }
            canvas.save();
            canvas.translate(x, height);
            mStepLayouts.get(i).draw(canvas);
            canvas.restore();
        }


        if (!mRunning || mFinishedStep == mStepLayouts.size()) {
            return;
        }
        mPaint.setColor(mAnimatorColor);
        canvas.drawCircle(paddingLeft + mRadius, mSpringY, mStepRadius, mPaint);
    }


    public void finishStep(int step) {
        if (step < 0 || step - 1 > mStepLayouts.size()) {
            throw new IllegalArgumentException("invalid step index:" + step);
        } else if (step != mFinishedStep) {
            mFinishedStep = step;
            if (step == mStepLayouts.size()) {
                stopSpringAnim();
            } else {
                resetSpringAnim();
            }
            invalidate();
        } else if (step == 0 && mFinishedStep == 0) {
            resetSpringAnim();
            invalidate();
        }
    }

    public void setItems(final List<String> items) {
        int maxTextWidth = (int) (getWidth() - getPaddingLeft() - getPaddingRight() - 2 * mRadius - mHorizontalGap);
        if (maxTextWidth < 0) {
            throw new IllegalStateException("Please call this method in Activity's onResume method or implements VerticalStepView.StepItemsListener's onStartLoadItems method !  ");
        } else if (items != null && items.size() > 1) {
            mStepLayouts.clear();
            for (String item : items) {
                StaticLayout layout = new StaticLayout(item, mTextPaint, maxTextWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                mStepLayouts.add(layout);
            }
            requestLayout();
        }
    }

    private void resetSpringAnim() {
        Log.d(TAG, "resetSpringAnim");
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

    private void stopSpringAnim() {
        if (mStepAnimator != null) {
            mStepAnimator.cancel();
            mStepAnimator = null;
            Log.d(TAG, "stopSpringAnim");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopSpringAnim();
    }

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        if (!mOnGlobalLayoutCalled) {
            mOnGlobalLayoutCalled = true;
            if (mStepItemsListener != null) {
                mStepItemsListener.onStartLoadItems(this);
            }
        }
    }

    public void setStepItemsListener(StepItemsListener stepItemsListener) {
        this.mStepItemsListener = stepItemsListener;
    }

    public interface StepItemsListener {
        void onStartLoadItems(VerticalStepView stepView);
    }

}
