package com.iwillow.app.android.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
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
 * Created by https://github.com/iwillow/ on 2017/1/9.
 */

public class SpeedSeekBar extends View {

    private ORIENTATION mOrientation;
    private Paint mBitmapPaint;
    private Bitmap mBackgroundBitmap;
    private Paint mLinePaint;
    private Paint mProgressLinePaint;

    private Bitmap mIndicatorBitmapWhite;
    private Bitmap mIndicatorBitmapBlue;
    private Bitmap mIndicatorBitmapDefault;
    private float mMotionDownX;
    private float mMotionDownY;
    private int mProgress = 50;
    private int mScale = 50;
    private int mSlop;
    private int mMax = 100;
    private boolean mIsDragging;
    private OnProgressChangeListener mProgressChangeListener;
    float mTouchProgressOffset;
    private float mLength;
    private float mPadding;
    private float mThin;
    private float mThick;
    private Matrix mBitmapMatrix = new Matrix();

    public SpeedSeekBar(Context context) {
        this(context, null);
    }

    public SpeedSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpeedSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }


    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SpeedSeekBar, defStyle, 0);
        int ori = a.getInt(R.styleable.SpeedSeekBar_layoutOrientation, 0);
        if (ori == 0) {
            mOrientation = ORIENTATION.HORIZONTAL;
        } else {
            mOrientation = ORIENTATION.VERTICAL;
        }
        a.recycle();
        mThin = dp2px(getResources(), 4);
        mThick = dp2px(getResources(), 5);
        mPadding = dp2px(getResources(), 32);

        mSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();


        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setStyle(Paint.Style.STROKE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.speed_seekbar_backgorund);

        mBitmapMatrix.reset();
        mBitmapMatrix.setScale(0.34f, 0.34f);
        mBackgroundBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mBitmapMatrix, true);


        Bitmap bitmapWhite = BitmapFactory.decodeResource(getResources(), R.drawable.speed_seekbar_indicator_white);
        mBitmapMatrix.reset();
        mBitmapMatrix.setScale(0.34f, 0.34f);
        mIndicatorBitmapWhite = Bitmap.createBitmap(bitmapWhite, 0, 0, bitmapWhite.getWidth(), bitmapWhite.getHeight(), mBitmapMatrix, true);


        Bitmap bitmapDefault = BitmapFactory.decodeResource(getResources(), R.drawable.speed_seekbar_indicator_default);
        mBitmapMatrix.reset();
        mBitmapMatrix.setScale(0.34f, 0.34f);
        mIndicatorBitmapDefault = Bitmap.createBitmap(bitmapDefault, 0, 0, bitmapDefault.getWidth(), bitmapDefault.getHeight(), mBitmapMatrix, true);


        Bitmap bitmapBlue = BitmapFactory.decodeResource(getResources(), R.drawable.speed_seekbar_indicator_blue);
        mBitmapMatrix.reset();
        mBitmapMatrix.setScale(0.34f, 0.34f);
        mIndicatorBitmapBlue = Bitmap.createBitmap(bitmapBlue, 0, 0, bitmapBlue.getWidth(), bitmapBlue.getHeight(), mBitmapMatrix, true);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(Color.GRAY);


        mProgressLinePaint = new Paint();
        mProgressLinePaint.setAntiAlias(true);
        mProgressLinePaint.setDither(true);
        mProgressLinePaint.setStyle(Paint.Style.FILL);
        mProgressLinePaint.setColor(Color.parseColor("#12aaeb"));


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
            if (mOrientation == ORIENTATION.HORIZONTAL) {
                width = (int) dp2px(getResources(), 240f);
            } else {
                width = (int) dp2px(getResources(), 60f);
            }
        } else {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            if (mOrientation == ORIENTATION.HORIZONTAL) {
                height = (int) dp2px(getResources(), 60);
            } else {
                height = (int) dp2px(getResources(), 240f);
            }
        } else {
            height = heightSize;
        }
        if (mOrientation == ORIENTATION.HORIZONTAL) {
            int size = Math.min(height, width / 4);
            width = size * 4;
            height = size;
        } else {
            int size = Math.min(height / 4, width);
            width = size;
            height = size * 4;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mOrientation == ORIENTATION.HORIZONTAL) {
            mLength = (getWidth()) * 0.5f - mPadding;
        } else {
            mLength = (getHeight()) * 0.5f - mPadding;
        }
    }

    public void setOrientation(ORIENTATION orientation) {
        if (orientation != mOrientation) {
            mOrientation = orientation;
            invalidate();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackgroundBitmap(canvas);
        drawLine(canvas);
        drawProgressLine(canvas, mScale);
        drawSpeedIndicator(canvas, mScale);

    }

    private void drawLine(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        RectF rectF;
        if (mOrientation == ORIENTATION.HORIZONTAL) {
            rectF = new RectF(-mLength, -mThin / 2, mLength, mThin / 2);
        } else {
            rectF = new RectF(-mThin / 2, -mLength, mThin / 2, mLength);
        }
        canvas.drawRect(rectF, mLinePaint);
        canvas.restore();
    }

    private void drawProgressLine(Canvas canvas, int progress) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        RectF rectF;
        if (mOrientation == ORIENTATION.HORIZONTAL) {
            rectF = new RectF(-mLength, -mThick / 2, -mLength + 2 * mLength * progress / 100, mThick / 2);
        } else {
            rectF = new RectF(-mThick / 2, -mLength, mThick / 2, -mLength + 2 * mLength * progress / 100);
        }
        canvas.drawRect(rectF, mProgressLinePaint);
        canvas.restore();
    }

    private void drawBackgroundBitmap(Canvas canvas) {
        if (mOrientation == ORIENTATION.HORIZONTAL) {
            drawHorizontalBackgroundBitmap(canvas);
        } else if (mOrientation == ORIENTATION.VERTICAL) {
            drawVerticalBackgroundBitmap(canvas);
        }
    }

    private void drawHorizontalBackgroundBitmap(Canvas canvas) {
        mBitmapMatrix.reset();
        mBitmapMatrix.postTranslate(getWidth() / 2 - mBackgroundBitmap.getWidth() / 2, getHeight() / 2 - mBackgroundBitmap.getHeight() / 2);
        canvas.drawBitmap(mBackgroundBitmap, mBitmapMatrix, mBitmapPaint);
    }

    private void drawVerticalBackgroundBitmap(Canvas canvas) {
        mBitmapMatrix.reset();
        mBitmapMatrix.postTranslate(getWidth() / 2 - mBackgroundBitmap.getWidth() / 2, getHeight() / 2 - mBackgroundBitmap.getHeight() / 2);
        mBitmapMatrix.postRotate(-90f, getWidth() / 2, getHeight() / 2);
        canvas.drawBitmap(mBackgroundBitmap, mBitmapMatrix, mBitmapPaint);
    }

    private void drawSpeedIndicator(Canvas canvas, int progress) {
        if (mOrientation == ORIENTATION.HORIZONTAL) {
            drawHorizontalSpeedIndicator(canvas, progress);
        } else if (mOrientation == ORIENTATION.VERTICAL) {
            drawVerticalSpeedIndicator(canvas, progress);
        }
    }

    private void drawHorizontalSpeedIndicator(Canvas canvas, int progress) {
        int left = (int) (mLength * 0.01f * (2 * progress - 100));
        if (progress == 50) {
            mBitmapMatrix.reset();
            mBitmapMatrix.postTranslate(getWidth() / 2 - mIndicatorBitmapDefault.getWidth() / 2, getHeight() / 2 - mIndicatorBitmapDefault.getHeight() / 2);
            canvas.drawBitmap(mIndicatorBitmapDefault, mBitmapMatrix, mBitmapPaint);
        } else if (progress < 50) {
            mBitmapMatrix.reset();
            mBitmapMatrix.postTranslate(getWidth() / 2 - mIndicatorBitmapWhite.getWidth() / 2 + left, getHeight() / 2 - mIndicatorBitmapWhite.getHeight() / 2);
            canvas.drawBitmap(mIndicatorBitmapWhite, mBitmapMatrix, mBitmapPaint);

        } else if (progress > 50) {
            mBitmapMatrix.reset();
            mBitmapMatrix.postTranslate(getWidth() / 2 - mIndicatorBitmapBlue.getWidth() / 2 + left, getHeight() / 2 - mIndicatorBitmapBlue.getHeight() / 2);
            canvas.drawBitmap(mIndicatorBitmapBlue, mBitmapMatrix, mBitmapPaint);
        }

    }

    private void drawVerticalSpeedIndicator(Canvas canvas, int progress) {
        int top = (int) (mLength * 0.01f * (2 * progress - 100));
        if (progress == 50) {
            mBitmapMatrix.reset();
            mBitmapMatrix.postTranslate(getWidth() / 2 - mIndicatorBitmapDefault.getWidth() / 2, getHeight() / 2 - mIndicatorBitmapDefault.getHeight() / 2);
            canvas.drawBitmap(mIndicatorBitmapDefault, mBitmapMatrix, mBitmapPaint);
        } else if (progress < 50) {
            mBitmapMatrix.reset();
            mBitmapMatrix.postTranslate(getWidth() / 2 - mIndicatorBitmapWhite.getWidth() / 2 + top, getHeight() / 2 - mIndicatorBitmapWhite.getHeight() / 2);
            mBitmapMatrix.postRotate(90f, getWidth() / 2, getHeight() / 2);
            canvas.drawBitmap(mIndicatorBitmapWhite, mBitmapMatrix, mBitmapPaint);
        } else if (progress > 50) {
            mBitmapMatrix.reset();
            mBitmapMatrix.postTranslate(getWidth() / 2 - mIndicatorBitmapBlue.getWidth() / 2 + top, getHeight() / 2 - mIndicatorBitmapBlue.getHeight() / 2);
            mBitmapMatrix.postRotate(90f, getWidth() / 2, getHeight() / 2);
            canvas.drawBitmap(mIndicatorBitmapBlue, mBitmapMatrix, mBitmapPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMotionDownX = event.getX();
                mMotionDownY = event.getY();
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
                    final float y = event.getY();
                    if (mOrientation == ORIENTATION.HORIZONTAL) {
                        if (Math.abs(x - mMotionDownX) > mSlop) {
                            setPressed(true);
                            invalidate();
                            onStartTrackingTouch();
                            trackTouchEvent(event);
                            attemptClaimDrag();
                        }
                    } else if (mOrientation == ORIENTATION.VERTICAL) {
                        if (Math.abs(y - mMotionDownY) > mSlop) {
                            setPressed(true);
                            invalidate();
                            onStartTrackingTouch();
                            trackTouchEvent(event);
                            attemptClaimDrag();
                        }
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

        }


        return true;
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
        if (mOrientation == ORIENTATION.HORIZONTAL) {
            trackHorizontalTouchEvent(event);
        } else if (mOrientation == ORIENTATION.VERTICAL) {
            trackVerticalTouchEvent(event);
        }
    }

    private void trackHorizontalTouchEvent(MotionEvent event) {
        final int width = getWidth();
        final int left = getPaddingLeft();
        final int right = getPaddingRight();
        final int available = width - left - right;
        // int x = (int) event.getX();
        int x = Math.round(event.getX());
        float scale;
        float progress = 0;
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


    private void trackVerticalTouchEvent(MotionEvent event) {
        final int height = getHeight();
        final int top = getPaddingTop();
        final int bottom = getPaddingBottom();
        final int available = height - top - bottom;
        int y = Math.round(event.getY());
        float scale;
        float progress = 0;
        if (y > height - bottom) {
            scale = 0.0f;
        } else if (y < top) {
            scale = 1.0f;
        } else {
            //scale = (float) (available - y + top) / (float) available;
            scale = 1f - (float) (y - top) / (float) available;
            progress = mTouchProgressOffset;
        }
        final int max = getMax();
        progress += scale * max;
        setProgress(max - Math.round(progress), true);

    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        setProgress(progress, false);
    }

    public int getMax() {
        return mMax;
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


    public static enum ORIENTATION {
        HORIZONTAL,
        VERTICAL
    }

    public interface OnProgressChangeListener {


        void onProgressChanged(SpeedSeekBar scaleView, int progress, boolean fromUser);

        void onStartTrackingTouch(SpeedSeekBar seekBar);

        void onStopTrackingTouch(SpeedSeekBar seekBar);
    }

}
