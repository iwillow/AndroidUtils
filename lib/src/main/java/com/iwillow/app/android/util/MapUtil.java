package com.iwillow.app.android.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ddx on 2017/4/17.
 */

public class MapUtil {


    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String str1, String str2) {
                        return str1.compareTo(str2);
                    }
                });
        sortMap.putAll(map);

        return sortMap;
    }
}
