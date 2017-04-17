package com.iwillow.app.samples.http.volley;

import com.android.volley.Response;

/**
 * Created by Administrator on 2017/4/17.
 */

public interface ResponseListener <T> extends Response.ErrorListener,
        Response.Listener<T> {

}
