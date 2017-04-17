package com.iwillow.app.android.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by https://github.com/iwillow/ on 2017/2/11.
 */

public class PermissionsUtil {

    public static final int PERMISSION_FOR_READ_CALENDAR = 1000;
    public static final int PERMISSION_FOR_WRITE_CALENDAR = PERMISSION_FOR_READ_CALENDAR + 1;

    public static final int PERMISSION_FOR_CAMERA = PERMISSION_FOR_WRITE_CALENDAR + 1;


    public static final int PERMISSION_FOR_READ_CONTACTS = PERMISSION_FOR_CAMERA + 1;
    public static final int PERMISSION_FOR_WRITE_CONTACTS = PERMISSION_FOR_READ_CONTACTS + 1;
    public static final int PERMISSION_FOR_GET_ACCOUNTS = PERMISSION_FOR_WRITE_CONTACTS + 1;


    public static final int PERMISSION_FOR_ACCESS_FINE_LOCATION = PERMISSION_FOR_GET_ACCOUNTS + 1;
    public static final int PERMISSION_FOR_ACCESS_COARSE_LOCATION = PERMISSION_FOR_ACCESS_FINE_LOCATION + 1;

    public static final int PERMISSION_FOR_RECORD_AUDIO = PERMISSION_FOR_ACCESS_COARSE_LOCATION + 1;

    public static final int PERMISSION_FOR_READ_PHONE_STATE = PERMISSION_FOR_RECORD_AUDIO + 1;
    public static final int PERMISSION_FOR_CALL_PHONE = PERMISSION_FOR_READ_PHONE_STATE + 1;
    public static final int PERMISSION_FOR_READ_CALL_LOG = PERMISSION_FOR_CALL_PHONE + 1;
    public static final int PERMISSION_FOR_WRITE_CALL_LOG = PERMISSION_FOR_READ_CALL_LOG + 1;
    public static final int PERMISSION_FOR_ADD_VOICEMAIL = PERMISSION_FOR_WRITE_CALL_LOG + 1;
    public static final int PERMISSION_FOR_USE_SIP = PERMISSION_FOR_ADD_VOICEMAIL + 1;
    public static final int PERMISSION_FOR_PROCESS_OUTGOING_CALLS = PERMISSION_FOR_USE_SIP + 1;

    public static final int PERMISSION_FOR_BODY_SENSORS = PERMISSION_FOR_PROCESS_OUTGOING_CALLS + 1;


    public static final int PERMISSION_FOR_SEND_SMS = PERMISSION_FOR_BODY_SENSORS + 1;
    public static final int PERMISSION_FOR_RECEIVE_SMS = PERMISSION_FOR_SEND_SMS + 1;
    public static final int PERMISSION_FOR_READ_SMS = PERMISSION_FOR_RECEIVE_SMS + 1;
    public static final int PERMISSION_FOR_RECEIVE_WAP_PUSH = PERMISSION_FOR_READ_SMS + 1;
    public static final int PERMISSION_FOR_RECEIVE_MMS = PERMISSION_FOR_RECEIVE_WAP_PUSH + 1;


    public static final int PERMISSION_FOR_READ_EXTERNAL_STORAGE = PERMISSION_FOR_RECEIVE_MMS + 1;
    public static final int PERMISSION_FOR_WRITE_EXTERNAL_STORAGE = PERMISSION_FOR_READ_EXTERNAL_STORAGE + 1;


    public static String[] COMMON_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    public static String[] LOCATION_PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };


    public static boolean hasPermissions(Context context, String... permissions) {
        return EasyPermissions.hasPermissions(context, permissions);
    }


    public static void requestPermissionsForFragment(@NonNull Fragment fragment,
                                                     @NonNull String rationale,
                                                     int requestCode,
                                                     @NonNull String... perms) {
        EasyPermissions.requestPermissions(fragment, rationale, requestCode, perms);
    }

    public static void requestPermissionsForActivity(@NonNull Activity activity,
                                                     @NonNull String rationale,
                                                     int requestCode,
                                                     @NonNull String... perms) {
        EasyPermissions.requestPermissions(activity, rationale, requestCode, perms);
    }
}
