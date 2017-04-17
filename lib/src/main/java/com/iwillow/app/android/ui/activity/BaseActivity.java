package com.iwillow.app.android.ui.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.iwillow.app.android.util.LogExt;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by https://github.com/iwillow/ on 2017/2/11.
 */

public abstract class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    public abstract String getTag();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        initView(savedInstanceState);
    }

    /**
     * @return @NonNull
     */
    protected abstract int getLayoutResID();

    protected abstract void initView(Bundle savedInstanceState);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        LogExt.d(getTag(), "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        LogExt.d(getTag(), "onPermissionsDenied:" + requestCode + ":" + perms.size());
    }


    protected <T extends View> T $(@IdRes int resId) {
        return (T) super.findViewById(resId);
    }

    protected <T extends View> T $(@NonNull View rootView, @IdRes int resId) {
        return (T) rootView.findViewById(resId);
    }
}
