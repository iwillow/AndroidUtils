package com.iwillow.app.android.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.iwillow.app.android.R;

/**
 * Created by https://github.com/iwillow/ on 2016/10/14.
 */

public class RemoteControlBallView extends View {

    public static final String TAG = RemoteControlBallView.class.getSimpleName();

    private static final int DEFAULT_COLOR = Color.GRAY;
    private static final int DEFAULT_COLOR_MOVING = Color.WHITE;
    private static final float DEFAULT_STROKE_WIDTH_INNER_CIRCLE = 1;
    private static final float DEFAULT_STROKE_WIDTH_MIDDLE_CIRCLE = 1;
    private static final float DEFAULT_STROKE_WIDTH_OUTER_CIRCLE = 1;


    private static final float DEFAULT_RADIUS_FRACTION_INNER_CIRCLE = 30;
    private static final float DEFAULT_RADIUS_FRACTION_MIDDLE_CIRCLE = 70;
    private static final float DEFAULT_RADIUS_FRACTION_OUTER_CIRCLE = 90;
    private static final float DEFAULT_DIRECTION_TEXT_SIZE = 30;
    private static final float DEFAULT_SINGLE_EDGE_LENGTH = 30;


    private static final String INSTANCE_STATE = "saved_instance";
    private static final String KEY_RADIUS_INNER_CIRCLE = "RadiusInnerCircle";
    private static final String KEY_RADIUS_INNER_CIRCLE_MOVING = "RadiusInnerCircleMoving";
    private static final String KEY_RADIUS_MIDDLE_CIRCLE = "RadiusMiddleCircle";
    private static final String KEY_RADIUS_MIDDLE_CIRCLE_MOVING = "RadiusMiddleCircleMoving";
    private static final String KEY_RADIUS_OUTER_CIRCLE = "RadiusOuterCircle";
    private static final String KEY_RADIUS_OUTER_CIRCLE_MOVING = "RadiusOuterCircleMoving";

    private static final String KEY_COLOR_INNER_CIRCLE = "ColorInnerCircle";
    private static final String KEY_COLOR_INNER_CIRCLE_MOVING = "ColorInnerCircleMoving";
    private static final String KEY_COLOR_MIDDLE_CIRCLE = "ColorMiddleCircle";
    private static final String KEY_COLOR_MIDDLE_CIRCLE_MOVING = "ColorMiddleCircleMoving";
    private static final String KEY_COLOR_OUTER_CIRCLE = "ColorOuterCircle";
    private static final String KEY_COLOR_OUTER_CIRCLE_MOVING = "ColorOuterCircleMoving";


    private static final String KEY_STROKE_WIDTH_INNER_CIRCLE = "StrokeWidthInnerCircle";
    private static final String KEY_STROKE_WIDTH_INNER_CIRCLE_MOVING = "StrokeWidthInnerCircleMoving";
    private static final String KEY_STROKE_WIDTH_MIDDLE_CIRCLE = "StrokeWidthMiddleCircle";
    private static final String KEY_STROKE_WIDTH_MIDDLE_CIRCLE_MOVING = "StrokeWidthMiddleCircleMoving";
    private static final String KEY_STROKE_WIDTH_OUTER_CIRCLE = "StrokeWidthOuterCircle";
    private static final String KEY_STROKE_WIDTH_OUTER_CIRCLE_MOVING = "StrokeWidthOuterCircleMoving";


    private float mRadiusInnerCircle;
    private float mRadiusInnerCircleMoving;
    private float mRadiusMiddleCircle;
    private float mRadiusMiddleCircleMoving;
    private float mRadiusOuterCircle;
    private float mRadiusOuterCircleMoving;

    private float mRadiusFractionInnerCircle;
    private float mRadiusFractionInnerCircleMoving;
    private float mRadiusFractionMiddleCircle;
    private float mRadiusFractionMiddleCircleMoving;
    private float mRadiusFractionOuterCircle;
    private float mRadiusFractionOuterCircleMoving;


    private int mColorInnerCircle;
    private int mColorInnerCircleMoving;
    private int mColorMiddleCircle;
    private int mColorMiddleCircleMoving;
    private int mColorOuterCircle;
    private int mColorOuterCircleMoving;

    private float mStrokeWidthInnerCircle;
    private float mStrokeWidthInnerCircleMoving;
    private float mStrokeWidthMiddleCircle;
    private float mStrokeWidthMiddleCircleMoving;
    private float mStrokeWidthOuterCircle;
    private float mStrokeWidthOuterCircleMoving;

