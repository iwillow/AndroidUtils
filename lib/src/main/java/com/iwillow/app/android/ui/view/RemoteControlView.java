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
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.iwillow.app.android.R;

/**
 * Created by https://github.com/iwillow/ on 2016/10/22.
 */

public class RemoteControlView extends View {

    private static final int DEFAULT_COLOR = Color.GRAY;
    private static final int DEFAULT_COLOR_MOVING = Color.WHITE;
    private static final float DEFAULT_TEXT_SIZE = 20f;
    private static final float DEFAULT_TEXT_SIZE_MOVING = 20f;
    private static final float DEFAULT_TRIANGLE_EDGE_LENGTH = 30;
    private static final float DEFAULT_TRIANGLE_EDGE_LENGTH_MOVING = 30;
    private static final float DEFAULT_DIRECTION_TRIANGLE_STROKE_WIDTH = 0;
    private static final float DEFAULT_DIRECTION_TRIANGLE_STROKE_WIDTH_MOVING = 0;

    private static final float DEFAULT_CIRCLE_OUTER_STROKE_WIDTH = 1;
    private static final float DEFAULT_CIRCLE_OUTER_STROKE_WIDTH_MOVING = 1;
    private static final float DEFAULT_CIRCLE_OUTER_RADIUS_FRACTION = 0.7f;
    private static final float DEFAULT_CIRCLE_OUTER_RADIUS_FRACTION_MOVING = 0.7f;


    private static final float DEFAULT_CIRCLE_OUTER_INNER_STROKE_WIDTH = 1;
    private static final float DEFAULT_CIRCLE_OUTER_INNER_STROKE_WIDTH_MOVING = 1;
    private static final float DEFAULT_CIRCLE_OUTER_INNER_RADIUS_FRACTION = 0.6f;
    private static final float DEFAULT_CIRCLE_OUTER_INNER_RADIUS_FRACTION_MOVING = 0.6f;

    private static final float DEFAULT_CIRCLE_INNER_STROKE_WIDTH = 1;
    private static final float DEFAULT_CIRCLE_INNER_STROKE_WIDTH_MOVING = 1;
    private static final float DEFAULT_CIRCLE_INNER_RADIUS_FRACTION = 0.1f;
    private static final float DEFAULT_CIRCLE_INNER_RADIUS_FRACTION_MOVING = 0.1f;


    private static final float DEFAULT_CIRCLE_INNER_OUTER_STROKE_WIDTH = 1;
    private static final float DEFAULT_CIRCLE_INNER_OUTER_STROKE_WIDTH_MOVING = 1;
    private static final float DEFAULT_CIRCLE_INNER_OUTER_RADIUS_FRACTION = 0.2f;
    private static final float DEFAULT_CIRCLE_INNER_OUTER_RADIUS_FRACTION_MOVING = 0.2f;


    private static final float DEFAULT_CIRCLE_CONTROL_STROKE_WIDTH = 1;
    private static final float DEFAULT_CIRCLE_CONTROL_STROKE_WIDTH_MOVING = 1;
    private static final float DEFAULT_CIRCLE_CONTROL_RADIUS_FRACTION = 0.2f;
    private static final float DEFAULT_CIRCLE_CONTROL_RADIUS_FRACTION_MOVING = 0.2f;


    private static final float DEFAULT_CIRCLE_CONTROL_INNER_STROKE_WIDTH = 1;
    private static final float DEFAULT_CIRCLE_CONTROL_INNER_STROKE_WIDTH_MOVING = 1;
    private static final float DEFAULT_CIRCLE_CONTROL_INNER_RADIUS_FRACTION = 0.15f;
    private static final float DEFAULT_CIRCLE_CONTROL_INNER_RADIUS_FRACTION_MOVING = 0.15f;
    public static final int WORK_MODE_GESTURE = 0;
    public static final int WORK_MODE_GYROSCOPE = 1;


    private int mWorkMode = WORK_MODE_GESTURE;
    private float mGyroscopeDistance;
    private float mCircleX;
    private float mCircleY;
    private boolean mMoving;
    private float mMotionDownX;
    private float mMotionDownY;
    private boolean mOnInnerCircleTouched;
    private int mSlop;

    //文字参数
    private TextPaint mDirectionTextPaint;
    private TextPaint mDirectionTextPaintMoving;
    private int mDirectionTextColor;
    private int mDirectionTextColorMoving;
    private float mDirectionTextSize;
    private float mDirectionTextSizeMoving;
    private String mLeft;
    private String mRight;
    private String mForward;
    private String mBackward;

    private boolean mEnableControl = true;

    //三角形参数
    private Paint mDirectionTrianglePaint;
    private Paint mDirectionTrianglePaintMoving;
    private int mDirectionTriangleColor;
    private int mDirectionTriangleColorMoving;
    private float mDirectionTriangleEdge;
    private float mDirectionTriangleEdgeMoving;
    private float mDirectionTriangleStrokeWidth;
    private float mDirectionTriangleStrokeWidthMoving;
    private Path mPathTriangle;
    private float mTriangleLeftX, mTriangleLeftY;
    private float mTriangleRightX, mTriangleRightY;
    private float mTriangleTopX, mTriangleTopY;


    //最外围的圆的参数
    private Paint mCircleOuterPaint;
    private Paint mCircleOuterPaintMoving;
    private int mCircleOuterColor;
    private int mCircleOuterColorMoving;
    private float mCircleOuterStrokeWidth;
    private float mCircleOuterStrokeWidthMoving;
    private float mCircleOuterRadiusFraction;
    private float mCircleOuterRadiusFractionMoving;
    private float mCircleOuterRadius;
    private float mCircleOuterRadiusMoving;


    //紧挨着最外围的内部圆的参数
    private Paint mCircleOuterInnerPaint;
    private Paint mCircleOuterInnerPaintMoving;
    private int mCircleOuterInnerColor;
    private int mCircleOuterInnerColorMoving;
    private float mCircleOuterInnerStrokeWidth;
    private float mCircleOuterInnerStrokeWidthMoving;
    private float mCircleOuterInnerRadiusFraction;
    private float mCircleOuterInnerRadiusFractionMoving;
    private float mCircleOuterInnerRadius;
    private float mCircleOuterInnerRadiusMoving;


