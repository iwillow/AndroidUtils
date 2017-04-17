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
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.iwillow.app.android.R;

import static com.iwillow.app.android.util.DimenUtil.dp2px;
import static com.iwillow.app.android.util.DimenUtil.sp2px;

/**
 * Created by https://github.com/iwillow/ on 2017/1/7.
 */

public class SpeedView extends View {

    private String mSpeed;

    private Bitmap mBackgroundScaleBitmap;
    private Paint mBackgroundPaint;
    private TextPaint mSpeedPaint;
    private TextPaint mUnitPaint;
    private float mGap;
    private float mOffset;

    public SpeedView(Context context) {
        this(context, null);
    }

    public SpeedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpeedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);

        mSpeedPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSpeedPaint.setColor(Color.WHITE);
        mSpeedPaint.setDither(true);
        mSpeedPaint.setStyle(Paint.Style.FILL);
        mSpeedPaint.setTextSize(sp2px(getResources(), 50f));

        mUnitPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mUnitPaint.setColor(Color.WHITE);
        mUnitPaint.setDither(true);
        mUnitPaint.setStyle(Paint.Style.FILL);
        mUnitPaint.setTextSize(sp2px(getResources(), 20f));

        mGap = dp2px(getResources(), 5f);
        mOffset = dp2px(getResources(), 15f);
        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.speed_background);
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setScale(0.32f, 0.32f);
        mBackgroundScaleBitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
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
            width = (int) dp2px(getResources(), 2 * 74.3f);
        } else {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            height = (int) dp2px(getResources(), 2 * 17.7f);
        } else {
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackgroundBitmap(canvas);
        drawSpeed(canvas, mSpeed);
    }

    private void drawBackgroundBitmap(Canvas canvas) {
        int width = this.getWidth();    //获取宽度
        int height = this.getHeight();
        int left = width / 2 - mBackgroundScaleBitmap.getWidth() / 2;
        int top = height / 2 - mBackgroundScaleBitmap.getHeight() / 2;
        int right = width / 2 + mBackgroundScaleBitmap.getWidth() / 2;
        int bottom = height / 2 + mBackgroundScaleBitmap.getHeight() / 2;
        Rect dst = new Rect(left, top, right, bottom);
        canvas.drawBitmap(mBackgroundScaleBitmap, null, dst, mBackgroundPaint);
    }

    private void drawSpeed(Canvas canvas, String speed) {
        String text;
        if (TextUtils.isEmpty(speed)) {
            text = "00.0";
        } else {
            text = speed;
        }
        Paint.FontMetrics fontMetrics = mSpeedPaint.getFontMetrics();
        float textWidth = mSpeedPaint.measureText(text);
        float x = 0.5f * getWidth() - 0.5f * textWidth;
        float y = 0.5f * getHeight() - fontMetrics.descent + 0.5f * (fontMetrics.descent - fontMetrics.ascent);
        canvas.drawText(text, x - mOffset, y, mSpeedPaint);
        canvas.drawText("km/h", x + textWidth - mOffset + mGap, y, mUnitPaint);
    }

    public void setSpeed(String speed) {
        this.mSpeed = speed;
        invalidate();
    }
}
