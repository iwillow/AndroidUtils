package com.iwillow.app.samples.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.iwillow.app.android.ui.view.GestureInstrumentView;
import com.iwillow.app.android.ui.view.SpeedScaleView;
import com.iwillow.app.samples.R;


public class CoolARemoteControlActivity extends AppCompatActivity {
    private GestureInstrumentView mGestureInstrumentView;
    private TextView mTvLogFraction;
    private SpeedScaleView mSpeedScaleView;
    private TextView mTvLogSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool_remote_control_a);
        mTvLogFraction = (TextView) findViewById(R.id.tv_log_fraction);
        mTvLogFraction.setText("水平方向系数:0\n垂直方向系数:0");
        mGestureInstrumentView = (GestureInstrumentView) findViewById(R.id.instrumentView);
        mGestureInstrumentView.enableControl(true);
        mGestureInstrumentView.setOnInnerCircleMoveListener(new GestureInstrumentView.OnInnerCircleMoveListener() {
            @Override
            public void onInnerCircleMove(float x, float y, float fractionX, float fractionY) {
                mTvLogFraction.setText("水平方向系数:" + fractionX + "\n垂直方向系数:" + fractionY);
            }
        });
        mTvLogSpeed = (TextView) findViewById(R.id.tv_log_speed);
        mTvLogSpeed.setText("最大速度:0");
        mSpeedScaleView = (SpeedScaleView) findViewById(R.id.speedScaleView);
        mSpeedScaleView.setProgressChangeListener(new SpeedScaleView.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(SpeedScaleView scaleView, int progress, boolean fromUser) {
                mTvLogSpeed.setText("最大速度:" + progress);
            }

            @Override
            public void onStartTrackingTouch(SpeedScaleView seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SpeedScaleView seekBar) {

            }
        });

    }
}
