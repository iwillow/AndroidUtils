package com.iwillow.app.android.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.iwillow.app.android.R;

/**
 * Created by ddx on 2017/1/7.
 */

public class BottomDecorView extends View {

    private Paint mBitmapPaint;
    private Bitmap mBackgroundBitmap;

    public BottomDecorView(Context context) {
        this(context, null);
    }

    public BottomDecorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomDecorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BottomDecorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setStyle(Paint.Style.STROKE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bottom_img);
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setScale(0.34f, 0.34f);
        mBackgroundBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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
            width = (int) dp2px(getResources(), 108.0f);
        } else {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            height = (int) dp2px(getResources(), 25.7f);
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


    public float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
