package com.iwillow.app.android.util;

/**
 * Created by https://github.com/iwillow/ on 2017/2/9.
 */

public class LogExt {

    private static LogTag a;
    private static String b;
    private static String c;
    private static boolean d;

    private LogExt() {
    }

    public static void init(String componentName, String version, boolean banAllTag) {
        b = componentName;
        c = version;
        d = banAllTag;
    }

    public static void d(String tag, String log) {
        a(b, c, d).d(tag, log);
    }

    private static LogTag a(String var0, String var1, boolean var2) {
        Class var3 = LogExt.class;
        synchronized (LogExt.class) {
            if (a == null) {
                a = new LogTag(var0, var1, var2);
            }
        }

        return a;
    }

    public static void d(String tag, String log, Throwable t) {
        a(b, c, d).d(tag, log, t);
    }

    public static void i(String tag, String log) {
        a(b, c, d).i(tag, log);
    }

    public static void i(String tag, String log, Throwable t) {
        a(b, c, d).i(tag, log, t);
    }

    public static void w(String tag, String log) {
        a(b, c, d).w(tag, log);
    }

    public static void w(String tag, String log, Throwable t) {
        a(b, c, d).w(tag, log, t);
    }

    public static void e(String tag, String log) {
        a(b, c, d).e(tag, log);
    }

    public static void e(String tag, String log, Throwable t) {
        a(b, c, d).e(tag, log, t);
    }

    public static void banTag(String tag) {
        a(b, c, d).banTag(tag);
    }

    public static void unbanTag(String tag) {
        a(b, c, d).unbanTag(tag);
    }
}
