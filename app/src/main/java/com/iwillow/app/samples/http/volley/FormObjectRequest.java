package com.iwillow.app.samples.http.volley;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by administrator on 2017/4/17.
 */

public class FormObjectRequest<T> extends Request<T> {

    /**
     * 正确处理数据回调时使用
     */
    private final DataResponseListener<T> mListener;
    /**
     * 解析 json
     */
    private Gson mGson;
    /**
     * 数据分割线
     */
    private String BOUNDARY = "---------8888888888888";
    /**
     * 数据类型
     */
    private String MULTIPART_FORM_DATA = "multipart/form-data";
    /**
     * 在用 gson 解析 json 数据的时候，需要用到这个参数
     */
    private Type mClazz;

    /**
     * post方式需要传入的表单数据
     */
    List<FormParams> mListItem;

    public FormObjectRequest(String url, List<FormParams> listItem, Type type,
                             DataResponseListener<T> listener) {
        super(Method.POST, url, listener);
        this.mListener = listener;

		/*
         * Gson 过滤字段 参见 http://blog.csdn.net/jxxfzgy/article/details/43746317
		 */
        mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create();
        mClazz = type;
        mListItem = listItem;
        setShouldCache(false);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        // TODO Auto-generated method stub
        try {
            T result;// 实体类
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));// 获取到的字符串
            Log.v("zgy", "====SearchResult===" + jsonString);
            result = mGson.fromJson(jsonString, mClazz);// 将字符串转换成指定类型的实体类
            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    /*
     * 回调解析之后的数据
     */
    @Override
    protected void deliverResponse(T response) {
        // TODO Auto-generated method stub
        mListener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        // TODO Auto-generated method stub
        return MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        // TODO Auto-generated method stub
        if (mListItem == null || mListItem.size() == 0) {
            return super.getBody();
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int N = mListItem.size();
        FormParams formParams;
        for (int i = 0; i < N; i++) {
            formParams = mListItem.get(i);
            StringBuffer sb = new StringBuffer();
			/* 第一行 */
            sb.append("--" + BOUNDARY);
            sb.append("\r\n");
			/* 第二行 */
            sb.append("Content-Disposition: form-data;");
            sb.append(" name=\"");
            sb.append(formParams.getName());
            sb.append("\"");
            sb.append("\r\n");
			/* 第三行 */
            sb.append("\r\n");
			/* 第四行 */
            sb.append(formParams.getValue());
            sb.append("\r\n");
            try {
                bos.write(sb.toString().getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		/* 结尾行 */
        String endLine = "--" + BOUNDARY + "--" + "\r\n";
        try {
            bos.write(endLine.toString().getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mListener.postData(bos.toString());
            }
        });
        Log.v("zgy", "=====formText====\n" + bos.toString());
        return bos.toByteArray();
    }

    public interface DataResponseListener<T> extends ResponseListener<T> {
        public void postData(String data);
    }
}
