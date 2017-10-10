package com.iwillow.app.samples.ui;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.iwillow.app.android.ui.view.ColorScaleView;
import com.iwillow.app.android.util.ActivityWeakHandler;
import com.iwillow.app.samples.R;


public class ColorScaleActivity extends AppCompatActivity implements ActivityWeakHandler.MessageListener {
    private SoundPool mSoundPool;
    private static ActivityWeakHandler<ColorScaleActivity> sHandler;
    public static final int MSG_ON_PLAY_SOUND = 0x111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        mSoundPool.load(this, R.raw.dada, 1);
        sHandler = new ActivityWeakHandler<>(this);
        setContentView(R.layout.activity_color_scale);
        final View bg = findViewById(R.id.view_bg);
        final ColorScaleView colorScaleView = (ColorScaleView) findViewById(R.id.colorScaleView);
        colorScaleView.setOnColorSelectedListener(new ColorScaleView.OnColorSelectedListener() {
            @Override
            public void onColorChanged(int index, @ColorInt int color) {
                bg.setBackgroundColor(color);
                if (sHandler != null) {
                    sHandler.sendEmptyMessage(MSG_ON_PLAY_SOUND);
                }
            }

            @Override
            public void onColorSelected(int index, @ColorInt int color) {
               /* if (sHandler != null) {
                    sHandler.sendEmptyMessage(MSG_ON_PLAY_SOUND);
                }*/
            }
        });
        final Switch switcher = (Switch) findViewById(R.id.switcher);
        switcher.setChecked(true);
        colorScaleView.enableControl(true);
        bg.setBackgroundColor(colorScaleView.getColor(0));
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                colorScaleView.enableControl(isChecked);
            }
        });
        sHandler.sendEmptyMessage(MSG_ON_PLAY_SOUND);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sHandler != null) {
            sHandler.removeCallbacksAndMessages(null);
            sHandler.clear();
            sHandler = null;
        }
        if (mSoundPool != null) {
            mSoundPool.release();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_ON_PLAY_SOUND:
                if (mSoundPool != null) {
                    mSoundPool.play(1, 1, 1, 0, 0, 1);
                }
                break;
        }

    }
}