    //最内的圆的参数
    private Paint mCircleInnerPaint;
    private Paint mCircleInnerPaintMoving;
    private int mCircleInnerColor;
    private int mCircleInnerColorMoving;
    private float mCircleInnerStrokeWidth;
    private float mCircleInnerStrokeWidthMoving;
    private float mCircleInnerRadiusFraction;
    private float mCircleInnerRadiusFractionMoving;
    private float mCircleInnerRadius;
    private float mCircleInnerRadiusMoving;


    //紧挨着最内的圆的外面圆环的参数
    private Paint mCircleInnerOuterPaint;
    private Paint mCircleInnerOuterPaintMoving;
    private int mCircleInnerOuterColor;
    private int mCircleInnerOuterColorMoving;
    private float mCircleInnerOuterStrokeWidth;
    private float mCircleInnerOuterStrokeWidthMoving;
    private float mCircleInnerOuterRadiusFraction;
    private float mCircleInnerOuterRadiusFractionMoving;
    private float mCircleInnerOuterRadius;
    private float mCircleInnerOuterRadiusMoving;


    //随手势滚动的圆的参数参数
    private Paint mCircleControlPaint;
    private Paint mCircleControlPaintMoving;
    private int mCircleControlColor;
    private int mCircleControlColorMoving;
    private float mCircleControlStrokeWidth;
    private float mCircleControlStrokeWidthMoving;
    private float mCircleControlRadiusFraction;
    private float mCircleControlRadiusFractionMoving;
    private float mCircleControlRadius;
    private float mCircleControlRadiusMoving;


    //随手势滚动的圆内部圆的参数
    private Paint mCircleControlInnerPaint;
    private Paint mCircleControlInnerPaintMoving;
    private int mCircleControlInnerColor;
    private int mCircleControlInnerColorMoving;
    private float mCircleControlInnerStrokeWidth;
    private float mCircleControlInnerStrokeWidthMoving;
    private float mCircleControlInnerRadiusFraction;
    private float mCircleControlInnerRadiusFractionMoving;
    private float mCircleControlInnerRadius;
    private float mCircleControlInnerRadiusMoving;


    private float mControlCircleX;
    private float mControlCircleY;


    private OnInnerCircleMoveListener mOnInnerCircleMoveListener;

    public RemoteControlView(Context context) {
        this(context, null);
    }

