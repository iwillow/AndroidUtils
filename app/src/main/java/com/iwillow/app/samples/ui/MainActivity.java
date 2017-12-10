package com.iwillow.app.samples.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iwillow.app.samples.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_step_view).setOnClickListener(this);
        findViewById(R.id.btn_movie_gallery).setOnClickListener(this);
        findViewById(R.id.btn_color_scale).setOnClickListener(this);
        findViewById(R.id.btn_simple_remote_control).setOnClickListener(this);
        findViewById(R.id.btn_cool_remote_control_a).setOnClickListener(this);
        findViewById(R.id.btn_cool_remote_control_b).setOnClickListener(this);
        findViewById(R.id.btn_jd_center).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_step_view:
                intent = new Intent(v.getContext(), StepActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_movie_gallery:
                intent = new Intent(v.getContext(), MovieGalleryActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_color_scale:
                intent = new Intent(v.getContext(), ColorScaleActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_simple_remote_control:
                intent = new Intent(v.getContext(), SimpleRemoteControlActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_cool_remote_control_a:
                intent = new Intent(v.getContext(), CoolARemoteControlActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_cool_remote_control_b:
                intent = new Intent(v.getContext(), CoolBRemoteControlActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_jd_center:
                intent = new Intent(v.getContext(), CenterItemActivity.class);
                startActivity(intent);
                break;
        }

    }
}
