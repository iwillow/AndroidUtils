package com.iwillow.app.samples.http.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by https://github.com/iwillow on 2017/4/17.
 */

public class XmlRequest extends Request<XmlPullParser> {
    private final ResponseListener<XmlPullParser> mListener;

    public XmlRequest(String url, ResponseListener<XmlPullParser> listener) {
        super(Method.GET, url, listener);
        this.mListener = listener;
    }

    @Override
    protected Response<XmlPullParser> parseNetworkResponse(
            NetworkResponse response) {
        try {
            String xmlString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlString));
            return Response.success(xmlPullParser,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));

        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(XmlPullParser response) {
        // TODO Auto-generated method stub
        mListener.onResponse(response);
    }
}
