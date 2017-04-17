package com.iwillow.app.android.util;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;


/**
 * Created by https://github.com/iwillow/ on 2017/2/9.
 */

public class ToastUtil {
    private static Application sApp;

    private ToastUtil() {

    }

    public static void install(Application application) {
        sApp = application;
    }

    public static void showShort(@NonNull Context context, CharSequence msg) {
        if (context != null && !TextUtils.isEmpty(msg)) {
            Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

    }

    public static void showShort(@NonNull CharSequence res) {
        showShort(sApp, res);
    }

    public static void showShort(@StringRes int res) {
        showShort(sApp, res);
    }

    public static void showShort(@NonNull Context context, @StringRes int res) {
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), res, Toast.LENGTH_SHORT).show();
        }

    }

    public static void showLong(@NonNull Context context, CharSequence msg) {
        if (context != null && !TextUtils.isEmpty(msg)) {
            Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }

    }

    public static void showLong(Context context, @StringRes int res) {
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), res, Toast.LENGTH_LONG).show();
        }
    }

    public static void showLong(@StringRes int res) {
        showLong(sApp, res);
    }

    public static void showLong(CharSequence res) {
        showLong(sApp, res);
    }


}
