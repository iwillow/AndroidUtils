package com.iwillow.app.samples.http;

import com.iwillow.app.android.http.HttpRequestInterface;

/**
 * Created by ddx on 2017/4/17.
 */

public class HttpRequestFactory {

    public static HttpRequestInterface getHttpClient() {
        return OkHttpClientSingleton.getInstance();
    }


}
