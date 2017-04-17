package com.iwillow.app.android.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import com.iwillow.app.android.R;

import static com.iwillow.app.android.util.DimenUtil.dp2px;

/**
 * Created by https://github.com/iwillow/ on 2017/1/7.
 */

public class SpeedScaleView extends View {

    private Paint mIndicatorPaint;
    private Paint mThickPaint;
    private Paint mThinPaint;
    private float mPadding;
    private float mThickHeight;
    private float mThinHeight;
    private float mLength;
    private Bitmap mSpeedIndicatorBitmap;
    private float mMotionDownX;
    private int mProgress;
    private int mScale;
    private int mSlop;
    private boolean mIsDragging;
    private OnProgressChangeListener mProgressChangeListener;
    float mTouchProgressOffset;
    private int mMax = 100;
    private int mScaleCount = 10;

    public SpeedScaleView(Context context) {
        this(context, null);
    }

    public SpeedScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpeedScaleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mPadding = dp2px(getResources(), 5);
        mThickPaint = new Paint();
        mThickPaint.setAntiAlias(true);
        mThickPaint.setDither(true);
        mThickPaint.setColor(Color.parseColor("#FF12A9EA"));
        mThickPaint.setStyle(Paint.Style.FILL);
        mThickPaint.setStrokeWidth(dp2px(getResources(), 1.5f));
        mThickHeight = dp2px(getResources(), 12);

        mThinPaint = new Paint();
        mThinPaint.setAntiAlias(true);
        mThinPaint.setDither(true);
        mThinPaint.setColor(Color.parseColor("#FF12A9EA"));
        mThinPaint.setStyle(Paint.Style.FILL);
        mThinPaint.setStrokeWidth(dp2px(getResources(), 3f));
        mThinHeight = dp2px(getResources(), 6);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setDither(true);
        mIndicatorPaint.setStyle(Paint.Style.STROKE);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.speed_indicator);
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setScale(0.4f, 0.4f);
        mSpeedIndicatorBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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
            width = (int) dp2px(getResources(), 100f);
        } else {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            height = (int) dp2px(getResources(), 20f);
        } else {
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLength = (getWidth() / 2 - mPadding) * 0.8f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMotionDownX = event.getX();
                setPressed(true);
                invalidate();
                onStartTrackingTouch();
                trackTouchEvent(event);
                attemptClaimDrag();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDragging) {
                    trackTouchEvent(event);
                } else {
                    final float x = event.getX();
                    if (Math.abs(x - mMotionDownX) > mSlop) {
                        setPressed(true);
                        invalidate();
                        onStartTrackingTouch();
                        trackTouchEvent(event);
                        attemptClaimDrag();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    // Touch up when we never crossed the touch slop threshold
                    // should be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                invalidate();

                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScale(canvas);
        drawSpeedIndicator(canvas, mScale);
    }

    void onStartTrackingTouch() {
        mIsDragging = true;
        if (mProgressChangeListener != null) {
            mProgressChangeListener.onStartTrackingTouch(this);
        }
    }

    void onStopTrackingTouch() {
        mIsDragging = false;
        if (mProgressChangeListener != null) {
            mProgressChangeListener.onStopTrackingTouch(this);
        }
    }

    private void attemptClaimDrag() {
        ViewParent p = getParent();
        if (p != null) {
            p.requestDisallowInterceptTouchEvent(true);
        }
    }

    private void trackTouchEvent(MotionEvent event) {
        final int width = getWidth();
        final int left = getPaddingLeft();
        final int right = getPaddingRight();
        final int available = width - left - right;
        int x = Math.round(event.getX());
        float scale;
        float progress = 0f;
        // 下面是最小值
        if (x > width - right) {
            scale = 1.0f;
        } else if (x < left) {
            scale = 0.0f;
        } else {
            //  scale = 1 - (float) (available - x + left) / (float) available;
            scale = (float) (x - left) / (float) available;
            progress = mTouchProgressOffset;
        }
        final int max = getMax();
        progress += scale * max;
        setProgress(Math.round(progress), true);
    }


    private void drawScale(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        for (int i = -mScaleCount; i <= mScaleCount; i++) {
            float startX = mLength * i / mScaleCount;
            float startY = 0;
            float endX = mLength * i / mScaleCount;
            float endY;
            if (i % 2 == 0) {
                endY = -mThickHeight;
                canvas.drawLine(startX, startY, endX, endY, mThickPaint);
            } else {
                endY = -mThinHeight;
                canvas.drawLine(startX, startY, endX, endY, mThinPaint);
            }

        }
        canvas.restore();
    }

    public void setScaleCount(int scaleCount) {
        if (scaleCount < 0) {
            throw new IllegalArgumentException("the argument " + scaleCount + " must be bigger than 0");
        } else if (scaleCount != mScaleCount) {
            mScaleCount = scaleCount;
            invalidate();
        }
    }

    public int getScaleCount() {
        return mScaleCount;
    }

    private void drawSpeedIndicator(Canvas canvas, int progress) {
        int left = (int) (mLength * 0.01f * (2 * progress - 100) + getWidth() / 2 - mSpeedIndicatorBitmap.getWidth() / 2);
        int right = (int) (mLength * 0.01f * (2 * progress - 100) + getWidth() / 2 + mSpeedIndicatorBitmap.getWidth() / 2);
        int top = (int) (getHeight() / 2 + dp2px(getResources(), 2));
        int bottom = (int) (getHeight() / 2 + mSpeedIndicatorBitmap.getHeight() + dp2px(getResources(), 2));
        Rect dst = new Rect(left, top, right, bottom);
        canvas.drawBitmap(mSpeedIndicatorBitmap, null, dst, mIndicatorPaint);
    }

    public void setProgress(int progress) {
        setProgress(progress, false);
    }

    private void setProgress(int progress, boolean fromUser) {
        if (mProgress != progress) {
            mProgress = progress;
            if (mProgress < 0) {
                mProgress = 0;
            } else if (mProgress > getMax()) {
                mProgress = getMax();
            }
            if (mProgressChangeListener != null) {
                mProgressChangeListener.onProgressChanged(this, mProgress, fromUser);
            }
            mScale = 100 * progress / getMax();
            invalidate();
        }

    }

    public void setProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.mProgressChangeListener = onProgressChangeListener;
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != mMax) {
            mMax = max;
            postInvalidate();
        }
        if (mProgress > max) {
            mProgress = max;
        }
    }

    public int getProgress() {
        return mProgress;
    }

    public int getMax() {
        return mMax;
    }

    public interface OnProgressChangeListener {


        void onProgressChanged(SpeedScaleView scaleView, int progress, boolean fromUser);

        void onStartTrackingTouch(SpeedScaleView seekBar);


        void onStopTrackingTouch(SpeedScaleView seekBar);
    }
}
