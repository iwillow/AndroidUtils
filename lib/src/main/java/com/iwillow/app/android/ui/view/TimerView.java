package com.iwillow.app.android.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.iwillow.app.android.R;

import static com.iwillow.app.android.util.DimenUtil.dp2px;


/**
 * Created by https://github.com/iwillow/ on 2017/2/16.
 */

public class TimerView extends View {

    private int mDefaultColor;
    private int mWarningColor;
    private int mOverColor;
    private float mProgress;
    private float mWarnProgress = 11.0f / 12;
    private Paint mDotPaint;
    private Paint mThinCirclePaint;
    private Paint mThickCirclePaint;
    private Paint mThickCircleDotPaint;
    private Paint mInnerFillCirclePaint;
    private Paint mInnerStrokeCirclePaint;
    private float mRadius;
    private float mDotCircleRadius;
    private float mThinCircleRadius;
    private float mThickCircleRadius;
    private float mThickCircleDotRadius;


    public TimerView(Context context) {
        super(context);
        init();
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TimerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        mDefaultColor = getResources().getColor(R.color.time_default_color);
        mWarningColor = getResources().getColor(R.color.time_warning_color);
        mOverColor = getResources().getColor(R.color.time_up_color);
        mDotCircleRadius = dp2px(getResources(), 2f);
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setColor(mDefaultColor);
        mDotPaint.setStyle(Paint.Style.FILL);


        mThinCircleRadius = dp2px(getResources(), 1.5f);
        mThinCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThinCirclePaint.setColor(Color.WHITE);
        mThinCirclePaint.setStyle(Paint.Style.STROKE);
        mThinCirclePaint.setStrokeWidth(mThinCircleRadius);


        mThickCircleRadius = dp2px(getResources(), 5);
        mThickCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThickCirclePaint.setColor(mDefaultColor);
        mThickCirclePaint.setStyle(Paint.Style.STROKE);
        mThickCirclePaint.setStrokeWidth(mThickCircleRadius);

        mThickCircleDotRadius = dp2px(getResources(), 2.5f);
        mThickCircleDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThickCircleDotPaint.setColor(mDefaultColor);
        mThickCircleDotPaint.setStyle(Paint.Style.FILL);


        mInnerFillCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerFillCirclePaint.setStyle(Paint.Style.FILL);
        mInnerFillCirclePaint.setColor(getResources().getColor(R.color.timer_inner_fill_color));

        mInnerStrokeCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerStrokeCirclePaint.setStyle(Paint.Style.STROKE);
        mInnerStrokeCirclePaint.setStrokeWidth(dp2px(getResources(), 0.5f));
        mInnerStrokeCirclePaint.setColor(getResources().getColor(R.color.timer_inner_stroke_color));


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
            height = (int) dp2px(getResources(), 100f);
        } else {
            height = heightSize;
        }
        int l = Math.min(width, height);
        setMeasuredDimension(l, l);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = 1.0f * getWidth() / 2 * 0.9f;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgressDots(canvas);
        drawProgressCircle(canvas);
        drawInnerCircle(canvas);
    }

    private void drawProgressDots(Canvas canvas) {
        float currentDegree = 100 * 3.6f * mProgress;
        float baseDegree = 360 * mWarnProgress;
        for (int i = 0; i < 12; i++) {
            canvas.save();
            canvas.translate(getWidth() / 2, getHeight() / 2);
            canvas.rotate(90 + 30 * i);
            if (30 * i <= currentDegree) {
                if (currentDegree == 0) {
                    mDotPaint.setColor(Color.WHITE);
                } else if (currentDegree < baseDegree && currentDegree > 0) {
                    mDotPaint.setColor(mDefaultColor);
                } else if (currentDegree >= baseDegree && currentDegree < 360f) {
                    mDotPaint.setColor(mWarningColor);
                } else {
                    mDotPaint.setColor(mOverColor);
                }
            } else {
                mDotPaint.setColor(Color.WHITE);
            }
            canvas.drawCircle(-mRadius, 0, mDotCircleRadius, mDotPaint);
            canvas.restore();
        }
    }

    private void drawProgressCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float r = mRadius * 0.85f;
        float currentDegree = 100 * 3.6f * mProgress;
        float baseDegree = 360 * mWarnProgress;
        canvas.drawCircle(0, 0, r, mThinCirclePaint);
        RectF rectF = new RectF(-r, -r, r, r);
        if (currentDegree < baseDegree) {
            mThickCirclePaint.setColor(mDefaultColor);
            mThickCircleDotPaint.setColor(mDefaultColor);
        } else if (currentDegree >= baseDegree && currentDegree < 360f) {
            mThickCirclePaint.setColor(mWarningColor);
            mThickCircleDotPaint.setColor(mWarningColor);
        } else {
            mThickCirclePaint.setColor(mOverColor);
            mThickCircleDotPaint.setColor(mOverColor);
        }

        if (currentDegree > 0) {
            canvas.drawArc(rectF, -90, currentDegree, false, mThickCirclePaint);
            canvas.drawCircle(0, -r, mThickCircleDotRadius, mThickCircleDotPaint);
            canvas.rotate(currentDegree);
            canvas.drawCircle(0, -r, mThickCircleDotRadius, mThickCircleDotPaint);
        }
        canvas.restore();
    }

    private void drawInnerCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float r1 = mRadius * 0.75f;
        canvas.drawCircle(0, 0, r1, mInnerFillCirclePaint);
        float r2 = mRadius * 0.65f;
        canvas.drawCircle(0, 0, r2, mInnerStrokeCirclePaint);
        canvas.restore();
    }

    public void setProgress(float progress) {
        float p = progress;
        if (p < 0) {
            p = 0;
        }
        if (p > 1f) {
            p = 1f;
        }
        if (mProgress != p) {
            mProgress = p;
            invalidate();
        }
    }

    public void setWarnProgress(float warnProgress) {
        float temp = warnProgress;
        if (temp < 0) {
            temp = 0;
        } else if (warnProgress > 1f) {
            temp = 1;
        }
        if (mWarnProgress != temp) {
            mWarnProgress = temp;
            invalidate();
        }
    }

}