    public RemoteControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RemoteControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RemoteControlView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RemoteControlView, defStyle, 0);
        mSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mMoving = false;
        initText(a);
        initTriangle(a);
        initOuterCircle(a);
        initOuterInnerCircle(a);
        initInnerCircle(a);
        initInnerOuterCircle(a);
        initControlCircle(a);
        initControlInnerCircle(a);
        setWorkMode(WORK_MODE_GESTURE);
        a.recycle();
    }

    private void initText(TypedArray a) {
        mLeft = getContext().getString(R.string.remote_control_direction_left);
        mRight = getContext().getString(R.string.remote_control_direction_right);
        mForward = getContext().getString(R.string.remote_control_direction_forward);
        mBackward = getContext().getString(R.string.remote_control_direction_backward);

        mDirectionTextColor = a.getColor(R.styleable.RemoteControlView_directionTextColor, DEFAULT_COLOR);
        mDirectionTextColorMoving = a.getColor(R.styleable.RemoteControlView_directionTextColorMoving, DEFAULT_COLOR_MOVING);
        mDirectionTextSize = a.getDimension(R.styleable.RemoteControlView_directionTextSize, DEFAULT_TEXT_SIZE);
        mDirectionTextSizeMoving = a.getDimension(R.styleable.RemoteControlView_directionTextSizeMoving, DEFAULT_TEXT_SIZE_MOVING);

        mDirectionTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mDirectionTextPaint.setStyle(Paint.Style.FILL);
        mDirectionTextPaint.setColor(mDirectionTextColor);
        mDirectionTextPaint.setTextSize(mDirectionTextSize);

        mDirectionTextPaintMoving = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mDirectionTextPaintMoving.setStyle(Paint.Style.FILL);
        mDirectionTextPaintMoving.setColor(mDirectionTextColorMoving);
        mDirectionTextPaintMoving.setTextSize(mDirectionTextSizeMoving);

    }

    private void initTriangle(TypedArray a) {
        mDirectionTriangleColor = a.getColor(R.styleable.RemoteControlView_directionTriangleColor, DEFAULT_COLOR);
        mDirectionTriangleColorMoving = a.getColor(R.styleable.RemoteControlView_directionTriangleColor, DEFAULT_COLOR_MOVING);
        mDirectionTriangleEdge = a.getDimension(R.styleable.RemoteControlView_directionTriangleEdge, DEFAULT_TRIANGLE_EDGE_LENGTH);
        mDirectionTriangleEdgeMoving = a.getDimension(R.styleable.RemoteControlView_directionTriangleEdgeMoving, DEFAULT_TRIANGLE_EDGE_LENGTH_MOVING);
        mDirectionTriangleStrokeWidth = a.getDimension(R.styleable.RemoteControlView_directionTriangleStrokeWidth, DEFAULT_DIRECTION_TRIANGLE_STROKE_WIDTH);
        mDirectionTriangleStrokeWidthMoving = a.getDimension(R.styleable.RemoteControlView_directionTriangleStrokeWidth, DEFAULT_DIRECTION_TRIANGLE_STROKE_WIDTH_MOVING);

        mDirectionTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDirectionTrianglePaint.setStyle(Paint.Style.FILL);
        mDirectionTrianglePaint.setColor(mDirectionTriangleColor);
        mDirectionTrianglePaint.setStrokeWidth(mDirectionTriangleStrokeWidth);

        mDirectionTrianglePaintMoving = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDirectionTrianglePaintMoving.setStyle(Paint.Style.FILL);
        mDirectionTrianglePaintMoving.setColor(mDirectionTriangleColorMoving);
        mDirectionTrianglePaintMoving.setStrokeWidth(mDirectionTriangleStrokeWidthMoving);

        mPathTriangle = new Path();
    }

    private void initOuterCircle(TypedArray a) {
        mCircleOuterColor = a.getColor(R.styleable.RemoteControlView_circleOuterColor, DEFAULT_COLOR);
        mCircleOuterColorMoving = a.getColor(R.styleable.RemoteControlView_circleOuterColorMoving, DEFAULT_COLOR_MOVING);
        mCircleOuterStrokeWidth = a.getDimension(R.styleable.RemoteControlView_circleOuterStrokeWidth, DEFAULT_CIRCLE_OUTER_STROKE_WIDTH);
        mCircleOuterStrokeWidthMoving = a.getDimension(R.styleable.RemoteControlView_circleOuterStrokeWidthMoving, DEFAULT_CIRCLE_OUTER_STROKE_WIDTH_MOVING);
        mCircleOuterRadiusFraction = a.getFloat(R.styleable.RemoteControlView_circleOuterRadiusFraction, DEFAULT_CIRCLE_OUTER_RADIUS_FRACTION);
        mCircleOuterRadiusFractionMoving = a.getFloat(R.styleable.RemoteControlView_circleOuterRadiusFractionMoving, DEFAULT_CIRCLE_OUTER_RADIUS_FRACTION_MOVING);

        if (mCircleOuterRadiusFraction < 0) {
            mCircleOuterRadiusFraction = 0;
        } else if (mCircleOuterRadiusFraction > 1) {
            mCircleOuterRadiusFraction = 1;
        }
        if (mCircleOuterRadiusFractionMoving < 0) {
            mCircleOuterRadiusFractionMoving = 0;
        } else if (mCircleOuterRadiusFractionMoving > 1) {
            mCircleOuterRadiusFractionMoving = 1;
        }

        mCircleOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleOuterPaint.setStyle(Paint.Style.STROKE);
        mCircleOuterPaint.setColor(mCircleOuterColor);
        mCircleOuterPaint.setStrokeWidth(mCircleOuterStrokeWidth);

        mCircleOuterPaintMoving = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleOuterPaintMoving.setStyle(Paint.Style.STROKE);
        mCircleOuterPaintMoving.setColor(mCircleOuterColorMoving);
        mCircleOuterPaintMoving.setStrokeWidth(mCircleOuterStrokeWidthMoving);
    }


    private void initOuterInnerCircle(TypedArray a) {
        mCircleOuterInnerColor = a.getColor(R.styleable.RemoteControlView_circleOuterInnerColor, DEFAULT_COLOR);
        mCircleOuterInnerColorMoving = a.getColor(R.styleable.RemoteControlView_circleOuterInnerColorMoving, DEFAULT_COLOR_MOVING);
        mCircleOuterInnerStrokeWidth = a.getDimension(R.styleable.RemoteControlView_circleOuterInnerStrokeWidth, DEFAULT_CIRCLE_OUTER_INNER_STROKE_WIDTH);
        mCircleOuterInnerStrokeWidthMoving = a.getDimension(R.styleable.RemoteControlView_circleOuterInnerStrokeWidthMoving, DEFAULT_CIRCLE_OUTER_INNER_STROKE_WIDTH_MOVING);
        mCircleOuterInnerRadiusFraction = a.getFloat(R.styleable.RemoteControlView_circleOuterInnerRadiusFraction, DEFAULT_CIRCLE_OUTER_INNER_RADIUS_FRACTION);
        mCircleOuterInnerRadiusFractionMoving = a.getFloat(R.styleable.RemoteControlView_circleOuterInnerRadiusFractionMoving, DEFAULT_CIRCLE_OUTER_INNER_RADIUS_FRACTION_MOVING);

        if (mCircleOuterInnerRadiusFraction < 0) {
            mCircleOuterInnerRadiusFraction = 0;
        } else if (mCircleOuterInnerRadiusFraction > 1) {
            mCircleOuterInnerRadiusFraction = 1;
        }
        if (mCircleOuterInnerRadiusFractionMoving < 0) {
            mCircleOuterInnerRadiusFractionMoving = 0;
        } else if (mCircleOuterInnerRadiusFractionMoving > 1) {
            mCircleOuterInnerRadiusFractionMoving = 1;
        }

        mCircleOuterInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleOuterInnerPaint.setStyle(Paint.Style.STROKE);
        mCircleOuterInnerPaint.setColor(mCircleOuterInnerColor);
        mCircleOuterInnerPaint.setStrokeWidth(mCircleOuterInnerStrokeWidth);

        mCircleOuterInnerPaintMoving = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleOuterInnerPaintMoving.setStyle(Paint.Style.STROKE);
        mCircleOuterInnerPaintMoving.setColor(mCircleOuterInnerColorMoving);
        mCircleOuterInnerPaintMoving.setStrokeWidth(mCircleOuterInnerStrokeWidthMoving);
    }

    public void setWorkMode(int workMode) {
        int mode = workMode;
        if (mode < WORK_MODE_GESTURE) {
            mode = WORK_MODE_GESTURE;
        } else if (mode > WORK_MODE_GYROSCOPE) {
            mode = WORK_MODE_GYROSCOPE;
        }
        if (mWorkMode != mode) {
            this.mWorkMode = mode;
            if (mEnableControl && mMoving) {
                reset();
            }

        }
    }

    public int getWorkMode() {
        return mWorkMode;
    }

    private void initInnerCircle(TypedArray a) {
        mCircleInnerColor = a.getColor(R.styleable.RemoteControlView_circleInnerColor, DEFAULT_COLOR);
        mCircleInnerColorMoving = a.getColor(R.styleable.RemoteControlView_circleInnerColorMoving, DEFAULT_COLOR_MOVING);
        mCircleInnerStrokeWidth = a.getDimension(R.styleable.RemoteControlView_circleInnerStrokeWidth, DEFAULT_CIRCLE_INNER_STROKE_WIDTH);
        mCircleInnerStrokeWidthMoving = a.getDimension(R.styleable.RemoteControlView_circleInnerStrokeWidthMoving, DEFAULT_CIRCLE_INNER_STROKE_WIDTH_MOVING);
        mCircleInnerRadiusFraction = a.getFloat(R.styleable.RemoteControlView_circleInnerRadiusFraction, DEFAULT_CIRCLE_INNER_RADIUS_FRACTION);
        mCircleInnerRadiusFractionMoving = a.getFloat(R.styleable.RemoteControlView_circleInnerRadiusFractionMoving, DEFAULT_CIRCLE_INNER_RADIUS_FRACTION_MOVING);

        if (mCircleInnerRadiusFraction < 0) {
            mCircleInnerRadiusFraction = 0;
        } else if (mCircleInnerRadiusFraction > 1) {
            mCircleInnerRadiusFraction = 1;
        }
        if (mCircleInnerRadiusFractionMoving < 0) {
            mCircleInnerRadiusFractionMoving = 0;
        } else if (mCircleInnerRadiusFractionMoving > 1) {
            mCircleInnerRadiusFractionMoving = 1;
        }

        mCircleInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleInnerPaint.setStyle(Paint.Style.FILL);
        mCircleInnerPaint.setColor(mCircleInnerColor);
        mCircleInnerPaint.setStrokeWidth(mCircleInnerStrokeWidth);

        mCircleInnerPaintMoving = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleInnerPaintMoving.setStyle(Paint.Style.FILL);
        mCircleInnerPaintMoving.setColor(mCircleInnerColorMoving);
        mCircleInnerPaintMoving.setStrokeWidth(mCircleInnerStrokeWidthMoving);
    }


    private void initInnerOuterCircle(TypedArray a) {
        mCircleInnerOuterColor = a.getColor(R.styleable.RemoteControlView_circleInnerOuterColor, DEFAULT_COLOR);
        mCircleInnerOuterColorMoving = a.getColor(R.styleable.RemoteControlView_circleInnerOuterColorMoving, DEFAULT_COLOR_MOVING);
        mCircleInnerOuterStrokeWidth = a.getDimension(R.styleable.RemoteControlView_circleInnerOuterStrokeWidth, DEFAULT_CIRCLE_INNER_OUTER_STROKE_WIDTH);
        mCircleInnerOuterStrokeWidthMoving = a.getDimension(R.styleable.RemoteControlView_circleInnerOuterStrokeWidthMoving, DEFAULT_CIRCLE_INNER_OUTER_STROKE_WIDTH_MOVING);
        mCircleInnerOuterRadiusFraction = a.getFloat(R.styleable.RemoteControlView_circleInnerOuterRadiusFraction, DEFAULT_CIRCLE_INNER_OUTER_RADIUS_FRACTION);
        mCircleInnerOuterRadiusFractionMoving = a.getFloat(R.styleable.RemoteControlView_circleInnerOuterRadiusFractionMoving, DEFAULT_CIRCLE_INNER_OUTER_RADIUS_FRACTION_MOVING);

        if (mCircleInnerOuterRadiusFraction < 0) {
            mCircleInnerOuterRadiusFraction = 0;
        } else if (mCircleInnerOuterRadiusFraction > 1) {
            mCircleInnerOuterRadiusFraction = 1;
        }
        if (mCircleInnerOuterRadiusFractionMoving < 0) {
            mCircleInnerOuterRadiusFractionMoving = 0;
        } else if (mCircleInnerOuterRadiusFractionMoving > 1) {
            mCircleInnerOuterRadiusFractionMoving = 1;
        }

        mCircleInnerOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleInnerOuterPaint.setStyle(Paint.Style.STROKE);
        mCircleInnerOuterPaint.setColor(mCircleInnerOuterColor);
        mCircleInnerOuterPaint.setStrokeWidth(mCircleInnerOuterStrokeWidth);

        mCircleInnerOuterPaintMoving = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleInnerOuterPaintMoving.setStyle(Paint.Style.STROKE);
        mCircleInnerOuterPaintMoving.setColor(mCircleInnerOuterColorMoving);
        mCircleInnerOuterPaintMoving.setStrokeWidth(mCircleInnerOuterStrokeWidthMoving);
    }


    private void initControlCircle(TypedArray a) {

        mCircleControlColor = a.getColor(R.styleable.RemoteControlView_circleControlColor, DEFAULT_COLOR);
        mCircleControlColorMoving = a.getColor(R.styleable.RemoteControlView_circleControlColorMoving, DEFAULT_COLOR_MOVING);
        mCircleControlStrokeWidth = a.getDimension(R.styleable.RemoteControlView_circleControlStrokeWidth, DEFAULT_CIRCLE_CONTROL_STROKE_WIDTH);
        mCircleControlStrokeWidthMoving = a.getDimension(R.styleable.RemoteControlView_circleControlStrokeWidthMoving, DEFAULT_CIRCLE_CONTROL_STROKE_WIDTH_MOVING);
        mCircleControlRadiusFraction = a.getFloat(R.styleable.RemoteControlView_circleControlRadiusFraction, DEFAULT_CIRCLE_CONTROL_RADIUS_FRACTION);
        mCircleControlRadiusFractionMoving = a.getFloat(R.styleable.RemoteControlView_circleControlRadiusFractionMoving, DEFAULT_CIRCLE_CONTROL_RADIUS_FRACTION_MOVING);

        if (mCircleControlRadiusFraction < 0) {
            mCircleControlRadiusFraction = 0;
        } else if (mCircleControlRadiusFraction > 1) {
            mCircleControlRadiusFraction = 1;
        }
        if (mCircleControlRadiusFractionMoving < 0) {
            mCircleControlRadiusFractionMoving = 0;
        } else if (mCircleControlRadiusFractionMoving > 1) {
            mCircleControlRadiusFractionMoving = 1;
        }

        mCircleControlPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleControlPaint.setStyle(Paint.Style.STROKE);
        mCircleControlPaint.setColor(mCircleControlColor);
        mCircleControlPaint.setStrokeWidth(mCircleControlStrokeWidth);

        mCircleControlPaintMoving = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleControlPaintMoving.setStyle(Paint.Style.STROKE);
        mCircleControlPaintMoving.setColor(mCircleControlColorMoving);
        mCircleControlPaintMoving.setStrokeWidth(mCircleControlStrokeWidthMoving);
    }

    private void initControlInnerCircle(TypedArray a) {
        mCircleControlInnerColor = a.getColor(R.styleable.RemoteControlView_circleControlInnerColor, DEFAULT_COLOR);
        mCircleControlInnerColorMoving = a.getColor(R.styleable.RemoteControlView_circleControlInnerColorMoving, DEFAULT_COLOR_MOVING);
        mCircleControlInnerStrokeWidth = a.getDimension(R.styleable.RemoteControlView_circleControlInnerStrokeWidth, DEFAULT_CIRCLE_CONTROL_INNER_STROKE_WIDTH);
        mCircleControlInnerStrokeWidthMoving = a.getDimension(R.styleable.RemoteControlView_circleControlInnerStrokeWidthMoving, DEFAULT_CIRCLE_CONTROL_INNER_STROKE_WIDTH_MOVING);
        mCircleControlInnerRadiusFraction = a.getFloat(R.styleable.RemoteControlView_circleControlInnerRadiusFraction, DEFAULT_CIRCLE_CONTROL_INNER_RADIUS_FRACTION);
        mCircleControlInnerRadiusFractionMoving = a.getFloat(R.styleable.RemoteControlView_circleControlInnerRadiusFractionMoving, DEFAULT_CIRCLE_CONTROL_INNER_RADIUS_FRACTION_MOVING);

        if (mCircleControlInnerRadiusFraction < 0) {
            mCircleControlInnerRadiusFraction = 0;
        } else if (mCircleControlInnerRadiusFraction > 1) {
            mCircleControlInnerRadiusFraction = 1;
        }
        if (mCircleControlInnerRadiusFractionMoving < 0) {
            mCircleControlInnerRadiusFractionMoving = 0;
        } else if (mCircleControlInnerRadiusFractionMoving > 1) {
            mCircleControlInnerRadiusFractionMoving = 1;
        }

        mCircleControlInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleControlInnerPaint.setStyle(Paint.Style.FILL);
        mCircleControlInnerPaint.setColor(mCircleControlInnerColor);
        mCircleControlInnerPaint.setStrokeWidth(mCircleControlInnerStrokeWidth);

        mCircleControlInnerPaintMoving = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleControlInnerPaintMoving.setStyle(Paint.Style.FILL);
        mCircleControlInnerPaintMoving.setColor(mCircleControlInnerColorMoving);
        mCircleControlInnerPaintMoving.setStrokeWidth(mCircleControlInnerStrokeWidthMoving);

    }


    public void setLeftText(String left) {
        if (!TextUtils.isEmpty(left)) {
            this.mLeft = left;
            invalidate();
        }
    }

    public void setRightText(String right) {
        if (!TextUtils.isEmpty(right)) {
            this.mRight = right;
            invalidate();
        }
    }

    public void setForwardText(String forward) {
        if (!TextUtils.isEmpty(forward)) {
            this.mForward = forward;
            invalidate();
        }
    }


    public void setBackward(String backward) {
        if (!TextUtils.isEmpty(backward)) {
            this.mBackward = backward;
        }
    }

    public void enableControl(boolean enableControl) {
        this.mEnableControl = enableControl;
        if (!enableControl && mMoving) {
            reset();
        }
    }

    public boolean isEnableControl() {
        return mEnableControl;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCircleX = getMeasuredWidth() / 2;
        mCircleY = getMeasuredHeight() / 2;
        float length = 0.5f * Math.min(getMeasuredWidth(), getMeasuredHeight());
        mCircleOuterRadius = length * mCircleOuterRadiusFraction;
        mCircleOuterRadiusMoving = length * mCircleOuterRadiusFractionMoving;
        mCircleOuterInnerRadius = length * mCircleOuterInnerRadiusFraction;
        mCircleOuterInnerRadiusMoving = length * mCircleOuterInnerRadiusFractionMoving;
        mCircleInnerRadius = length * mCircleInnerRadiusFraction;
        mCircleInnerRadiusMoving = length * mCircleInnerRadiusFractionMoving;
        mCircleInnerOuterRadius = length * mCircleInnerOuterRadiusFraction;
        mCircleInnerOuterRadiusMoving = length * mCircleInnerOuterRadiusFractionMoving;
        mCircleControlRadius = length * mCircleControlRadiusFraction;
        mCircleControlRadiusMoving = length * mCircleControlRadiusFractionMoving;
        mCircleControlInnerRadius = length * mCircleControlInnerRadiusFraction;
        mCircleControlInnerRadiusMoving = length * mCircleControlInnerRadiusFractionMoving;
        mGyroscopeDistance = mCircleOuterInnerRadius - mCircleInnerOuterRadius;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnableControl || getWorkMode() == WORK_MODE_GYROSCOPE) {
            return true;
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
                if (mOnInnerCircleTouched && canMove(event)) {
                    mMoving = true;
                    calibrateMotion(event);
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


    public void move(float fractionX, float fractionY) {
        if (!mEnableControl || getWorkMode() != WORK_MODE_GYROSCOPE) {
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
        float x = mCircleX + mGyroscopeDistance * fractionX;
        float y = mCircleY + mGyroscopeDistance * fractionY;
        calibrateXY(x, y);
    }


    private void calibrateXY(float x, float y) {
        float gravityX, gravityY;
        double theta;
        float x1, y1;
        if (x > mCircleX && y < mCircleY) {//第一象限
            theta = Math.atan((mCircleY - y) / (x - mCircleX));
            x1 = (float) (mCircleX + (mCircleOuterInnerRadius - mCircleInnerOuterRadius) * Math.cos(theta));
            y1 = (float) (mCircleY - (mCircleOuterInnerRadius - mCircleInnerOuterRadius) * Math.sin(theta));
            if (x > x1) {
                x = x1;
            }
            if (y < y1) {
                y = y1;
            }
            gravityX = (float) (mCircleX + mCircleOuterRadius * Math.cos(theta));
            gravityY = (float) (mCircleY - mCircleOuterRadius * Math.sin(theta));

            mTriangleLeftX = (float) (gravityX - 0.5f * mDirectionTriangleEdgeMoving * Math.sin(theta));
            mTriangleLeftY = (float) (gravityY - 0.5f * mDirectionTriangleEdgeMoving * Math.cos(theta));

            mTriangleRightX = (float) (gravityX + 0.5f * mDirectionTriangleEdgeMoving * Math.sin(theta));
            mTriangleRightY = (float) (gravityY + 0.5f * mDirectionTriangleEdgeMoving * Math.cos(theta));


            mTriangleTopX = (float) (gravityX + mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6) * Math.cos(theta));
            mTriangleTopY = (float) (gravityY - mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6) * Math.sin(theta));


        } else if (x > mCircleX && y > mCircleY) { //第二象限
            theta = Math.atan((y - mCircleY) / (x - mCircleX));
            x1 = (float) (mCircleX + (mCircleOuterInnerRadius - mCircleInnerOuterRadius) * Math.cos(theta));
            y1 = (float) (mCircleY + (mCircleOuterInnerRadius - mCircleInnerOuterRadius) * Math.sin(theta));
            if (x > x1) {
                x = x1;
            }
            if (y > y1) {
                y = y1;
            }

            gravityX = (float) (mCircleX + mCircleOuterRadius * Math.cos(theta));
            gravityY = (float) (mCircleY + mCircleOuterRadius * Math.sin(theta));


            mTriangleLeftX = (float) (gravityX + 0.5f * mDirectionTriangleEdgeMoving * Math.sin(theta));
            mTriangleLeftY = (float) (gravityY - 0.5f * mDirectionTriangleEdgeMoving * Math.cos(theta));


            mTriangleRightX = (float) (gravityX - 0.5f * mDirectionTriangleEdgeMoving * Math.sin(theta));
            mTriangleRightY = (float) (gravityY + 0.5f * mDirectionTriangleEdgeMoving * Math.cos(theta));


            mTriangleTopX = (float) (gravityX + mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6) * Math.cos(theta));
            mTriangleTopY = (float) (gravityY + mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6) * Math.sin(theta));

        } else if (x < mCircleX && y > mCircleY) {   //第三象限
            theta = Math.atan((y - mCircleY) / (mCircleX - x));
            x1 = (float) (mCircleX - (mCircleOuterInnerRadius - mCircleInnerOuterRadius) * Math.cos(theta));
            y1 = (float) (mCircleY + (mCircleOuterInnerRadius - mCircleInnerOuterRadius) * Math.sin(theta));
            if (x < x1) {
                x = x1;
            }
            if (y > y1) {
                y = y1;
            }

            gravityX = (float) (mCircleX - mCircleOuterRadius * Math.cos(theta));
            gravityY = (float) (mCircleY + mCircleOuterRadius * Math.sin(theta));


            mTriangleLeftX = (float) (gravityX + 0.5f * mDirectionTriangleEdgeMoving * Math.sin(theta));
            mTriangleLeftY = (float) (gravityY + 0.5f * mDirectionTriangleEdgeMoving * Math.cos(theta));

            mTriangleRightX = (float) (gravityX - 0.5f * mDirectionTriangleEdgeMoving * Math.sin(theta));
            mTriangleRightY = (float) (gravityY - 0.5f * mDirectionTriangleEdgeMoving * Math.cos(theta));

            mTriangleTopX = (float) (gravityX - mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6) * Math.cos(theta));
            mTriangleTopY = (float) (gravityY + mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6) * Math.sin(theta));


        } else if (x < mCircleX && y < mCircleY) {//第四象限
            theta = Math.atan((mCircleY - y) / (mCircleX - x));
            x1 = (float) (mCircleX - (mCircleOuterInnerRadius - mCircleInnerOuterRadius) * Math.cos(theta));
            y1 = (float) (mCircleY - (mCircleOuterInnerRadius - mCircleInnerOuterRadius) * Math.sin(theta));
            if (x < x1) {
                x = x1;
            }
            if (y < y1) {
                y = y1;
            }

            gravityX = (float) (mCircleX - mCircleOuterRadius * Math.cos(theta));
            gravityY = (float) (mCircleY - mCircleOuterRadius * Math.sin(theta));

            mTriangleLeftX = (float) (gravityX - 0.5f * mDirectionTriangleEdgeMoving * Math.sin(theta));
            mTriangleLeftY = (float) (gravityY + 0.5f * mDirectionTriangleEdgeMoving * Math.cos(theta));


            mTriangleRightX = (float) (gravityX + 0.5f * mDirectionTriangleEdgeMoving * Math.sin(theta));
            mTriangleRightY = (float) (gravityY - 0.5f * mDirectionTriangleEdgeMoving * Math.cos(theta));


            mTriangleTopX = (float) (gravityX - mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6) * Math.cos(theta));
            mTriangleTopY = (float) (gravityY - mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6) * Math.sin(theta));


        } else if (x == mCircleX) {

            if (y > mCircleY + (mCircleOuterInnerRadius - mCircleInnerOuterRadius)) {
                y = mCircleY + (mCircleOuterInnerRadius - mCircleInnerOuterRadius);
            } else if (y < (mCircleY - (mCircleOuterInnerRadius - mCircleInnerOuterRadius))) {
                y = mCircleY - (mCircleOuterInnerRadius - mCircleInnerOuterRadius);
            }

            if (y > mCircleY) {
                mTriangleLeftX = mCircleX + 0.5f * mDirectionTriangleEdgeMoving;
                mTriangleLeftY = mCircleY + mCircleOuterRadius;

                mTriangleRightX = mCircleX - 0.5f * mDirectionTriangleEdgeMoving;
                mTriangleRightY = mCircleY + mCircleOuterRadius;

                mTriangleTopX = mCircleX;
                mTriangleTopY = (float) (mCircleY + mCircleOuterRadius + mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6));

            } else if (y < mCircleY) {


                mTriangleLeftX = mCircleX - 0.5f * mDirectionTriangleEdgeMoving;
                mTriangleLeftY = mCircleY - mCircleOuterRadius;

                mTriangleRightX = mCircleX + 0.5f * mDirectionTriangleEdgeMoving;
                mTriangleRightY = mCircleY - mCircleOuterRadius;

                mTriangleTopX = mCircleX;
                mTriangleTopY = (float) (mCircleY - mCircleOuterRadius - mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6));

            }
        } else if (y == mCircleY) {
            if (x > mCircleX + (mCircleOuterInnerRadius - mCircleInnerOuterRadius)) {
                x = mCircleX + (mCircleOuterInnerRadius - mCircleInnerOuterRadius);
            } else if (x < (mCircleX - (mCircleOuterInnerRadius - mCircleInnerOuterRadius))) {
                x = mCircleX - (mCircleOuterInnerRadius - mCircleInnerOuterRadius);
            }


            if (x > mCircleX) {
                mTriangleLeftX = mCircleX + mCircleOuterRadius;
                mTriangleLeftY = mCircleY - 0.5f * mDirectionTriangleEdgeMoving;

                mTriangleRightX = mCircleX + mCircleOuterRadius;
                mTriangleRightY = mCircleY + 0.5f * mDirectionTriangleEdgeMoving;

                mTriangleTopX = (float) (mCircleX + mCircleOuterRadius + mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6));
                mTriangleTopY = mCircleY;

            } else if (x < mCircleX) {


                mTriangleLeftX = mCircleX - mCircleOuterRadius;
                mTriangleLeftY = mCircleY + 0.5f * mDirectionTriangleEdgeMoving;

                mTriangleRightX = mCircleX - mCircleOuterRadius;
                mTriangleRightY = mCircleY - 0.5f * mDirectionTriangleEdgeMoving;

                mTriangleTopX = (float) (mCircleX - mCircleOuterRadius - mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6));
                mTriangleTopY = mCircleY;

            }


        }
        mControlCircleX = x;
        mControlCircleY = y;
        if (mOnInnerCircleMoveListener != null) {
            float fractionX = (mCircleX - mControlCircleX) / (mCircleOuterInnerRadius - mCircleInnerOuterRadius);
            float fractionY = (mCircleY - mControlCircleY) / (mCircleOuterInnerRadius - mCircleInnerOuterRadius);
            mOnInnerCircleMoveListener.onInnerCircleMove(mControlCircleX, mControlCircleY, fractionX, fractionY);
        }
        invalidate();
    }

    private void calibrateMotion(MotionEvent event) {
        calibrateXY(event.getX(), event.getY());
    }

    private boolean canMove(MotionEvent event) {
        return (Math.abs(event.getX() - mMotionDownX) > mSlop || Math.abs(event.getX() - mMotionDownY) > mSlop);
    }


    private void reset() {
        CirclePosition startValue = new CirclePosition(mControlCircleX, mControlCircleY);
        CirclePosition endValue = new CirclePosition(mCircleX, mCircleY);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new PositionEvaluator(), startValue, endValue);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                CirclePosition circlePosition = (CirclePosition) animation.getAnimatedValue();
                mControlCircleX = circlePosition.getX();
                mControlCircleY = circlePosition.getY();
                if (mOnInnerCircleMoveListener != null) {
                    float fractionX = (mCircleX - mControlCircleX) / (mCircleOuterInnerRadius - mCircleInnerOuterRadius);
                    float fractionY = (mCircleY - mControlCircleY) / (mCircleOuterInnerRadius - mCircleInnerOuterRadius);
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


    private boolean onInnerCircleTouched(MotionEvent event) {
        float radius = 0.5f * Math.min(getWidth(), getHeight()) * mCircleInnerOuterRadiusFractionMoving;
        radius = Math.max(radius, 0.5f * Math.min(getWidth(), getHeight()) * mCircleInnerOuterRadiusFraction);
        float x = event.getX();
        float y = event.getY();
        return (x - mCircleX) * (x - mCircleX) + (y - mCircleY) * (y - mCircleY) <= radius * radius;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawForwardText(canvas);
        drawBackwardText(canvas);
        drawLeftText(canvas);
        drawRightText(canvas);
        drawOuterCircle(canvas);
        drawOuterInnerCircle(canvas);
        drawInnerCircle(canvas);
        drawInnerOuterCircle(canvas);
        drawTriangle(canvas);
        drawControlCircle(canvas);
        drawControlInnerCircle(canvas);
    }

    private void drawControlCircle(Canvas canvas) {
        if (mMoving) {
            canvas.drawCircle(mControlCircleX, mControlCircleY, mCircleControlRadiusMoving, mCircleControlPaintMoving);
        } else {
            canvas.drawCircle(mCircleX, mCircleY, mCircleControlRadius, mCircleControlPaint);
        }
    }

    private void drawControlInnerCircle(Canvas canvas) {
        if (mMoving) {
            canvas.drawCircle(mControlCircleX, mControlCircleY, mCircleControlInnerRadiusMoving, mCircleControlInnerPaintMoving);
        } else {
            canvas.drawCircle(mCircleX, mCircleY, mCircleControlInnerRadius, mCircleControlInnerPaint);
        }


    }

    private void drawOuterCircle(Canvas canvas) {
        if (mMoving) {
            canvas.drawCircle(mCircleX, mCircleY, mCircleOuterRadiusMoving, mCircleOuterPaintMoving);
        } else {
            canvas.drawCircle(mCircleX, mCircleY, mCircleOuterRadius, mCircleOuterPaint);
        }
    }

    private void drawOuterInnerCircle(Canvas canvas) {
        if (mMoving) {
            canvas.drawCircle(mCircleX, mCircleY, mCircleOuterInnerRadiusMoving, mCircleOuterInnerPaintMoving);
        } else {
            canvas.drawCircle(mCircleX, mCircleY, mCircleOuterInnerRadius, mCircleOuterInnerPaint);
        }
    }


    private void drawInnerCircle(Canvas canvas) {
        if (mMoving) {
            canvas.drawCircle(mCircleX, mCircleY, mCircleInnerRadiusMoving, mCircleInnerPaintMoving);
        } else {
            canvas.drawCircle(mCircleX, mCircleY, mCircleInnerRadius, mCircleInnerPaint);
        }

    }

    private void drawInnerOuterCircle(Canvas canvas) {
        if (mMoving) {
            canvas.drawCircle(mCircleX, mCircleY, mCircleInnerOuterRadiusMoving, mCircleInnerOuterPaintMoving);
        } else {
            canvas.drawCircle(mCircleX, mCircleY, mCircleInnerOuterRadius, mCircleInnerOuterPaint);
        }
    }

    private void drawTriangle(Canvas canvas) {
        if (mMoving) {
            mPathTriangle.reset();
            mPathTriangle.moveTo(mTriangleLeftX, mTriangleLeftY);
            mPathTriangle.lineTo(mTriangleRightX, mTriangleRightY);
            mPathTriangle.lineTo(mTriangleTopX, mTriangleTopY);
            mPathTriangle.close();
            canvas.drawPath(mPathTriangle, mDirectionTrianglePaintMoving);
        } else {

        }

    }

    private void drawForwardText(Canvas canvas) {
        if (mMoving) {
            float baseX = mCircleX - 0.5f * mDirectionTextPaintMoving.measureText(mForward);
            Paint.FontMetrics fontMetrics = mDirectionTextPaintMoving.getFontMetrics();
            float baseY = mCircleY - mCircleOuterRadiusMoving - mDirectionTriangleEdgeMoving * ((float) Math.cos(Math.PI / 6)) - fontMetrics.bottom;
            canvas.drawText(mForward, baseX, baseY, mDirectionTextPaintMoving);
        } else {
            float baseX = mCircleX - 0.5f * mDirectionTextPaint.measureText(mForward);
            Paint.FontMetrics fontMetrics = mDirectionTextPaint.getFontMetrics();
            float baseY = mCircleY - mCircleOuterRadius - mDirectionTriangleEdge * ((float) Math.cos(Math.PI / 6)) - fontMetrics.bottom;
            canvas.drawText(mForward, baseX, baseY, mDirectionTextPaint);
        }


    }

    private void drawBackwardText(Canvas canvas) {
        if (mMoving) {
            float baseX = mCircleX - 0.5f * mDirectionTextPaintMoving.measureText(mBackward);
            Paint.FontMetrics fontMetrics = mDirectionTextPaintMoving.getFontMetrics();
            float baseY = (float) (mCircleY + mCircleOuterRadiusMoving + mDirectionTriangleEdgeMoving * Math.cos(Math.PI / 6) - fontMetrics.top);
            canvas.drawText(mBackward, baseX, baseY, mDirectionTextPaintMoving);
        } else {
            float baseX = mCircleX - 0.5f * mDirectionTextPaint.measureText(mBackward);
            Paint.FontMetrics fontMetrics = mDirectionTextPaint.getFontMetrics();
            float baseY = (float) (mCircleY + mCircleOuterRadius + mDirectionTriangleEdge * Math.cos(Math.PI / 6) - fontMetrics.top);
            canvas.drawText(mBackward, baseX, baseY, mDirectionTextPaint);
        }

    }

    private void drawLeftText(Canvas canvas) {
        if (mMoving) {
            float baseX = mCircleX - mDirectionTextPaintMoving.measureText(mLeft) - mDirectionTriangleEdgeMoving * ((float) Math.cos(Math.PI / 6)) - mCircleOuterRadius;
            Paint.FontMetrics fontMetrics = mDirectionTextPaintMoving.getFontMetrics();
            float baseY = mCircleY + 0.5f * (fontMetrics.descent - fontMetrics.ascent) - fontMetrics.bottom;
            canvas.drawText(mLeft, baseX, baseY, mDirectionTextPaintMoving);
        } else {
            float baseX = mCircleX - mDirectionTextPaint.measureText(mLeft) - mDirectionTriangleEdge * ((float) Math.cos(Math.PI / 6)) - mCircleOuterRadiusMoving;
            Paint.FontMetrics fontMetrics = mDirectionTextPaint.getFontMetrics();
            float baseY = mCircleY + 0.5f * (fontMetrics.descent - fontMetrics.ascent) - fontMetrics.bottom;
            canvas.drawText(mLeft, baseX, baseY, mDirectionTextPaint);
        }


    }


    private void drawRightText(Canvas canvas) {
        if (mMoving) {
            float baseX = mCircleX + mCircleOuterRadiusMoving + mDirectionTriangleEdgeMoving * ((float) Math.cos(Math.PI / 6));
            Paint.FontMetrics fontMetrics = mDirectionTextPaintMoving.getFontMetrics();
            float baseY = mCircleY + 0.5f * (fontMetrics.descent - fontMetrics.ascent) - fontMetrics.bottom;
            canvas.drawText(mRight, baseX, baseY, mDirectionTextPaintMoving);
        } else {
            float baseX = mCircleX + mCircleOuterRadius + mDirectionTriangleEdge * ((float) Math.cos(Math.PI / 6));
            Paint.FontMetrics fontMetrics = mDirectionTextPaint.getFontMetrics();
            float baseY = mCircleY + 0.5f * (fontMetrics.descent - fontMetrics.ascent) - fontMetrics.bottom;
            canvas.drawText(mRight, baseX, baseY, mDirectionTextPaint);
        }
    }

    public interface OnInnerCircleMoveListener {
        void onInnerCircleMove(float x, float y, float fractionX, float fractionY);
    }

    public void setOnInnerCircleMoveListener(OnInnerCircleMoveListener listener) {
        this.mOnInnerCircleMoveListener = listener;
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
}
