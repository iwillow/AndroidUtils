package com.iwillow.app.samples.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.iwillow.app.android.ui.view.RemoteControlView;
import com.iwillow.app.android.util.ToastUtil;
import com.iwillow.app.samples.R;


public class SimpleRemoteControlActivity extends AppCompatActivity implements SensorEventListener {
    private Sensor mGravitySensor;
    private SensorManager mSensorManager;
    private RemoteControlView mRemoteControlView;
    private TextView mTvLog;
    private boolean mSupportGravity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_remote_control);
        initGravitySensor();
        mRemoteControlView = (RemoteControlView) findViewById(R.id.remote_control_view);
        mTvLog = (TextView) findViewById(R.id.tv_log);
        mTvLog.setText("水平方向系数:0\n垂直方向系数:0");
        mRemoteControlView.setOnInnerCircleMoveListener(new RemoteControlView.OnInnerCircleMoveListener() {
            @Override
            public void onInnerCircleMove(float x, float y, float fractionX, float fractionY) {
                mTvLog.setText("水平方向系数:" + fractionX + "\n垂直方向系数:" + fractionY);
            }
        });
        mRemoteControlView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!mRemoteControlView.isEnableControl()) {
                        ToastUtil.showShort("还未开启遥控模式");
                    }
                }
                return false;
            }
        });
        Switch switcher = (Switch) findViewById(R.id.switcher);
        switcher.setChecked(true);
        mRemoteControlView.enableControl(true);
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mRemoteControlView.enableControl(isChecked);
            }
        });
        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_control_mode);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_gesture) {
                    mRemoteControlView.setWorkMode(RemoteControlView.WORK_MODE_GESTURE);
                } else if (checkedId == R.id.rb_gravity) {
                    if (!mSupportGravity) {
                        ToastUtil.showShort("不支持重力感应");
                    }
                    mRemoteControlView.setWorkMode(RemoteControlView.WORK_MODE_GYROSCOPE);
                }
            }
        });

    }

    private void initGravitySensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            // Success! There's a magnetometer.
            mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            mSupportGravity = true;
        } else {
            mSupportGravity = false;
            ToastUtil.showShort("不支持重力感应");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGravitySensor != null) {
            mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_GRAVITY)
            return;
        if (!mRemoteControlView.isEnableControl()) {
            return;
        }
        if (mRemoteControlView.getWorkMode() == RemoteControlView.WORK_MODE_GYROSCOPE) {
            final float axisX = event.values[0];
            final float axisY = event.values[1];
            float fractionX = axisX / 9.80f;
            float fractionY = axisY / 9.80f;
            if (Math.abs(fractionX) < 0.02) {
                fractionX = 0;
            }
            if (Math.abs(fractionY) < 0.02) {
                fractionY = 0;
            }
            mRemoteControlView.move(-fractionX, fractionY);
            return;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
