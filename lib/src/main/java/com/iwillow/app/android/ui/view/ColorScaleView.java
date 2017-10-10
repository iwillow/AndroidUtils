package com.iwillow.app.android.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.iwillow.app.android.R;

import static com.iwillow.app.android.util.DimenUtil.dp2px;


/**
 * Created by iwillow on 2017/3/29.
 */

public class ColorScaleView extends View {
    public static final String TAG = ColorScaleView.class.getSimpleName();
    private float mCircleRadius;
    private float mInnerCircleRadius;
    private int[] mColorArray;
    private Paint mPaint;
    private float mCenterX;
    private float mCenterY;
    private float mStickHeight;
    private float mStickWidth;
    private int mCurrentIndex;
    private float mIndicatorHeight;
    private Path mPath;
    private float mStokeWidth;
    private float mMotionDownX;
    private float mMotionDownY;
    private int mSlop;
    private boolean mMoving;
    private boolean mOnSticksTouched;
    public OnColorSelectedListener mOnColorSelectedListener;
    private ValueAnimator mAnimator;
    private boolean mEnableControl = true;


    public ColorScaleView(Context context) {
        this(context, null);

    }

    public ColorScaleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        mSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mColorArray = getResources().getIntArray(R.array.color_values);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStickWidth = dp2px(getResources(), 3);
        mStokeWidth = dp2px(getResources(), 0.8f);
        mPath = new Path();
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
            width = (int) dp2px(getResources(), 150);
        } else {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            height = (int) dp2px(getResources(), 150);
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
        mCircleRadius = 1.0f * (1.0f * getWidth() / 2 - p) * 0.95f;
        mInnerCircleRadius = mCircleRadius * 2 / 3;
        mStickHeight = mCircleRadius / 5;
        mIndicatorHeight = mCircleRadius * 0.050f;
        mCenterX = getWidth() * 0.5f;
        mCenterY = getHeight() * 0.5f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnableControl) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMotionDownX = event.getX();
                mMotionDownY = event.getY();
                if (!mMoving) {
                    mOnSticksTouched = isSticksTouched(event);
                    if (mOnSticksTouched) {
                        calibrateMotion(event);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMoving) {
                    calibrateMotion(event);
                } else if (mOnSticksTouched && canMove(event)) {
                    mMoving = true;
                    // Log.d(TAG, "已经选中");
                    calibrateMotion(event);
                } else {
                    Log.d(TAG, "没有选中");
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mMotionDownX = 0;
                mMotionDownY = 0;
                mMoving = false;
                if (mOnSticksTouched) {
                    if (mOnColorSelectedListener != null) {
                        mOnColorSelectedListener.onColorSelected(mCurrentIndex, mColorArray[mCurrentIndex]);
                    }
                    mOnSticksTouched = false;
                }
                break;
        }

        return true;
    }

    private boolean canMove(MotionEvent event) {
        return ((event.getX() - mMotionDownX) * (event.getX() - mMotionDownX) + (event.getY() - mMotionDownY) * (event.getY() - mMotionDownY) > mSlop * mSlop);
    }

    private void calibrateMotion(MotionEvent event) {
        if (mColorArray == null) {
            return;
        }
        if (mColorArray.length == 0) {
            return;
        }

        float x = event.getX();
        float y = event.getY();
        double theta = 0;
        if (Math.abs(x - mCenterX) < 0.0001) {
            if (y < mCenterY) {
                theta = 0f;//negative Y axis

            } else {
                theta = Math.PI;//positive Y axis
            }
        } else if (Math.abs(y - mCenterY) < 0.0001) {
            if (x < mCenterX) {
                theta = Math.PI * 1.5;//negative X axis
            } else {
                theta = Math.PI * 0.5;//positive X axis
            }
        } else {
            float deltaX;
            float deltaY;
            if (x > mCenterX && y < mCenterY) {
                deltaX = x - mCenterX;
                deltaY = mCenterY - y;
                theta = Math.abs(Math.atan(deltaX / deltaY));
            } else if (x > mCenterX && y > mCenterY) {
                deltaX = x - mCenterX;
                deltaY = y - mCenterY;
                theta = Math.PI - Math.abs(Math.atan(deltaX / deltaY));
            } else if (x < mCenterX && y > mCenterY) {
                deltaX = mCenterX - x;
                deltaY = y - mCenterY;
                theta = Math.PI + Math.abs(Math.atan(deltaX / deltaY));
            } else if (x < mCenterX && y < mCenterY) {
                deltaX = mCenterX - x;
                deltaY = mCenterY - y;
                theta = 2 * Math.PI - Math.abs(Math.atan(deltaX / deltaY));
            }
        }
        double unit = 2 * Math.PI / mColorArray.length;
        double count = theta / unit;
        int index = (int) Math.round(count) - 1;
        if (index > (mColorArray.length - 1)) {
            index = mColorArray.length - 1;
        } else if (index < 0) {
            index = 0;
        }
        mCurrentIndex = index;
        if (mOnColorSelectedListener != null) {
            mOnColorSelectedListener.onColorChanged(mCurrentIndex, mColorArray[mCurrentIndex]);
        }
        invalidate();
    }

    private boolean isSticksTouched(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float distance = (float) Math.sqrt((x - mCenterX) * (x - mCenterX) + (y - mCenterY) * (y - mCenterY));
        return distance >= mInnerCircleRadius && distance <= mInnerCircleRadius + mStickHeight;
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        mOnColorSelectedListener = listener;
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOuterCircle(canvas);
        drawSticks(canvas);
        drawTriangle(canvas);
        drawLooper(canvas);
    }

    private void drawOuterCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(dp2px(getResources(), 0.3f));
        canvas.drawCircle(0, 0, mCircleRadius, mPaint);
        canvas.restore();
    }


    private void drawSticks(Canvas canvas) {
        if (mColorArray != null && mColorArray.length > 0) {
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(mStickWidth);
            float degree = 360f / mColorArray.length;
            for (int i = 0; i < mColorArray.length; i++) {
                canvas.save();
                canvas.translate(getWidth() / 2, getHeight() / 2);
                canvas.rotate(i * degree);
                if (mEnableControl) {
                    mPaint.setColor(mColorArray[i]);
                } else {
                    mPaint.setColor(Color.GRAY);
                }
                canvas.drawLine(0, -mInnerCircleRadius, 0, -mInnerCircleRadius - mStickHeight, mPaint);
                canvas.drawCircle(0, -mInnerCircleRadius, mStickWidth * 0.5f, mPaint);
                canvas.drawCircle(0, -mInnerCircleRadius - mStickHeight, mStickWidth * 0.5f, mPaint);
                canvas.restore();
            }
        }
    }

    private void drawTriangle(Canvas canvas) {
        if (mColorArray != null && mColorArray.length > 0) {
            canvas.save();
            canvas.translate(getWidth() / 2, getHeight() / 2);
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            if (mEnableControl) {
                float degree = 360f / mColorArray.length;
                canvas.rotate(mCurrentIndex * degree);
                mPaint.setColor(mColorArray[mCurrentIndex]);
            } else {
                canvas.rotate(0);
                mPaint.setColor(Color.GRAY);
            }
            mPath.reset();
            mPath.moveTo(0, mIndicatorHeight - mCircleRadius);
            float left = (float) (0.5f * Math.sqrt(3) * mIndicatorHeight);
            mPath.lineTo(-left, -mCircleRadius);
            mPath.lineTo(left, -mCircleRadius);
            mPath.close();
            canvas.drawPath(mPath, mPaint);
            canvas.restore();
        }
    }


    private void drawLooper(Canvas canvas) {
        if (mColorArray != null && mColorArray.length > 0) {
            canvas.save();
            canvas.translate(getWidth() / 2, getHeight() / 2);
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStokeWidth);
            if (mEnableControl) {
                float degree = 360f / mColorArray.length;
                canvas.rotate(mCurrentIndex * degree);
                mPaint.setColor(mColorArray[mCurrentIndex]);
            } else {
                canvas.rotate(0);
                mPaint.setColor(Color.GRAY);
            }
            float size = 3 * mStickWidth;
            canvas.drawLine(-2 * mStickWidth, -mInnerCircleRadius + 1.5f * mStickWidth, -2 * mStickWidth, -mInnerCircleRadius - mStickHeight - 1.5f * mStickWidth, mPaint);
            canvas.drawLine(2 * mStickWidth, -mInnerCircleRadius + 1.5f * mStickWidth, 2 * mStickWidth, -mInnerCircleRadius - mStickHeight - 1.5f * mStickWidth, mPaint);
            RectF bottomRef = new RectF(-2 * mStickWidth, -mInnerCircleRadius, 2 * mStickWidth, -mInnerCircleRadius + size);
            canvas.drawArc(bottomRef, 0, 180, false, mPaint);
            RectF topRef = new RectF(-2 * mStickWidth, -mInnerCircleRadius - mStickHeight - size, 2 * mStickWidth, -mInnerCircleRadius - mStickHeight);
            canvas.drawArc(topRef, 180, 180, false, mPaint);
            canvas.restore();
        }

    }


    public void setCurrentIndex(int currentIndex) {
        if (mColorArray != null && mColorArray.length > 0) {
            if (currentIndex < 0) {
                currentIndex = 0;
            } else if (currentIndex > mColorArray.length - 1) {
                currentIndex = mColorArray.length - 1;
            }
            if (mCurrentIndex != currentIndex) {
                mCurrentIndex = currentIndex;
                if (mOnColorSelectedListener != null) {
                    mOnColorSelectedListener.onColorChanged(mCurrentIndex, mColorArray[mCurrentIndex]);
                }
                invalidate();
               /* if (mAnimator != null) {
                    if (mAnimator.isRunning()) {
                        mAnimator.cancel();
                    }
                    mAnimator = null;

                }

                final int startValue = mCurrentIndex;
                final int stopValue = currentIndex;
                mAnimator = ValueAnimator.ofInt(startValue, stopValue);
                mAnimator.setDuration(300);
                mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mCurrentIndex = (int) animation.getAnimatedValue();
                        if (mOnColorSelectedListener != null) {
                            mOnColorSelectedListener.onColorChanged(mCurrentIndex, mColorArray[mCurrentIndex]);
                        }
                        invalidate();
                    }
                });
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        mCurrentIndex = startValue;
                        if (mOnColorSelectedListener != null) {
                            mOnColorSelectedListener.onColorChanged(mCurrentIndex, mColorArray[mCurrentIndex]);
                        }
                        invalidate();
                        mAnimator = null;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mCurrentIndex = stopValue;
                        if (mOnColorSelectedListener != null) {
                            mOnColorSelectedListener.onColorChanged(mCurrentIndex, mColorArray[mCurrentIndex]);
                        }
                        invalidate();
                        mAnimator = null;
                    }
                });
                mAnimator.start();*/
            }
        }
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

    public int getColor(int index) {
        if (mColorArray != null && mColorArray.length > 0 && index >= 0 && index < mColorArray.length) {
            return mColorArray[index];
        }
        return Color.TRANSPARENT;
    }


    public interface OnColorSelectedListener {

        public void onColorChanged(int index, @ColorInt int color);

        public void onColorSelected(int index, @ColorInt int color);
    }

}
