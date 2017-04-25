package com.iwillow.app.android.ui.view;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.iwillow.app.android.R;

import static com.iwillow.app.android.util.DimenUtil.dp2px;


/**
 * Created by https://github.com/iwillow/ on 2017/1/5.
 */

public class GestureInstrumentView extends View {

    public static final String TAG = "GestureInstrumentView";

    private float mRadius;
    private float mContainerPadding;
    private Paint mPaintInnerCircle;
    private Paint mPaintInnerOuterCircle;
    private Paint mPaintDot;
    private Paint mPaintControlCircle;
    private Paint mPaintControlCircleInner;
    private Paint mBitmapPaint;

    private float mTheta;
    private float mProgressX;
    private float mProgressY;
    private float mDotHorizontalGap;
    private float mDotWidth;
    private float mDotThickness;

    private int mSlop;
    private float mCircleX;
    private float mCircleY;
    private float mMotionDownX;
    private float mMotionDownY;
    private boolean mMoving;
    private boolean mOnInnerCircleTouched;
    private float mMaxDistance;

    private float mControlCircleX;
    private float mControlCircleY;

    private float mRadiusControl;
    private float mRadiusControlInner;
    private Bitmap mBackgroundScaleBmp;
    private Bitmap mLeftRightScaleBitmap;
    private Bitmap mUpDownScaleBitmap;
    private OnInnerCircleMoveListener mOnInnerCircleMoveListener;
    public boolean mEnableControl;
    private Matrix mMatrix = new Matrix();
    private boolean mGestureMode = true;
    private float mInnerOuterCircleStokeWidth;
    private float mControlCircleStokeWidth;
    private int mInnerCircleColor;
    private int mInnerOuterCircleColor;
    private int mControlCircleColor;
    private int mControlCircleInnerColor;
    private int mDotColor;
    private float mRadiusFraction = 4.00f;
    private float mUpDownBitmapOffSet;

    public GestureInstrumentView(Context context) {
        this(context, null);
    }

