package com.iwillow.app.samples.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.iwillow.app.samples.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_step_view).setOnClickListener(this);
        findViewById(R.id.btn_movie_gallery).setOnClickListener(this);
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
        }

    }
}
