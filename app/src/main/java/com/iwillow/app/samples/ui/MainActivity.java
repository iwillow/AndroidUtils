package com.iwillow.app.samples.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.iwillow.app.android.ui.view.HorizontalStepView;
import com.iwillow.app.android.ui.view.VerticalStepView;
import com.iwillow.app.samples.R;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HorizontalStepView horizontalStepView = (HorizontalStepView) findViewById(R.id.horizontalStepView);
        List<String> data = Arrays.asList(getResources().getStringArray(R.array.step_names));
        horizontalStepView.setItems(data);
        horizontalStepView.setCurrentStep(0);
        VerticalStepView verticalStepView = (VerticalStepView) findViewById(R.id.verticalStepView);
        verticalStepView.setStepItemsListener(new VerticalStepView.StepItemsListener() {
            @Override
            public void onStartLoadItems(VerticalStepView stepView) {
                List<String> data = Arrays.asList(getResources().getStringArray(R.array.i_have_a_dream));
                stepView.setItems(data);
                stepView.finishStep(5);
            }
        });

    }


}
