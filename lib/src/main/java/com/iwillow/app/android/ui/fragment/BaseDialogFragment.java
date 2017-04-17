package com.iwillow.app.android.ui.fragment;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.iwillow.app.android.util.LogExt;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by https://github.com/iwillow/ on 2017/2/18.
 */

public abstract class BaseDialogFragment extends DialogFragment implements EasyPermissions.PermissionCallbacks {

    public abstract String getFragmentTag();


    protected <T extends View> T $(View rootView, @IdRes int resId) {
        return (T) rootView.findViewById(resId);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
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
