package com.iwillow.app.android.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by ddx on 2017/2/27.
 */

public class ActivityWeakHandler<T extends Activity & ActivityWeakHandler.MessageListener> extends Handler {
    private WeakReference<T> mActivityReference;

    public ActivityWeakHandler(T activity) {
        mActivityReference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        final T owner = getOwner();
        if (owner != null) {
            owner.handleMessage(msg);
        }
    }

    public T getOwner() {
        return this.mActivityReference.get();
    }

    public void clear() {
        mActivityReference.clear();
    }


    public static interface MessageListener {
        public void handleMessage(Message msg);
    }
}
