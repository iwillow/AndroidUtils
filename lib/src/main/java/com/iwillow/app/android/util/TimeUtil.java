package com.iwillow.app.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by https://github.com/iwillow/ on 2017/2/20.
 */

public class TimeUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    private static final Date DATE = new Date();

    private TimeUtil() {
    }

    public static long getTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static String date2Str(Date date) {
        return simpleDateFormat.format(date);
    }

    public static String date2Str(long timeInMillis) {
        return simpleDateFormat.format(new Date(timeInMillis));
    }

    public static Date str2Data(String date) throws ParseException {
        return simpleDateFormat.parse(date);
    }


}
