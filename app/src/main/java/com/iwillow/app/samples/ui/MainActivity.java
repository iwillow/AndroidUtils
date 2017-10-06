package com.iwillow.app.samples.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iwillow.app.android.ui.view.HorizontalStepView;
import com.iwillow.app.android.ui.view.StepView;
import com.iwillow.app.android.ui.view.VerticalStepView;
import com.iwillow.app.samples.R;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HorizontalStepView horizontalStepView = (HorizontalStepView) findViewById(R.id.horizontalStepView);
        List<String> data = Arrays.asList(getResources().getStringArray(R.array.step_names));
        horizontalStepView.setItems(data);
        horizontalStepView.setCurrentStep(0);
        VerticalStepView staticLayoutView = (VerticalStepView) findViewById(R.id.staticLayoutView);
        data = Arrays.asList(getResources().getStringArray(R.array.step_names_2));
        staticLayoutView.setItems(data, 500);
        staticLayoutView.finishStep(0);
    }


}
