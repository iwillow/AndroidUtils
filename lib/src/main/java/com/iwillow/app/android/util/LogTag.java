package com.iwillow.app.android.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by https://github.com/iwillow/  on 2017/2/9.
 */

class LogTag {
    private final String a;
    private final boolean b;
    private List<String> c = new ArrayList();

    public LogTag(String componentName, String version, boolean banAllTag) {
        this.a = "[" + (componentName == null ? "" : componentName) + "][" + (version == null ? "" : version) + "]";
        this.b = banAllTag;
    }

    public void d(String tag, String log) {
        if (this.a(tag)) {
            Log.d(this.a, "[" + tag + "]" + log);
        }
    }

    private boolean a(String var1) {
        return !this.b && !this.c.contains(var1);
    }

    public void d(String tag, String log, Throwable t) {
        if (this.a(tag)) {
            Log.d(this.a, "[" + tag + "]" + log, t);
        }
    }

    public void i(String tag, String log) {
        Log.i(this.a, "[" + tag + "]" + log);
    }

    public void i(String tag, String log, Throwable t) {
        Log.i(this.a, "[" + tag + "]" + log, t);
    }

    public void w(String tag, String log) {
        Log.w(this.a, "[" + tag + "]" + log);
    }

    public void w(String tag, String log, Throwable t) {
        Log.w(this.a, "[" + tag + "]" + log, t);
    }

    public void e(String tag, String log) {
        Log.e(this.a, "[" + tag + "]" + log);
    }

    public void e(String tag, String log, Throwable t) {
        Log.e(this.a, "[" + tag + "]" + log, t);
    }

    public void banTag(String tag) {
        this.c.add(tag);
    }

    public void unbanTag(String tag) {
        this.c.remove(tag);
    }
}