    private Paint mPaintInnerCircle;
    private Paint mPaintInnerCircleMoving;
    private Paint mPaintMiddleCircle;
    private Paint mPaintMiddleCircleMoving;
    private Paint mPaintOuterCircle;
    private Paint mPaintTriangle;

    private Paint mPaintOuterCircleMoving;

    private TextPaint mTextPaint;

    private float mInnerCircleX;
    private float mInnerCircleY;

    private float mCircleX;
    private float mCircleY;
    private boolean mMoving;
    private float mMotionDownX;
    private float mMotionDownY;
    private boolean mOnInnerCircleTouched;
    private int mSlop;
    private OnInnerCircleMoveListener mOnInnerCircleMoveListener;

    private Path mPathTriangle;
    private float mTriangleLeftX, mTriangleLeftY;
    private float mTriangleRightX, mTriangleRightY;
    private float mTriangleTopX, mTriangleTopY;
    private float mTriangleSide;
    public int mTriangleColor;

    private String mLeft;
    private String mRight;
    private String mForward;
    private String mBackward;

    private float mDirectionTextSize;
    private int mDirectionTextColor;


    public RemoteControlBallView(Context context) {
        super(context);
        init(null, 0);
    }

    public RemoteControlBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }


    public RemoteControlBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RemoteControlBallView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }


    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RemoteControlBallView, defStyle, 0);
        initInnerCircle(a);
        initMiddleCircle(a);
        initOuterCircle(a);
        initTrianglePath(a);
        initTextPaint(a);
        a.recycle();
        mLeft = getContext().getString(R.string.remote_control_direction_left);
        mRight = getContext().getString(R.string.remote_control_direction_right);
        mForward = getContext().getString(R.string.remote_control_direction_forward);
        mBackward = getContext().getString(R.string.remote_control_direction_backward);
        mSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mMoving = false;
    }


    private void initInnerCircle(TypedArray a) {
        mStrokeWidthInnerCircle = a.getDimension(R.styleable.RemoteControlBallView_strokeWidthInnerCircle, DEFAULT_STROKE_WIDTH_INNER_CIRCLE);
        mColorInnerCircle = a.getColor(R.styleable.RemoteControlBallView_colorInnerCircle, DEFAULT_COLOR);
        mPaintInnerCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintInnerCircle.setStrokeWidth(mStrokeWidthInnerCircle);
        mPaintInnerCircle.setStyle(Paint.Style.FILL);
        mPaintInnerCircle.setColor(mColorInnerCircle);
        mRadiusFractionInnerCircle = a.getFloat(R.styleable.RemoteControlBallView_radiusInnerCircle, DEFAULT_RADIUS_FRACTION_INNER_CIRCLE);
        if (mRadiusFractionInnerCircle < 0) {
            mRadiusFractionInnerCircle = 0;
        }
        if (mRadiusFractionInnerCircle > 100) {
            mRadiusFractionInnerCircle = 100;
        }


        mStrokeWidthInnerCircleMoving = a.getDimension(R.styleable.RemoteControlBallView_strokeWidthInnerCircleMoving, DEFAULT_STROKE_WIDTH_INNER_CIRCLE);
        mColorInnerCircleMoving = a.getColor(R.styleable.RemoteControlBallView_colorInnerCircleMoving, DEFAULT_COLOR_MOVING);
        mPaintInnerCircleMoving = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintInnerCircleMoving.setStrokeWidth(mStrokeWidthInnerCircleMoving);
        mPaintInnerCircleMoving.setStyle(Paint.Style.STROKE);

        mPaintInnerCircleMoving.setColor(mColorInnerCircleMoving);
        mRadiusFractionInnerCircleMoving = a.getFloat(R.styleable.RemoteControlBallView_radiusInnerCircleMoving, DEFAULT_RADIUS_FRACTION_INNER_CIRCLE);
        if (mRadiusFractionInnerCircleMoving < 0) {
            mRadiusFractionInnerCircleMoving = 0;
        }
        if (mRadiusFractionInnerCircleMoving > 100) {
            mRadiusFractionInnerCircleMoving = 100;
        }
    }

    private void initMiddleCircle(TypedArray a) {
        mStrokeWidthMiddleCircle = a.getDimension(R.styleable.RemoteControlBallView_strokeWidthMiddleCircle, DEFAULT_STROKE_WIDTH_MIDDLE_CIRCLE);
        mColorMiddleCircle = a.getColor(R.styleable.RemoteControlBallView_colorMiddleCircle, DEFAULT_COLOR);
        mPaintMiddleCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintMiddleCircle.setStyle(Paint.Style.STROKE);
        mPaintMiddleCircle.setStrokeWidth(mStrokeWidthMiddleCircle);
        mPaintMiddleCircle.setColor(mColorMiddleCircle);
        mRadiusFractionMiddleCircle = a.getFloat(R.styleable.RemoteControlBallView_radiusMiddleCircle, DEFAULT_RADIUS_FRACTION_MIDDLE_CIRCLE);
        if (mRadiusFractionMiddleCircle < 0) {
            mRadiusFractionMiddleCircle = 0;
        }
        if (mRadiusFractionMiddleCircle > 100) {
            mRadiusFractionMiddleCircle = 100;
        }


        mStrokeWidthMiddleCircleMoving = a.getDimension(R.styleable.RemoteControlBallView_strokeWidthMiddleCircleMoving, DEFAULT_STROKE_WIDTH_MIDDLE_CIRCLE);
        mColorMiddleCircleMoving = a.getColor(R.styleable.RemoteControlBallView_colorMiddleCircleMoving, DEFAULT_COLOR_MOVING);
        mPaintMiddleCircleMoving = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintMiddleCircleMoving.setStrokeWidth(mStrokeWidthMiddleCircleMoving);
        mPaintMiddleCircleMoving.setColor(mColorMiddleCircleMoving);
        mPaintMiddleCircleMoving.setStyle(Paint.Style.STROKE);
        mRadiusFractionMiddleCircleMoving = a.getFloat(R.styleable.RemoteControlBallView_radiusMiddleCircleMoving, DEFAULT_RADIUS_FRACTION_MIDDLE_CIRCLE);
        if (mRadiusFractionMiddleCircleMoving < 0) {
            mRadiusFractionMiddleCircleMoving = 0;
        }
        if (mRadiusFractionMiddleCircleMoving > 100) {
            mRadiusFractionMiddleCircleMoving = 100;
        }
    }

    private void initOuterCircle(TypedArray a) {
        mStrokeWidthOuterCircle = a.getDimension(R.styleable.RemoteControlBallView_strokeWidthOuterCircle, DEFAULT_STROKE_WIDTH_OUTER_CIRCLE);
        mColorOuterCircle = a.getColor(R.styleable.RemoteControlBallView_colorOuterCircle, DEFAULT_COLOR);
        mPaintOuterCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOuterCircle.setStyle(Paint.Style.STROKE);
        mPaintOuterCircle.setColor(mColorOuterCircle);
        mPaintOuterCircle.setStrokeWidth(mStrokeWidthOuterCircle);
        mRadiusFractionOuterCircle = a.getFloat(R.styleable.RemoteControlBallView_radiusOuterCircle, DEFAULT_RADIUS_FRACTION_OUTER_CIRCLE);
        if (mRadiusFractionOuterCircle < 0) {
            mRadiusFractionOuterCircle = 0;
        }
        if (mRadiusFractionOuterCircle > 100) {
            mRadiusFractionOuterCircle = 100;
        }


        mStrokeWidthOuterCircleMoving = a.getDimension(R.styleable.RemoteControlBallView_strokeWidthOuterCircleMoving, DEFAULT_STROKE_WIDTH_OUTER_CIRCLE);
        mColorOuterCircleMoving = a.getColor(R.styleable.RemoteControlBallView_colorOuterCircle, DEFAULT_COLOR_MOVING);
        mPaintOuterCircleMoving = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOuterCircleMoving.setStyle(Paint.Style.STROKE);
        mPaintOuterCircleMoving.setColor(mColorOuterCircleMoving);
        mPaintOuterCircleMoving.setStrokeWidth(mStrokeWidthOuterCircleMoving);
        mRadiusFractionOuterCircleMoving = a.getFloat(R.styleable.RemoteControlBallView_radiusOuterCircleMoving, DEFAULT_RADIUS_FRACTION_OUTER_CIRCLE);
        if (mRadiusFractionOuterCircleMoving < 0) {
            mRadiusFractionOuterCircleMoving = 0;
        }
        if (mRadiusFractionOuterCircleMoving > 100) {
            mRadiusFractionOuterCircleMoving = 100;
        }
    }

    private void initTrianglePath(TypedArray a) {
        mTriangleColor = a.getColor(R.styleable.RemoteControlBallView_triangleColor, DEFAULT_COLOR);
        mTriangleSide = a.getDimension(R.styleable.RemoteControlBallView_triangleSingleEdgeLength, DEFAULT_SINGLE_EDGE_LENGTH);
        mPaintTriangle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTriangle.setStyle(Paint.Style.FILL);
        // mPaintTriangle.setStrokeWidth(20);
        mPaintTriangle.setColor(mTriangleColor);
        mPathTriangle = new Path();


    }

    private void initTextPaint(TypedArray a) {

        mDirectionTextColor = a.getColor(R.styleable.RemoteControlBallView_textColorDirection, DEFAULT_COLOR);
        mDirectionTextSize = a.getDimension(R.styleable.RemoteControlBallView_textColorDirection, DEFAULT_DIRECTION_TEXT_SIZE);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mDirectionTextSize);
        mTextPaint.setColor(mDirectionTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);

    }


    @Override
    protected int getSuggestedMinimumHeight() {
        return 100;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return 100;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCircleX = getMeasuredWidth() / 2;
        mCircleY = getMeasuredHeight() / 2;
        mInnerCircleX = mCircleX;
        mInnerCircleY = mCircleY;
        mRadiusInnerCircle = 0.01f * mRadiusFractionInnerCircle * getWidth() / 2;
        mRadiusInnerCircleMoving = 0.01f * mRadiusFractionInnerCircleMoving * getWidth() / 2;
        mRadiusMiddleCircle = 0.01f * mRadiusFractionMiddleCircle * getWidth() / 2;
        mRadiusMiddleCircleMoving = 0.01f * mRadiusFractionMiddleCircleMoving * getWidth() / 2;
        mRadiusOuterCircle = 0.01f * mRadiusFractionOuterCircle * getWidth() / 2;
        mRadiusOuterCircleMoving = 0.01f * mRadiusFractionOuterCircleMoving * getWidth() / 2;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMotionDownX = event.getX();
                mMotionDownY = event.getY();
                if (!mMoving) {
                    mOnInnerCircleTouched = onInnerCircleTouched(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mOnInnerCircleTouched && canMove(event)) {
                    calibrateXY(event);
                    mMoving = true;
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

    private void calibrateXY(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float gravityX, gravityY;
        double theta;
        float x1, y1;
        if (x > mCircleX && y < mCircleY) {//the first quadrant
            theta = Math.atan((mCircleY - y) / (x - mCircleX));
            x1 = (float) (mCircleX + (mRadiusMiddleCircle - mRadiusInnerCircle) * Math.cos(theta));
            y1 = (float) (mCircleY - (mRadiusMiddleCircle - mRadiusInnerCircle) * Math.sin(theta));
            if (x > x1) {
                x = x1;
            }
            if (y < y1) {
                y = y1;
            }
            gravityX = (float) (mCircleX + mRadiusOuterCircle * Math.cos(theta));
            gravityY = (float) (mCircleY - mRadiusOuterCircle * Math.sin(theta));

            mTriangleLeftX = (float) (gravityX - 0.5f * mTriangleSide * Math.sin(theta));
            mTriangleLeftY = (float) (gravityY - 0.5f * mTriangleSide * Math.cos(theta));

            mTriangleRightX = (float) (gravityX + 0.5f * mTriangleSide * Math.sin(theta));
            mTriangleRightY = (float) (gravityY + 0.5f * mTriangleSide * Math.cos(theta));


            mTriangleTopX = (float) (gravityX + mTriangleSide * Math.cos(Math.PI / 6) * Math.cos(theta));
            mTriangleTopY = (float) (gravityY - mTriangleSide * Math.cos(Math.PI / 6) * Math.sin(theta));


        } else if (x > mCircleX && y > mCircleY) { //the second quadrant
            theta = Math.atan((y - mCircleY) / (x - mCircleX));
            x1 = (float) (mCircleX + (mRadiusMiddleCircle - mRadiusInnerCircle) * Math.cos(theta));
            y1 = (float) (mCircleY + (mRadiusMiddleCircle - mRadiusInnerCircle) * Math.sin(theta));
            if (x > x1) {
                x = x1;
            }
            if (y > y1) {
                y = y1;
            }

            gravityX = (float) (mCircleX + mRadiusOuterCircle * Math.cos(theta));
            gravityY = (float) (mCircleY + mRadiusOuterCircle * Math.sin(theta));


            mTriangleLeftX = (float) (gravityX + 0.5f * mTriangleSide * Math.sin(theta));
            mTriangleLeftY = (float) (gravityY - 0.5f * mTriangleSide * Math.cos(theta));


            mTriangleRightX = (float) (gravityX - 0.5f * mTriangleSide * Math.sin(theta));
            mTriangleRightY = (float) (gravityY + 0.5f * mTriangleSide * Math.cos(theta));


            mTriangleTopX = (float) (gravityX + mTriangleSide * Math.cos(Math.PI / 6) * Math.cos(theta));
            mTriangleTopY = (float) (gravityY + mTriangleSide * Math.cos(Math.PI / 6) * Math.sin(theta));

        } else if (x < mCircleX && y > mCircleY) {  //the third quadrant
            theta = Math.atan((y - mCircleY) / (mCircleX - x));
            x1 = (float) (mCircleX - (mRadiusMiddleCircle - mRadiusInnerCircle) * Math.cos(theta));
            y1 = (float) (mCircleY + (mRadiusMiddleCircle - mRadiusInnerCircle) * Math.sin(theta));
            if (x < x1) {
                x = x1;
            }
            if (y > y1) {
                y = y1;
            }

            gravityX = (float) (mCircleX - mRadiusOuterCircle * Math.cos(theta));
            gravityY = (float) (mCircleY + mRadiusOuterCircle * Math.sin(theta));


            mTriangleLeftX = (float) (gravityX + 0.5f * mTriangleSide * Math.sin(theta));
            mTriangleLeftY = (float) (gravityY + 0.5f * mTriangleSide * Math.cos(theta));

            mTriangleRightX = (float) (gravityX - 0.5f * mTriangleSide * Math.sin(theta));
            mTriangleRightY = (float) (gravityY - 0.5f * mTriangleSide * Math.cos(theta));

            mTriangleTopX = (float) (gravityX - mTriangleSide * Math.cos(Math.PI / 6) * Math.cos(theta));
            mTriangleTopY = (float) (gravityY + mTriangleSide * Math.cos(Math.PI / 6) * Math.sin(theta));


        } else if (x < mCircleX && y < mCircleY) {//the fourth quadrant
            theta = Math.atan((mCircleY - y) / (mCircleX - x));
            x1 = (float) (mCircleX - (mRadiusMiddleCircle - mRadiusInnerCircle) * Math.cos(theta));
            y1 = (float) (mCircleY - (mRadiusMiddleCircle - mRadiusInnerCircle) * Math.sin(theta));
            if (x < x1) {
                x = x1;
            }
            if (y < y1) {
                y = y1;
            }

            gravityX = (float) (mCircleX - mRadiusOuterCircle * Math.cos(theta));
            gravityY = (float) (mCircleY - mRadiusOuterCircle * Math.sin(theta));

            mTriangleLeftX = (float) (gravityX - 0.5f * mTriangleSide * Math.sin(theta));
            mTriangleLeftY = (float) (gravityY + 0.5f * mTriangleSide * Math.cos(theta));


            mTriangleRightX = (float) (gravityX + 0.5f * mTriangleSide * Math.sin(theta));
            mTriangleRightY = (float) (gravityY - 0.5f * mTriangleSide * Math.cos(theta));


            mTriangleTopX = (float) (gravityX - mTriangleSide * Math.cos(Math.PI / 6) * Math.cos(theta));
            mTriangleTopY = (float) (gravityY - mTriangleSide * Math.cos(Math.PI / 6) * Math.sin(theta));


        } else if (x == mCircleX) {

            if (y > mCircleY + (mRadiusMiddleCircle - mRadiusInnerCircle)) {
                y = mCircleY + (mRadiusMiddleCircle - mRadiusInnerCircle);
            } else if (y < (mCircleY - (mRadiusMiddleCircle - mRadiusInnerCircle))) {
                y = mCircleY - (mRadiusMiddleCircle - mRadiusInnerCircle);
            }

            if (y > mCircleY) {
                mTriangleLeftX = mCircleX + 0.5f * mTriangleSide;
                mTriangleLeftY = mCircleY + mRadiusOuterCircle;

                mTriangleRightX = mCircleX - 0.5f * mTriangleSide;
                mTriangleRightY = mCircleY + mRadiusOuterCircle;

                mTriangleTopX = mCircleX;
                mTriangleTopY = (float) (mCircleY + mRadiusOuterCircle + mTriangleSide * Math.cos(Math.PI / 6));

            } else if (y < mCircleY) {


                mTriangleLeftX = mCircleX - 0.5f * mTriangleSide;
                mTriangleLeftY = mCircleY - mRadiusOuterCircle;

                mTriangleRightX = mCircleX + 0.5f * mTriangleSide;
                mTriangleRightY = mCircleY - mRadiusOuterCircle;

                mTriangleTopX = mCircleX;
                mTriangleTopY = (float) (mCircleY - mRadiusOuterCircle - mTriangleSide * Math.cos(Math.PI / 6));

            }


        } else if (y == mCircleY) {
            if (x > mCircleX + (mRadiusMiddleCircle - mRadiusInnerCircle)) {
                x = mCircleX + (mRadiusMiddleCircle - mRadiusInnerCircle);
            } else if (x < (mCircleX - (mRadiusMiddleCircle - mRadiusInnerCircle))) {
                x = mCircleX - (mRadiusMiddleCircle - mRadiusInnerCircle);
            }


            if (x > mCircleX) {
                mTriangleLeftX = mCircleX + mRadiusOuterCircle;
                mTriangleLeftY = mCircleY - 0.5f * mTriangleSide;

                mTriangleRightX = mCircleX + mRadiusOuterCircle;
                mTriangleRightY = mCircleY + 0.5f * mTriangleSide;

                mTriangleTopX = (float) (mCircleX + mRadiusOuterCircle + mTriangleSide * Math.cos(Math.PI / 6));
                mTriangleTopY = mCircleY;

            } else if (x < mCircleX) {


                mTriangleLeftX = mCircleX - mRadiusOuterCircle;
                mTriangleLeftY = mCircleY + 0.5f * mTriangleSide;

                mTriangleRightX = mCircleX - mRadiusOuterCircle;
                mTriangleRightY = mCircleY - 0.5f * mTriangleSide;

                mTriangleTopX = (float) (mCircleX - mRadiusOuterCircle - mTriangleSide * Math.cos(Math.PI / 6));
                mTriangleTopY = mCircleY;

            }


        }
        mInnerCircleX = x;
        mInnerCircleY = y;
        if (mOnInnerCircleMoveListener != null) {
            float fractionX = (mCircleX - mInnerCircleX) / (mRadiusMiddleCircle - mRadiusInnerCircle);
            float fractionY = (mCircleY - mInnerCircleY) / (mRadiusMiddleCircle - mRadiusInnerCircle);
            mOnInnerCircleMoveListener.onInnerCircleMove(mInnerCircleX, mInnerCircleY, fractionX, fractionY);
        }
        invalidate();
    }


    private boolean onInnerCircleTouched(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return (x - mCircleX) * (x - mCircleX) + (y - mCircleY) * (y - mCircleY) <= mRadiusInnerCircle * mRadiusInnerCircle;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawInnerCircle(canvas);
        drawMiddleCircle(canvas);
        drawOuterCircle(canvas);
        drawDecoration(canvas);
        drawTriangle(canvas);
        drawForwardText(canvas);
        drawBackwardText(canvas);
        drawLeftText(canvas);
        drawRightText(canvas);
    }

    private void drawTriangle(Canvas canvas) {
        if (mMoving) {
            mPathTriangle.reset();
            mPathTriangle.moveTo(mTriangleLeftX, mTriangleLeftY);
            mPathTriangle.lineTo(mTriangleRightX, mTriangleRightY);
            mPathTriangle.lineTo(mTriangleTopX, mTriangleTopY);
            mPathTriangle.close();
            canvas.drawPath(mPathTriangle, mPaintTriangle);
        }

    }

    private void drawForwardText(Canvas canvas) {
        float baseX = mCircleX - 0.5f * mTextPaint.measureText(mForward);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float baseY = mCircleY - mRadiusOuterCircle - mTriangleSide * ((float) Math.cos(Math.PI / 6)) - fontMetrics.bottom;
        canvas.drawText(mForward, baseX, baseY, mTextPaint);
    }

    private void drawBackwardText(Canvas canvas) {
        float baseX = mCircleX - 0.5f * mTextPaint.measureText(mBackward);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float baseY = (float) (mCircleY + mRadiusOuterCircle + mTriangleSide * Math.cos(Math.PI / 6) - fontMetrics.top);
        canvas.drawText(mBackward, baseX, baseY, mTextPaint);
    }

    private void drawLeftText(Canvas canvas) {
        float baseX = mCircleX - mTextPaint.measureText(mLeft) - mTriangleSide * ((float) Math.cos(Math.PI / 6)) - mRadiusOuterCircle;
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float baseY = mCircleY + 0.5f * (fontMetrics.descent - fontMetrics.ascent) - fontMetrics.bottom;
        canvas.drawText(mLeft, baseX, baseY, mTextPaint);
    }


    private void drawRightText(Canvas canvas) {
        float baseX = mCircleX + mRadiusOuterCircle + mTriangleSide * ((float) Math.cos(Math.PI / 6));
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float baseY = mCircleY + 0.5f * (fontMetrics.descent - fontMetrics.ascent) - fontMetrics.bottom;
        canvas.drawText(mRight, baseX, baseY, mTextPaint);
    }

    private void reset() {
        CirclePosition startValue = new CirclePosition(mInnerCircleX, mInnerCircleY);
        CirclePosition endValue = new CirclePosition(mCircleX, mCircleY);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new PositionEvaluator(), startValue, endValue);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                CirclePosition circlePosition = (CirclePosition) animation.getAnimatedValue();
                mInnerCircleX = circlePosition.getX();
                mInnerCircleY = circlePosition.getY();
                if (mOnInnerCircleMoveListener != null) {
                    float fractionX = (mCircleX - mInnerCircleX) / (mRadiusMiddleCircle - mRadiusInnerCircle);
                    float fractionY = (mCircleY - mInnerCircleY) / (mRadiusMiddleCircle - mRadiusInnerCircle);
                    mOnInnerCircleMoveListener.onInnerCircleMove(mInnerCircleX, mInnerCircleY, fractionX, fractionY);
                }
                invalidate();


            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMoving = false;
                if (mOnInnerCircleMoveListener != null) {
                    mOnInnerCircleMoveListener.onInnerCircleMove(mCircleX, mCircleY, 0f, 0f);
                }
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }


    private void drawInnerCircle(Canvas canvas) {
        if (mMoving) {
            canvas.drawCircle(mInnerCircleX, mInnerCircleY, mRadiusInnerCircleMoving, mPaintInnerCircleMoving);
            canvas.drawCircle(mInnerCircleX, mInnerCircleY, 0.9f * mRadiusInnerCircleMoving, mPaintInnerCircle);
        } else {
            canvas.drawCircle(mInnerCircleX, mInnerCircleY, mRadiusInnerCircle, mPaintInnerCircle);
        }

    }

    private void drawDecoration(Canvas canvas) {
        canvas.drawCircle(mCircleX, mCircleY, mRadiusInnerCircle, mPaintMiddleCircle);
        canvas.drawCircle(mCircleX, mCircleY, 0.5f * mRadiusInnerCircle, mPaintInnerCircle);
    }

    private void drawMiddleCircle(Canvas canvas) {
        if (mMoving) {
            canvas.drawCircle(mCircleX, mCircleY, mRadiusMiddleCircleMoving, mPaintMiddleCircleMoving);
        } else {
            canvas.drawCircle(mCircleX, mCircleY, mRadiusMiddleCircle, mPaintMiddleCircle);
        }
    }

    private void drawOuterCircle(Canvas canvas) {
        if (mMoving) {
            canvas.drawCircle(mCircleX, mCircleY, mRadiusOuterCircleMoving, mPaintOuterCircleMoving);
        } else {
            canvas.drawCircle(mCircleX, mCircleY, mRadiusOuterCircle, mPaintOuterCircle);
        }

    }


    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(KEY_RADIUS_INNER_CIRCLE, mRadiusInnerCircle);
        bundle.putFloat(KEY_RADIUS_INNER_CIRCLE_MOVING, mRadiusInnerCircleMoving);
        bundle.putFloat(KEY_RADIUS_MIDDLE_CIRCLE, mRadiusMiddleCircle);
        bundle.putFloat(KEY_RADIUS_MIDDLE_CIRCLE_MOVING, mRadiusMiddleCircleMoving);
        bundle.putFloat(KEY_RADIUS_OUTER_CIRCLE, mRadiusOuterCircle);
        bundle.putFloat(KEY_RADIUS_OUTER_CIRCLE_MOVING, mRadiusOuterCircleMoving);

        bundle.putFloat(KEY_STROKE_WIDTH_INNER_CIRCLE, mStrokeWidthInnerCircle);
        bundle.putFloat(KEY_STROKE_WIDTH_INNER_CIRCLE_MOVING, mStrokeWidthInnerCircleMoving);
        bundle.putFloat(KEY_STROKE_WIDTH_MIDDLE_CIRCLE, mStrokeWidthMiddleCircle);
        bundle.putFloat(KEY_STROKE_WIDTH_MIDDLE_CIRCLE_MOVING, mStrokeWidthMiddleCircleMoving);
        bundle.putFloat(KEY_STROKE_WIDTH_OUTER_CIRCLE, mStrokeWidthOuterCircle);
        bundle.putFloat(KEY_STROKE_WIDTH_OUTER_CIRCLE_MOVING, mStrokeWidthOuterCircleMoving);

        bundle.putInt(KEY_COLOR_INNER_CIRCLE, mColorInnerCircle);
        bundle.putInt(KEY_COLOR_INNER_CIRCLE_MOVING, mColorInnerCircleMoving);
        bundle.putInt(KEY_COLOR_MIDDLE_CIRCLE, mColorMiddleCircle);
        bundle.putInt(KEY_COLOR_MIDDLE_CIRCLE_MOVING, mColorMiddleCircleMoving);
        bundle.putInt(KEY_COLOR_OUTER_CIRCLE, mColorOuterCircle);
        bundle.putInt(KEY_COLOR_OUTER_CIRCLE_MOVING, mColorOuterCircleMoving);

        return bundle;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            mRadiusFractionInnerCircle = bundle.getFloat(KEY_RADIUS_INNER_CIRCLE, DEFAULT_RADIUS_FRACTION_INNER_CIRCLE);
            mRadiusFractionInnerCircleMoving = bundle.getFloat(KEY_RADIUS_INNER_CIRCLE_MOVING, DEFAULT_RADIUS_FRACTION_INNER_CIRCLE);
            mRadiusFractionMiddleCircle = bundle.getFloat(KEY_RADIUS_MIDDLE_CIRCLE, DEFAULT_RADIUS_FRACTION_MIDDLE_CIRCLE);
            mRadiusFractionMiddleCircleMoving = bundle.getFloat(KEY_RADIUS_MIDDLE_CIRCLE_MOVING, DEFAULT_RADIUS_FRACTION_MIDDLE_CIRCLE);
            mRadiusFractionOuterCircle = bundle.getFloat(KEY_RADIUS_OUTER_CIRCLE, DEFAULT_RADIUS_FRACTION_OUTER_CIRCLE);
            mRadiusFractionOuterCircleMoving = bundle.getFloat(KEY_RADIUS_OUTER_CIRCLE_MOVING, DEFAULT_RADIUS_FRACTION_OUTER_CIRCLE);

            mStrokeWidthInnerCircle = bundle.getFloat(KEY_STROKE_WIDTH_INNER_CIRCLE, DEFAULT_STROKE_WIDTH_INNER_CIRCLE);
            mStrokeWidthInnerCircleMoving = bundle.getFloat(KEY_STROKE_WIDTH_INNER_CIRCLE_MOVING, DEFAULT_STROKE_WIDTH_INNER_CIRCLE);
            mStrokeWidthMiddleCircle = bundle.getFloat(KEY_STROKE_WIDTH_MIDDLE_CIRCLE, DEFAULT_STROKE_WIDTH_MIDDLE_CIRCLE);
            mStrokeWidthMiddleCircleMoving = bundle.getFloat(KEY_STROKE_WIDTH_MIDDLE_CIRCLE_MOVING, DEFAULT_STROKE_WIDTH_MIDDLE_CIRCLE);
            mStrokeWidthOuterCircle = bundle.getFloat(KEY_STROKE_WIDTH_OUTER_CIRCLE, DEFAULT_STROKE_WIDTH_OUTER_CIRCLE);
            mStrokeWidthOuterCircleMoving = bundle.getFloat(KEY_STROKE_WIDTH_OUTER_CIRCLE_MOVING, DEFAULT_STROKE_WIDTH_OUTER_CIRCLE);

            mColorInnerCircle = bundle.getInt(KEY_COLOR_INNER_CIRCLE, DEFAULT_COLOR);
            mColorInnerCircleMoving = bundle.getInt(KEY_COLOR_INNER_CIRCLE_MOVING, DEFAULT_COLOR_MOVING);
            mColorMiddleCircle = bundle.getInt(KEY_COLOR_MIDDLE_CIRCLE, DEFAULT_COLOR);
            mColorMiddleCircleMoving = bundle.getInt(KEY_COLOR_MIDDLE_CIRCLE_MOVING, DEFAULT_COLOR_MOVING);
            mColorOuterCircle = bundle.getInt(KEY_COLOR_OUTER_CIRCLE, DEFAULT_COLOR);
            mColorOuterCircleMoving = bundle.getInt(KEY_COLOR_OUTER_CIRCLE_MOVING, DEFAULT_COLOR_MOVING);

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private boolean canMove(MotionEvent event) {
        return (Math.abs(event.getX() - mMotionDownX) > mSlop || Math.abs(event.getX() - mMotionDownY) > mSlop);
    }

    static class CirclePosition {
        final private float x;
        final private float Y;

        public CirclePosition(float x, float y) {
            this.x = x;
            Y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return Y;
        }
    }

    public static class PositionEvaluator implements TypeEvaluator<CirclePosition> {

        @Override
        public CirclePosition evaluate(float fraction, CirclePosition startValue, CirclePosition endValue) {
            float x = startValue.getX() + fraction * (endValue.getX() - startValue.getX());
            float y = startValue.getY() + fraction * (endValue.getY() - startValue.getY());
            return new CirclePosition(x, y);
        }
    }

    public interface OnInnerCircleMoveListener {

        void onInnerCircleMove(float x, float y, float fractionX, float fractionY);
    }

    public void setOnInnerCircleMoveListener(OnInnerCircleMoveListener onInnerCircleMoveListener) {
        mOnInnerCircleMoveListener = onInnerCircleMoveListener;
    }
}
