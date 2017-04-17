package com.iwillow.app.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * Created by https://github.com/iwillow/ on 2017/2/20.
 */

public class SysUtil {

    private SysUtil() {

    }

    public static String getLanguageEnv() {
        Locale l = Locale.getDefault();
        String language = l.getLanguage();
        if (l.getCountry().toLowerCase().equals("hk")) {
            language = "en";
        }
        return language;
    }

    public static String getLocalEnv() {
        Locale l = Locale.getDefault();
        String country = l.getCountry().toLowerCase();
        return country;
    }

    public static boolean isZh(@NonNull Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language != null && language.endsWith("zh");
    }


    public static String getVersionName(@NonNull Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "error";
        }
        return version;
    }

    public static int getVersionCode(Context context) {
        int versionCode;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionCode = -1;
        }
        return versionCode;
    }

    public static String getPackageName(@NonNull Context context) {
        String packageName;
        try {
            packageName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            packageName = "error";
        }
        return packageName;
    }

    public static boolean isDebugMode(@NonNull Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
