package com.iwillow.app.android.util;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookUtils {
    private static final String TAG = "HookUtils";

    private HookUtils() {

    }

    static void hookViewOnClick(@NonNull View v) {
        try {
            Method method = v.getClass().getDeclaredMethod("getListenerInfo");
            method.setAccessible(true);
            Object listenerInfo = method.invoke(v);
            Class<?> classListenerInfo = Class.forName("android.view.View$ListenerInfo");
            Field field = classListenerInfo.getDeclaredField("mOnClickListener");
            final View.OnClickListener originOnClickListener = (View.OnClickListener) field.get(listenerInfo);
            Object proxyOnClickListener = Proxy.newProxyInstance(v.getContext().getClass().getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Log.d(TAG, "针对点击事件的HOOK处理");
                    return method.invoke(originOnClickListener, args);
                }
            });
            field.set(listenerInfo, proxyOnClickListener);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
