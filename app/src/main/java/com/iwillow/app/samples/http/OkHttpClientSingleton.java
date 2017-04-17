package com.iwillow.app.samples.http;

import com.iwillow.app.android.http.HttpRequestInterface;
import com.iwillow.app.android.util.FileUtil;
import com.iwillow.app.android.util.LogExt;
import com.iwillow.app.android.util.MapUtil;
import com.iwillow.app.android.util.MimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ddx on 2017/4/17.
 */

public class OkHttpClientSingleton implements HttpRequestInterface {

    public static final String TAG = "OkHttpClientSingleton";
    public volatile static OkHttpClientSingleton INSTANCE;

    private OkHttpClient mOkHttpClient;

    private static final int CONNECT_TIME_OUT = 25;

    private OkHttpClientSingleton() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    public static OkHttpClientSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (OkHttpClientSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OkHttpClientSingleton();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public String doGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String rep = response.body().string();
            return rep;
        } else {
            throw new IOException("Unexpected IOException:" + request);
        }
    }

    @Override
    public String doGet(String url, Map<String, String> parameters) throws IOException {
        String realUrl;
        Map<String, String> sortMap = MapUtil.sortMapByKey(parameters);
        if (sortMap != null && sortMap.keySet().size() > 0) {
            StringBuilder sb = new StringBuilder();
            Iterator<Map.Entry<String, String>> entries = sortMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                sb.append("&");
            }
            realUrl = url + "?" + sb.toString().substring(0, sb.lastIndexOf("&"));
        } else {
            realUrl = url;
        }
        Request request = new Request.Builder()
                .url(realUrl)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String rep = response.body().string();
            LogExt.d(TAG, "get response:\n" + rep);
            return rep;
        } else {
            throw new IOException("Unexpected IOException: " + response);
        }
    }


    @Override
    public String doPost(String url, Map<String, String> parameters) throws IOException {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (parameters != null && parameters.keySet().size() > 0) {
            Set<Map.Entry<String, String>> keySet = parameters.entrySet();
            for (Map.Entry<String, String> kvEntry : keySet) {
                formBodyBuilder.add(kvEntry.getKey(), kvEntry.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(formBodyBuilder.build())
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String rep = response.body().string();
            LogExt.d(TAG, "post response:\n" + rep);
            return rep;
        } else {
            throw new IOException("Unexpected IOException: " + response);
        }
    }

    @Override
    public String uploadFile(String url, File file) throws IOException {
        String mime = MimeUtils.getMimeType(FileUtil.getFileNameSuffix(file));
        RequestBody fileBody = RequestBody.create(MediaType.parse(mime), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String rep = response.body().string();
            LogExt.d(TAG, "uploadFile response:\n" + rep);
            return rep;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    @Override
    public String uploadBytes(String url, String format, byte[] content) throws IOException {
        String mime = MimeUtils.getMimeType(format);
        RequestBody fileBody = RequestBody.create(MediaType.parse(mime), content);
        Request request = new Request.Builder()
                .url(url)
                .post(fileBody)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String rep = response.body().string();
            LogExt.d(TAG, "uploadStream response:\n" + rep);
            return rep;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    @Override
    public String postString(String url, String mime, String str) throws IOException {
        RequestBody stringBody = RequestBody.create(MediaType.parse(mime), str);
        Request request = new Request.Builder()
                .url(url)
                .post(stringBody)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String rep = response.body().string();
            LogExt.d(TAG, "postString response:\n" + rep);
            return rep;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            LogExt.d(TAG, String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            LogExt.d(TAG, String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }
}
