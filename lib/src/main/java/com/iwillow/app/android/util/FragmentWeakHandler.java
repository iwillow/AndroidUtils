package com.iwillow.app.android.util;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Created by ddx on 2017/3/1.
 */

public class FragmentWeakHandler<T extends Fragment & FragmentWeakHandler.MessageListener> extends Handler {
    private WeakReference<T> mFragmentReference;

    public FragmentWeakHandler(T fragment) {
        mFragmentReference = new WeakReference<>(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        final T owner = getOwner();
        if (owner != null) {
            owner.handleMessage(msg);
        }
        super.handleMessage(msg);
    }

    public T getOwner() {
        return this.mFragmentReference.get();
    }

    public void clear() {
        mFragmentReference.clear();
    }

    public static interface MessageListener {
        public void handleMessage(Message msg);
    }
}
