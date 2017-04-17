package com.iwillow.app.samples.http;

import com.iwillow.app.android.http.HttpRequestInterface;

/**
 * Created by https://github.com/iwillow on 2017/4/17.
 */

public class HttpRequestFactory {

    public static HttpRequestInterface getHttpClient() {
        return OkHttpClientSingleton.getInstance();
    }


}
