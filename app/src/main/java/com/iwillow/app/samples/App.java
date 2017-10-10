package com.iwillow.app.samples;

import android.app.Application;

import com.iwillow.app.android.util.ToastUtil;

/**
 * Created by iwillow on 2017/9/10.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.install(this);
    }
}
