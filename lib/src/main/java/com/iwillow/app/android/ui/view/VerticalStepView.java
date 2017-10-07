package com.iwillow.app.android.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
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
    private static final float MAX_RADIUS_FRACTION = 1.6f;
    private static final float MIN_RADIUS_FRACTION = 1.3f;
    private TextPaint mTextPaint;
    private int mTextColor;
    private float mTextSize;
    private Paint mPaint;
    private List<StaticLayout> mStepLayouts = new ArrayList<>();
    private float mAnimationRadius;
    private float mMaxStepRadius;
    private float mMinStepRadius;
    private float mRadius;
    private float mHorizontalGap;
    private float mVerticalGap;
    private int mFinishedStep = 0;
    private int mUnfinishedColor;
    private int mFinishedColor;
    private int mAnimationColor;
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
        init(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VerticalStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.VerticalStepView, defStyleAttr, 0);

        float defaultRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        mRadius = typedArray.getDimension(R.styleable.VerticalStepView_circle_radius, defaultRadius);
        mMaxStepRadius = mRadius * MAX_RADIUS_FRACTION;
        mMinStepRadius = mRadius * MIN_RADIUS_FRACTION;

        float defaultHorizontalGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        mHorizontalGap = typedArray.getDimension(R.styleable.VerticalStepView_horizontal_gap, defaultHorizontalGap);

        float defaultVerticalGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        mVerticalGap = typedArray.getDimension(R.styleable.VerticalStepView_vertical_gap, defaultVerticalGap);

        float defaultLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        mLineWidth = typedArray.getDimension(R.styleable.VerticalStepView_line_width, defaultLineWidth);

        int defaultUnfinishedColor = getResources().getColor(R.color.default_unfinished_color);
        mUnfinishedColor = typedArray.getColor(R.styleable.VerticalStepView_unfinished_color, defaultUnfinishedColor);

        int defaultFinishedColor = getResources().getColor(R.color.default_finished_color);
        mFinishedColor = typedArray.getColor(R.styleable.VerticalStepView_finished_color, defaultFinishedColor);

        int defaultAnimationColor = getResources().getColor(R.color.default_animation_color);
        mAnimationColor = typedArray.getColor(R.styleable.VerticalStepView_animation_color, defaultAnimationColor);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mUnfinishedColor);
        mPaint.setAntiAlias(true);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        float defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics());
        mTextSize = typedArray.getDimension(R.styleable.VerticalStepView_step_text_size, defaultTextSize);
        mTextPaint.setTextSize(mTextSize);
        mTextColor = typedArray.getColor(R.styleable.VerticalStepView_step_text_color, Color.RED);
        mTextPaint.setColor(mTextColor);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_finished);
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setStyle(Paint.Style.STROKE);

        typedArray.recycle();

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
        mPaint.setColor(mUnfinishedColor);
        int height = startY;
        int finishY = startY;
        for (int i = 1; i < mStepLayouts.size(); i++) {
            height += mStepLayouts.get(i - 1).getHeight() + mVerticalGap;
            if (i <= mFinishedStep) {
                finishY += mStepLayouts.get(i - 1).getHeight() + mVerticalGap;
            }
        }
        mPaint.setColor(mFinishedColor);
        canvas.drawLine(startX, startY, startX, finishY, mPaint);
        mPaint.setColor(mUnfinishedColor);
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
            mPaint.setColor(mFinishedColor);
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
                mPaint.setColor(mFinishedColor);
                mSpringY = height + mRadius;
                canvas.drawCircle(paddingLeft + mRadius, mSpringY, mRadius, mPaint);
            } else {
                mPaint.setColor(mUnfinishedColor);
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
        mPaint.setColor(mAnimationColor);
        canvas.drawCircle(paddingLeft + mRadius, mSpringY, mAnimationRadius, mPaint);
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
            mTextPaint.setColor(mTextColor);
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
                mAnimationRadius = (float) animation.getAnimatedValue();
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


    public void setHorizontalGap(float horizontalGap) {
        if (mHorizontalGap != horizontalGap) {
            mHorizontalGap = horizontalGap;
            invalidate();
        }
    }

    public void setVerticalGap(float verticalGap) {
        if (mVerticalGap != verticalGap) {
            mVerticalGap = verticalGap;
            requestLayout();
        }
    }

    public void setTextColor(@ColorInt int textColor) {
        if (mTextColor != textColor) {
            mTextColor = textColor;
            mTextPaint.setColor(textColor);
            invalidate();
        }
    }

    public void setTextSize(float textSize) {
        if (mTextSize != textSize) {
            mTextSize = textSize;
            mTextPaint.setTextSize(textSize);
            invalidate();
        }
    }

    public void setLineWidth(int lineWidth) {
        if (mLineWidth != lineWidth) {
            mLineWidth = lineWidth;
            invalidate();
        }
    }

    public void setAnimationColor(@ColorInt int animationColor) {
        if (mAnimationColor != animationColor) {
            mAnimationColor = animationColor;
            invalidate();
        }
    }

    public void setFinishedColor(@ColorInt int finishedColor) {
        if (mFinishedColor != finishedColor) {
            mFinishedColor = finishedColor;
            invalidate();
        }
    }

    public void setUnFinishedColor(@ColorInt int UnfinishedColor) {
        if (mUnfinishedColor != UnfinishedColor) {
            mUnfinishedColor = UnfinishedColor;
            invalidate();
        }
    }

    public void setRadius(float radius) {
        if (mRadius != radius) {
            mRadius = radius;
            mMaxStepRadius = mRadius * MAX_RADIUS_FRACTION;
            mMinStepRadius = mRadius * MIN_RADIUS_FRACTION;
            requestLayout();
            resetSpringAnim();
        }
    }


    public void setStepItemsListener(StepItemsListener stepItemsListener) {
        this.mStepItemsListener = stepItemsListener;
    }

    public interface StepItemsListener {
        void onStartLoadItems(VerticalStepView stepView);
    }

}
