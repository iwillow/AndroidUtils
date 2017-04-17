package com.iwillow.app.android.ui.view;

import android.content.Context;
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
import android.util.AttributeSet;
import android.view.View;

import com.iwillow.app.android.R;

import static com.iwillow.app.android.util.DimenUtil.dp2px;
import static com.iwillow.app.android.util.DimenUtil.sp2px;

/**
 * Created by https://github.com/iwillow/ on 2017/4/17.
 */
public class BatteryView extends View {
    private Paint mBitmapPaint;
    private TextPaint mBatteryPaint;
    private TextPaint mBatteryWarningPaint;
    private Bitmap mBackgroundBitmap;
    private Bitmap mBatteryBitmap20;
    private Bitmap mBatteryBitmap40;
    private Bitmap mBatteryBitmap60;
    private Bitmap mBatteryBitmap80;
    private Bitmap mBatteryBitmap100;
    private int mBattery;
    private String mBatteryText;
    private float mOffsetLeft;
    private float mOffsetRight;
    private Matrix mMatrix = new Matrix();

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mOffsetLeft = dp2px(getResources(), 15f);
        mOffsetRight = dp2px(getResources(), 8f);
        mBatteryText = "0%";
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setStyle(Paint.Style.STROKE);


        mBatteryPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mBatteryPaint.setColor(Color.WHITE);
        mBatteryPaint.setDither(true);
        mBatteryPaint.setStyle(Paint.Style.FILL);
        mBatteryPaint.setTextSize(sp2px(getResources(), 15f));

        mBatteryWarningPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mBatteryWarningPaint.setColor(Color.RED);
        mBatteryWarningPaint.setDither(true);
        mBatteryWarningPaint.setStyle(Paint.Style.FILL);
        mBatteryWarningPaint.setTextSize(sp2px(getResources(), 15f));


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.battery_background);

        mMatrix.reset();
        mMatrix.setScale(0.50f, 0.50f);
        mBackgroundBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mMatrix, true);

        Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.drawable.battery_20);
        mMatrix.reset();
        mMatrix.setScale(0.5f, 0.5f);
        mBatteryBitmap20 = Bitmap.createBitmap(b2, 0, 0, b2.getWidth(), b2.getHeight(), mMatrix, true);


        Bitmap b4 = BitmapFactory.decodeResource(getResources(), R.drawable.battery_40);
        mMatrix.reset();
        mMatrix.setScale(0.5f, 0.5f);
        mBatteryBitmap40 = Bitmap.createBitmap(b4, 0, 0, b4.getWidth(), b4.getHeight(), mMatrix, true);

        Bitmap b6 = BitmapFactory.decodeResource(getResources(), R.drawable.battery_60);
        mMatrix.reset();
        mMatrix.setScale(0.5f, 0.5f);
        mBatteryBitmap60 = Bitmap.createBitmap(b6, 0, 0, b6.getWidth(), b6.getHeight(), mMatrix, true);

        Bitmap b8 = BitmapFactory.decodeResource(getResources(), R.drawable.battery_80);
        mMatrix.reset();
        mMatrix.setScale(0.5f, 0.5f);
        mBatteryBitmap80 = Bitmap.createBitmap(b8, 0, 0, b8.getWidth(), b8.getHeight(), mMatrix, true);

        Bitmap b10 = BitmapFactory.decodeResource(getResources(), R.drawable.battery_100);
        mMatrix.reset();
        mMatrix.setScale(0.5f, 0.5f);
        mBatteryBitmap100 = Bitmap.createBitmap(b10, 0, 0, b10.getWidth(), b10.getHeight(), mMatrix, true);

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
            height = (int) dp2px(getResources(), 50f);
        } else {
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackgroundBitmap(canvas);
        drawBatteryBitmap(canvas, mBatteryText);
    }

    public void setBattery(int battery) {
        int temp = battery;
        if (temp < 0) {
            temp = 0;
        } else if (temp > 100) {
            temp = 100;
        }
        if (mBattery != temp) {
            mBattery = temp;
            mBatteryText = mBattery + "%";
            invalidate();
        }
    }

    private void drawBackgroundBitmap(Canvas canvas) {
        int width = this.getWidth();    //获取宽度
        int height = this.getHeight();
        int left = width / 2 - mBackgroundBitmap.getWidth() / 2;
        int top = height / 2 - mBackgroundBitmap.getHeight() / 2;
        int right = width / 2 + mBackgroundBitmap.getWidth() / 2;
        int bottom = height / 2 + mBackgroundBitmap.getHeight() / 2;
        Rect dst = new Rect(left, top, right, bottom);
        canvas.drawBitmap(mBackgroundBitmap, null, dst, mBitmapPaint);
    }

    private void drawBatteryBitmap(Canvas canvas, String battery) {
        int width = this.getWidth();    //获取宽度
        int height = this.getHeight();
        int left = (int) (-mOffsetLeft + width / 2 - mBatteryBitmap20.getWidth() / 2);
        int top = height / 2 - mBatteryBitmap20.getHeight() / 2;
        int right = (int) (-mOffsetLeft + width / 2 + mBatteryBitmap20.getWidth() / 2);
        int bottom = height / 2 + mBatteryBitmap20.getHeight() / 2;
        if (mBattery < 10) {
            left += dp2px(getResources(), 10);
            right += dp2px(getResources(), 10);
        } else if (mBattery < 100) {
            left += dp2px(getResources(), -2);
            right += dp2px(getResources(), -2);
        }

        Rect dst = new Rect(left, top, right, bottom);
        if (mBattery < 20) {
            canvas.drawBitmap(mBatteryBitmap20, null, dst, mBitmapPaint);
        } else if (mBattery <= 40) {
            canvas.drawBitmap(mBatteryBitmap40, null, dst, mBitmapPaint);
        } else if (mBattery <= 60) {
            canvas.drawBitmap(mBatteryBitmap60, null, dst, mBitmapPaint);
        } else if (mBattery <= 80) {
            canvas.drawBitmap(mBatteryBitmap80, null, dst, mBitmapPaint);
        } else if (mBattery <= 100) {
            canvas.drawBitmap(mBatteryBitmap100, null, dst, mBitmapPaint);
        }

        Paint.FontMetrics fontMetrics = mBatteryPaint.getFontMetrics();
        float x = mOffsetRight + 0.5f * getWidth();
        float y = 0.5f * getHeight() - fontMetrics.descent + 0.5f * (fontMetrics.descent - fontMetrics.ascent);
        if (mBattery < 10) {
            x += dp2px(getResources(), 10);
        } else if (mBattery == 100) {
            x += dp2px(getResources(), -3);
        }

        if (mBattery < 20) {
            canvas.drawText(battery, x, y, mBatteryWarningPaint);
        } else {
            canvas.drawText(battery, x, y, mBatteryPaint);
        }
    }


}
