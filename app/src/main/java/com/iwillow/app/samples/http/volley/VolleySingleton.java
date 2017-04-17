package com.iwillow.app.samples.http.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by https://github.com/iwillow on 2017/4/17.
 */

public class VolleySingleton {

    private static VolleySingleton mInstance;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req);
    }


    public void cancelRequest(Object tag) {
        getRequestQueue().cancelAll(tag);
    }


    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    /**
     * @param url
     * @param listener
     */
    public <T> void doGet(String url, ResponseListener<T> listener) {

        Request<T> request = new GetObjectRequest<T>(url, new TypeToken<T>() {
        }.getType(), listener);

        addToRequestQueue(request);

    }

    /**
     * @param url
     * @param param
     * @param listener
     */
    public <T> void doPost(String url, HashMap<String, String> param,
                           ResponseListener<T> listener) {

        Request<T> request = new PostObjectRequest<T>(url, param,
                new TypeToken<T>() {
                }.getType(), listener);

        addToRequestQueue(request);

    }

    public <T> void doGetWithCache(String url,
                                   ResponseListener<T> listener) {

        Request<T> request = new GetObjectRequest<T>(url, new TypeToken<T>() {
        }.getType(), listener);
        request.setShouldCache(true);
        // request.setCacheTime(10 * 60);
        addToRequestQueue(request);

    }

    public <T> void doFormPost(String url, List<FormParams> listItem,
                               Type type, FormObjectRequest.DataResponseListener<T> listener) {
        Request<T> request = new FormObjectRequest<T>(url, listItem, type,
                listener);
        addToRequestQueue(request);

    }

    public void getXml(String url,
                       ResponseListener<XmlPullParser> listener) {
        Request<XmlPullParser> request = new XmlRequest(url, listener);
        addToRequestQueue(request);
    }

    public void upLoadImage(String url, List<FormImage> listItem,
                            ResponseListener<String> listener) {
        PostUploadRequest request = new PostUploadRequest(url, listItem,
                listener);
        addToRequestQueue(request);
    }


}
