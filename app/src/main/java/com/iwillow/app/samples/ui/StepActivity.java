package com.iwillow.app.samples.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iwillow.app.android.ui.view.HorizontalStepView;
import com.iwillow.app.android.ui.view.VerticalStepView;
import com.iwillow.app.android.util.ToastUtil;
import com.iwillow.app.samples.R;

import java.util.Arrays;
import java.util.List;

public class StepActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        final HorizontalStepView horizontalStepView = (HorizontalStepView) findViewById(R.id.horizontalStepView);
        List<String> data = Arrays.asList(getResources().getStringArray(R.array.step_names));
        horizontalStepView.setItems(data);
        horizontalStepView.finishStep(0);
        findViewById(R.id.btn_horizontal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!horizontalStepView.isFinished()){
                    int next= horizontalStepView.getCurrentFinishedStep()+1;
                    horizontalStepView.finishStep(next);
                }else{
                    ToastUtil.showShort("流程已结束");
                }
            }
        });


       final VerticalStepView verticalStepView = (VerticalStepView) findViewById(R.id.verticalStepView);
        verticalStepView.setStepItemsListener(new VerticalStepView.StepItemsListener() {
            @Override
            public void onStartLoadItems(VerticalStepView stepView) {
                List<String> data = Arrays.asList(getResources().getStringArray(R.array.i_have_a_dream));
                stepView.setItems(data);
                stepView.finishStep(0);
            }
        });

        findViewById(R.id.btn_vertical).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if(!verticalStepView.isFinished()){
                     int next= verticalStepView.getCurrentFinishedStep()+1;
                      verticalStepView.finishStep(next);
                  }else{
                      ToastUtil.showShort("流程已结束");
                  }

            }
        });
    }


}
