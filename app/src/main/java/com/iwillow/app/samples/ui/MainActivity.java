package com.iwillow.app.samples.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iwillow.app.android.ui.view.StepView;
import com.iwillow.app.samples.R;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private StepView mStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStepView = (StepView) findViewById(R.id.stepView);
        List<String> data = Arrays.asList(getResources().getStringArray(R.array.step_names));
        mStepView.setItems(data);
        mStepView.setCurrentStep(2);
    }
}