    public GestureInstrumentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureInstrumentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GestureInstrumentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GestureInstrumentView, defStyleAttr, 0);

        mSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mContainerPadding = a.getDimension(R.styleable.GestureInstrumentView_gestureInstrumentView_containerPadding, dp2px(getResources(), 5));

        mDotHorizontalGap = a.getDimension(R.styleable.GestureInstrumentView_gestureInstrumentView_dotHorizontalGap, dp2px(getResources(), 4f));
        mDotWidth = a.getDimension(R.styleable.GestureInstrumentView_gestureInstrumentView_dotWidth, dp2px(getResources(), 0.5f));
        mDotThickness = a.getDimension(R.styleable.GestureInstrumentView_gestureInstrumentView_dotThickness, dp2px(getResources(), 0.5f));
        mDotColor = a.getColor(R.styleable.GestureInstrumentView_gestureInstrumentView_dotColor, getResources().getColor(R.color.dotColor));


        mInnerCircleColor = a.getColor(R.styleable.GestureInstrumentView_gestureInstrumentView_innerCircleColor, getResources().getColor(R.color.default_innerCircleColor));
        mInnerOuterCircleColor = a.getColor(R.styleable.GestureInstrumentView_gestureInstrumentView_innerOuterCircleColor, getResources().getColor(R.color.default_innerOuterCircleColor));
        mInnerOuterCircleStokeWidth = a.getDimension(R.styleable.GestureInstrumentView_gestureInstrumentView_innerOuterCircleStokeWidth, dp2px(getResources(), 3f));

        mControlCircleColor = a.getColor(R.styleable.GestureInstrumentView_gestureInstrumentView_controlCircleColor, Color.GRAY);
        mControlCircleStokeWidth = a.getDimension(R.styleable.GestureInstrumentView_gestureInstrumentView_controlCircleStokeWidth, dp2px(getResources(), 3f));

        mControlCircleInnerColor = a.getColor(R.styleable.GestureInstrumentView_gestureInstrumentView_controlCircleInnerColor, Color.WHITE);

        a.recycle();

        mPaintInnerCircle = new Paint();
        mPaintInnerCircle.setAntiAlias(true);
        mPaintInnerCircle.setDither(true);
        mPaintInnerCircle.setColor(mInnerCircleColor);
        mPaintInnerCircle.setStyle(Paint.Style.FILL);


        mPaintInnerOuterCircle = new Paint();
        mPaintInnerOuterCircle.setAntiAlias(true);
        mPaintInnerOuterCircle.setDither(true);
        mPaintInnerOuterCircle.setColor(mInnerOuterCircleColor);
        mPaintInnerOuterCircle.setStyle(Paint.Style.STROKE);
        mPaintInnerOuterCircle.setStrokeWidth(mInnerOuterCircleStokeWidth);


        mPaintDot = new Paint();
        mPaintDot.setAntiAlias(true);
        mPaintDot.setDither(true);
        mPaintDot.setColor(mDotColor);
        mPaintDot.setStyle(Paint.Style.FILL);
        mPaintDot.setStrokeWidth(mDotThickness);

        mPaintControlCircle = new Paint();
        mPaintControlCircle.setAntiAlias(true);
        mPaintControlCircle.setDither(true);
        mPaintControlCircle.setColor(mControlCircleColor);
        mPaintControlCircle.setStyle(Paint.Style.STROKE);
        mPaintControlCircle.setStrokeWidth(mControlCircleStokeWidth);


        mPaintControlCircleInner = new Paint();
        mPaintControlCircleInner.setAntiAlias(true);
        mPaintControlCircleInner.setDither(true);
        mPaintControlCircleInner.setColor(mControlCircleInnerColor);
        mPaintControlCircleInner.setStyle(Paint.Style.FILL);


        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setStyle(Paint.Style.STROKE);

        Bitmap backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_mode_bitmap_background);
        mMatrix.reset();
        mMatrix.setScale(0.35f, 0.35f);
        mBackgroundScaleBmp = Bitmap.createBitmap(backgroundBitmap, 0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), mMatrix, true);


        Bitmap leftRightBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_mode_bitmap_left_right);
        mMatrix.reset();
        mMatrix.setScale(0.35f, 0.35f);
        mLeftRightScaleBitmap = Bitmap.createBitmap(leftRightBitmap, 0, 0, leftRightBitmap.getWidth(), leftRightBitmap.getHeight(), mMatrix, true);


        Bitmap upDownBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_mode_bitmap_up_down);
        mMatrix.reset();
        mMatrix.setScale(0.35f, 0.35f);
        mUpDownScaleBitmap = Bitmap.createBitmap(upDownBitmap, 0, 0, upDownBitmap.getWidth(), upDownBitmap.getHeight(), mMatrix, true);
        mUpDownBitmapOffSet = dp2px(getResources(), 5);
    }

    public void setDotColor(@ColorInt int dotColor) {
        if (dotColor != mDotColor) {
            mDotColor = dotColor;
            invalidate();
        }
    }

    public void setControlCircleColor(@ColorInt int controlCircleColor) {
        if (controlCircleColor != mControlCircleColor) {
            mControlCircleColor = controlCircleColor;
            invalidate();
        }
    }


    public void setControlCircleInnerColor(@ColorInt int controlCircleInnerColor) {
        if (controlCircleInnerColor != mControlCircleInnerColor) {
            mControlCircleInnerColor = controlCircleInnerColor;
            invalidate();
        }
    }

    public int getControlCircleInnerColor() {
        return mControlCircleInnerColor;
    }


    public int getControlCircleColor() {
        return mControlCircleColor;
    }

    public int getDotColor() {
        return mDotColor;
    }

    public int getInnerCircleColor() {
        return mInnerCircleColor;
    }


    public void setInnerCircleColor(@ColorInt int innerCircleColor) {
        if (innerCircleColor != mInnerCircleColor) {
            mInnerCircleColor = innerCircleColor;
            invalidate();
        }
    }


    public void setInnerOuterCircleStokeWidth(int innerOuterCircleStokeWidth) {
        if (mInnerOuterCircleStokeWidth != innerOuterCircleStokeWidth) {
            mInnerOuterCircleStokeWidth = innerOuterCircleStokeWidth;
            invalidate();
        }
    }

    public void setControlCircleStokeWidth(int controlCircleStokeWidth) {
        if (mControlCircleStokeWidth != controlCircleStokeWidth && controlCircleStokeWidth >= 0) {
            mControlCircleStokeWidth = controlCircleStokeWidth;
            invalidate();
        }
    }

    public void setConainerPadding(float containerPadding) {
        if (mContainerPadding != containerPadding && containerPadding >= 0) {
            this.mContainerPadding = containerPadding;
            invalidate();
        }
    }

    public float getContainerPadding() {
        return mContainerPadding;
    }

    public void setDotHorizontalGap(float dotHorizontalGap) {
        if (mDotHorizontalGap != dotHorizontalGap && dotHorizontalGap > 0) {
            this.mDotHorizontalGap = dotHorizontalGap;
            invalidate();
        }
    }

    public float getDotHorizontalGap() {
        return mDotHorizontalGap;
    }

    public void setDotWidth(float dotWidth) {
        if (mDotWidth != dotWidth && dotWidth > 0) {
            this.mDotWidth = dotWidth;
            invalidate();
        }
    }

    public float getDotWidth() {
        return mDotWidth;
    }


    public void setDotThickness(float dotThickness) {
        if (mDotThickness != dotThickness && dotThickness > 0) {
            this.mDotThickness = dotThickness;
            invalidate();
        }
    }

    public float getDotThickness() {
        return mDotThickness;
    }

    public void setInnerOuterCircleStokeWidth(float innerOuterCircleStokeWidth) {
        if (mInnerOuterCircleStokeWidth != innerOuterCircleStokeWidth) {
            mInnerOuterCircleStokeWidth = innerOuterCircleStokeWidth;
            invalidate();
        }
    }

    public float getInnerOuterCircleStokeWidth() {
        return mInnerOuterCircleStokeWidth;
    }

    public void setRadiusFraction(float radiusFraction) {
        if (radiusFraction != mRadiusFraction && radiusFraction > 0) {
            mRadiusFraction = radiusFraction;
            invalidate();
        }

    }

    public void setUpDownBitmapOffSet(float upDownBitmapOffSet) {
        if (mUpDownBitmapOffSet != upDownBitmapOffSet) {
            mUpDownBitmapOffSet = upDownBitmapOffSet;
            invalidate();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) dp2px(getResources(), 10000);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            width = (int) dp2px(getResources(), 100);
        } else {
            if (widthMode == MeasureSpec.EXACTLY) {
                width = Math.min(widthSize, width);
            }
            if (heightMode == MeasureSpec.EXACTLY) {
                width = Math.min(heightSize, width);
            }
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = 0.5f * (Math.min(w, h) - getPaddingLeft() - getPaddingRight());
        mRadiusControl = mRadius * 0.15f;
        mRadiusControlInner = mRadius * 0.10f;
        mCircleX = getMeasuredWidth() / 2;
        mCircleY = getMeasuredHeight() / 2;
        mMaxDistance = mRadius - mContainerPadding - mRadiusFraction * mRadiusControl;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnableControl()) {
            return super.onTouchEvent(event);
        }
        if (!isGestureMode()) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMotionDownX = event.getX();
                mMotionDownY = event.getY();
                if (!mMoving) {
                    mOnInnerCircleTouched = onInnerCircleTouched(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMoving) {
                    calibrateMotion(event);
                } else if (mOnInnerCircleTouched && canMove(event)) {
                    mMoving = true;
                    calibrateMotion(event);
                } else {

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mMotionDownX = 0;
                mMotionDownY = 0;
                mOnInnerCircleTouched = false;
                if (mMoving) {
                    reset();
                }
                break;
        }
        return true;
    }

    public boolean isEnableControl() {
        return mEnableControl;
    }

    public void enableControl(boolean enable) {
        mEnableControl = enable;
    }

    public boolean isGestureMode() {
        return mGestureMode;
    }

    public void enableGestureMode(boolean enable) {
        this.mGestureMode = enable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackgroundBitmap(canvas);
        drawHorizontal(canvas, (int) mProgressX);
        drawVertical(canvas, (int) mProgressX);
        drawCircle(canvas);
        drawUpDownBitmap(canvas, mTheta);
        drawLeftRightBitmap(canvas, -mTheta);
        if (mMoving) {
            drawControlCircle(canvas);
        }
    }

    private void drawBackgroundBitmap(Canvas canvas) {
        int width = this.getWidth();
        int height = this.getHeight();
        int left = width / 2 - mBackgroundScaleBmp.getWidth() / 2;
        int top = height / 2 - mBackgroundScaleBmp.getHeight() / 2;
        int right = width / 2 + mBackgroundScaleBmp.getWidth() / 2;
        int bottom = height / 2 + mBackgroundScaleBmp.getHeight() / 2;
        Rect dst = new Rect(left, top, right, bottom);
        canvas.drawBitmap(mBackgroundScaleBmp, null, dst, mBitmapPaint);
    }


    private void drawUpDownBitmap(Canvas canvas, float delta) {
        mMatrix.reset();
        mMatrix.postTranslate(getWidth() / 2 - mUpDownScaleBitmap.getWidth() / 2, getHeight() / 2 - mUpDownScaleBitmap.getHeight() / 2 - mUpDownBitmapOffSet);
        mMatrix.postRotate(delta, getWidth() / 2, getHeight() / 2);
        canvas.drawBitmap(mUpDownScaleBitmap, mMatrix, mBitmapPaint);
    }


    private void drawLeftRightBitmap(Canvas canvas, float delta) {
        mMatrix.reset();
        mMatrix.postTranslate(getWidth() / 2 - mLeftRightScaleBitmap.getWidth() / 2, getHeight() / 2 - mLeftRightScaleBitmap.getHeight() / 2);
        mMatrix.postRotate(delta, getWidth() / 2, getHeight() / 2);
        canvas.drawBitmap(mLeftRightScaleBitmap, mMatrix, mBitmapPaint);
    }


    private void drawCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawCircle(0, 0, mRadius * 0.08f, mPaintInnerCircle);
        canvas.drawCircle(0, 0, mRadius * 0.12f, mPaintInnerOuterCircle);
        canvas.restore();
    }

    public void setOnInnerCircleMoveListener(OnInnerCircleMoveListener listener) {
        this.mOnInnerCircleMoveListener = listener;
    }

    private boolean onInnerCircleTouched(MotionEvent event) {
        float radius = mRadiusControlInner;
        float x = event.getX();
        float y = event.getY();
        return (x - mCircleX) * (x - mCircleX) + (y - mCircleY) * (y - mCircleY) <= radius * radius;
    }

    private boolean canMove(MotionEvent event) {
        return ((event.getX() - mMotionDownX) * (event.getX() - mMotionDownX) + (event.getY() - mMotionDownY) * (event.getY() - mMotionDownY) > mSlop * mSlop);
    }


    private void calibrateMotion(MotionEvent event) {
        calibrateXY(event.getX(), event.getY());
    }

    public void move(float fractionX, float fractionY) {
        if (!mEnableControl) {
            mMoving = false;
            return;
        }
        if (fractionX == 0f && fractionY == 0f) {
            mMoving = false;
        } else {
            mMoving = true;
        }
        if (fractionX > 1.0f) {
            fractionX = 1.0f;
        }
        if (fractionY > 1.0f) {
            fractionY = 1.0f;
        }
        if (fractionX < -1.0f) {
            fractionX = -1.0f;
        }
        if (fractionY < -1.0f) {
            fractionY = -1.0f;
        }
        float x = mCircleX + mMaxDistance * fractionX;
        float y = mCircleY + mMaxDistance * fractionY;
        calibrateXY(x, y);
    }


    private void calibrateXY(float x, float y) {
        float x1, y1;
        float theta;
        if (x > mCircleX && y < mCircleY) {//the first quadrant
            theta = (float) Math.atan((mCircleY - y) / (x - mCircleX));
            x1 = (float) (mCircleX + mMaxDistance * Math.cos(theta));
            y1 = (float) (mCircleY - mMaxDistance * Math.sin(theta));
            if (x > x1) {
                x = x1;
            }
            if (y < y1) {
                y = y1;
            }
            mTheta = (float) (90 - 180 * theta / Math.PI);
        } else if (x > mCircleX && y > mCircleY) { //the second quadrant
            theta = (float) Math.atan((y - mCircleY) / (x - mCircleX));
            x1 = (float) (mCircleX + mMaxDistance * Math.cos(theta));
            y1 = (float) (mCircleY + mMaxDistance * Math.sin(theta));
            if (x > x1) {
                x = x1;
            }
            if (y > y1) {
                y = y1;
            }
            mTheta = (float) (90 + 180 * theta / Math.PI);

        } else if (x < mCircleX && y > mCircleY) {   //the third quadrant
            theta = (float) (Math.atan((y - mCircleY) / (mCircleX - x)));
            x1 = (float) (mCircleX - mMaxDistance * Math.cos(theta));
            y1 = (float) (mCircleY + mMaxDistance * Math.sin(theta));
            if (x < x1) {
                x = x1;
            }
            if (y > y1) {
                y = y1;
            }
            mTheta = (float) (270 - 180 * theta / Math.PI);

        } else if (x < mCircleX && y < mCircleY) {//the fourth quadrant
            theta = (float) (Math.atan((mCircleY - y) / (mCircleX - x)));
            x1 = (float) (mCircleX - mMaxDistance * Math.cos(theta));
            y1 = (float) (mCircleY - mMaxDistance * Math.sin(theta));
            if (x < x1) {
                x = x1;
            }
            if (y < y1) {
                y = y1;
            }
            mTheta = (float) (270 + 180 * theta / Math.PI);

        } else if (x == mCircleX && y != mCircleY) {//Y axis
            if (y > mCircleY + mMaxDistance) {
                y = mCircleY + mMaxDistance;
                mTheta = 0;
            } else if (y < (mCircleY - mMaxDistance)) {
                y = mCircleY - mMaxDistance;
                mTheta = 180f;
            }

        } else if (y == mCircleY && x != mCircleX) {//X axis
            if (x > mCircleX + mMaxDistance) {
                x = mCircleX + mMaxDistance;
                mTheta = 270f;
            } else if (x < (mCircleX - mMaxDistance)) {
                x = mCircleX - mMaxDistance;
                mTheta = 90f;
            }

        } else {
            mTheta = 0;
        }
        mControlCircleX = x;
        mControlCircleY = y;
        float fractionX = (mControlCircleX - mCircleX) / mMaxDistance;
        float fractionY = (mControlCircleY - mCircleY) / mMaxDistance;
        mProgressX = 100f * fractionX;
        mProgressY = 100f * fractionY;
        if (mOnInnerCircleMoveListener != null) {
            mOnInnerCircleMoveListener.onInnerCircleMove(mControlCircleX, mControlCircleY, fractionX, fractionY);
        }
        invalidate();
    }


    private void drawControlCircle(Canvas canvas) {
        canvas.drawCircle(mControlCircleX, mControlCircleY, mRadiusControl, mPaintControlCircle);
        canvas.drawCircle(mControlCircleX, mControlCircleY, mRadiusControlInner, mPaintControlCircleInner);
    }

    private void drawHorizontal(Canvas canvas, int progress) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(90);
        float r = mRadius;
        for (int i = -100; i <= 100; i = i + 2) {
            float r1 = i * r / 100f;
            canvas.drawLine(mDotHorizontalGap, r1, mDotHorizontalGap + mDotWidth, r1, mPaintDot);//the dot in the right
            if ((i + progress) % 5 == 0) {
                canvas.drawLine(-mDotHorizontalGap - mDotWidth, r1, mDotHorizontalGap + mDotWidth, r1, mPaintDot);//the dot in the right
            }
            canvas.drawLine(-mDotHorizontalGap, r1, -mDotHorizontalGap - mDotWidth, r1, mPaintDot);//the dot in the left
        }

        canvas.restore();
    }

    private void drawVertical(Canvas canvas, int progress) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float r = (mRadius - mContainerPadding) * 0.8f;
        for (int i = -60; i <= 60; i = i + 2) {
            float r1 = i * r / 100f;
            canvas.drawLine(mDotHorizontalGap, r1, mDotHorizontalGap + mDotWidth, r1, mPaintDot);//the dot in the right
            if ((progress + i) % 5 == 0) {
                canvas.drawLine(-mDotHorizontalGap - mDotWidth, r1, mDotHorizontalGap + mDotWidth, r1, mPaintDot);//the dot in the right
            }
            canvas.drawLine(-mDotHorizontalGap, r1, -mDotHorizontalGap - mDotWidth, r1, mPaintDot);//the dot in the left

        }
        canvas.restore();
    }

    public void reset() {

        float theta = mTheta;
        if (mTheta > 180) {
            theta = -(360 - mTheta);
        }
        CirclePosition startValue = new CirclePosition(mControlCircleX, mControlCircleY, mProgressX, mProgressY, theta);
        CirclePosition endValue = new CirclePosition(mCircleX, mCircleY, 0, 0, 0);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new PositionEvaluator(), startValue, endValue);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                CirclePosition circlePosition = (CirclePosition) animation.getAnimatedValue();
                mControlCircleX = circlePosition.getX();
                mControlCircleY = circlePosition.getY();
                mProgressX = circlePosition.getProgressX();
                mProgressY = circlePosition.getProgressY();
                mTheta = circlePosition.getTheta();
                if (mOnInnerCircleMoveListener != null) {
                    float fractionX = mProgressX / 100f;
                    float fractionY = mProgressY / 100f;
                    mOnInnerCircleMoveListener.onInnerCircleMove(mControlCircleX, mControlCircleY, fractionX, fractionY);
                }
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMoving = false;
                mTheta = 0;
                mProgressX = 0;
                mProgressY = 0;
                mControlCircleX = mCircleX;
                mControlCircleX = mCircleY;
                if (mOnInnerCircleMoveListener != null) {
                    mOnInnerCircleMoveListener.onInnerCircleMove(mCircleX, mCircleY, 0f, 0f);
                }
                invalidate();
            }
        });
        valueAnimator.start();
    }


    private static class CirclePosition {
        final private float x;
        final private float y;
        final private float progressX;
        final private float progressY;
        final private float theta;

        public CirclePosition(float x, float y, float progressX, float progressY, float theta) {
            this.x = x;
            this.y = y;
            this.progressX = progressX;
            this.progressY = progressY;
            this.theta = theta;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getProgressX() {
            return progressX;
        }

        public float getProgressY() {
            return progressY;
        }

        public float getTheta() {
            return theta;
        }
    }

    private static class PositionEvaluator implements TypeEvaluator<CirclePosition> {

        @Override
        public CirclePosition evaluate(float fraction, CirclePosition startValue, CirclePosition endValue) {
            float x = startValue.getX() + fraction * (endValue.getX() - startValue.getX());
            float y = startValue.getY() + fraction * (endValue.getY() - startValue.getY());
            float progressX = startValue.getProgressX() + fraction * (endValue.getProgressX() - startValue.getProgressX());
            float progressY = startValue.getProgressY() + fraction * (endValue.getProgressY() - startValue.getProgressY());
            float theta = startValue.getTheta() + fraction * (endValue.getTheta() - startValue.getTheta());
            return new CirclePosition(x, y, progressX, progressY, theta);
        }
    }


    public interface OnInnerCircleMoveListener {

        void onInnerCircleMove(float x, float y, float fractionX, float fractionY);
    }

}
