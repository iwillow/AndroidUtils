package com.iwillow.app.android.http;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by  on 2017/4/17.
 */

public interface HttpRequestInterface {
    String doGet(String url) throws IOException;

    String doGet(String url, Map<String, String> parameters) throws IOException;

    String doPost(String url, Map<String, String> parameters) throws IOException;

    String uploadFile(String url, File file) throws IOException;

    String uploadBytes(String url, String format, byte[] content) throws IOException;

    String postString(String url, String mime, String str) throws IOException;


    public interface HttpRequestLisenter<T> {

        public void onResponse(T response);

        public void onErrorResponse(Throwable throwable);
    }
}
