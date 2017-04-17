package com.iwillow.app.samples.http.volley;

/**
 * Created by Administrator on 2017/4/17.
 */

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @param <T>
 * @author gyzhong
 * @date 2015-03-01
 * @description 通过post方式获取指定类型的数据
 */
public class PostObjectRequest<T> extends Request<T> {

    /**
     * 正确处理数据回调时使用
     */
    private final ResponseListener<T> mListener;
    /**
     * 解析 json
     */
    private Gson mGson;

    /**
     * 在用 gson 解析 json 数据的时候，需要用到这个参数
     */
    private Type mClazz;

    /**
     * post方式需要传入的值
     */
    private Map<String, String> mParams;

    public PostObjectRequest(String url, Map<String, String> params, Type type,
                             ResponseListener<T> listener) {
        super(Method.POST, url, listener);
        this.mListener = listener;
        /*
         * Gson 过滤字段 参见 http://blog.csdn.net/jxxfzgy/article/details/43746317
		 */
        mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .create();
        mClazz = type;
        mParams = params;
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

    //关键代码就在这里，在 Volley 的网络操作中，
    //如果判断请求方式为 Post 则会通过此方法来获取 param，
    //所以在这里返回我们需要的参数
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        // TODO Auto-generated method stub
        return mParams;
    }
}
