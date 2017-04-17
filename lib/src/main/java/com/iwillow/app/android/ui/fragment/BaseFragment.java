package com.iwillow.app.android.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.iwillow.app.android.util.LogExt;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by https://github.com/iwillow/ on 2017/2/11.
 */

public abstract class BaseFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    public abstract String getFragmentTag();


    protected <T extends View> T $(View rootView, @IdRes int resId) {
        return (T) rootView.findViewById(resId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getResource(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    @LayoutRes
    protected abstract int getResource();

    protected abstract void initView(@NonNull View rootView, Bundle savedInstanceState);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        LogExt.d(getFragmentTag(), "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        LogExt.d(getFragmentTag(), "onPermissionsDenied:" + requestCode + ":" + perms.size());
    }
}
