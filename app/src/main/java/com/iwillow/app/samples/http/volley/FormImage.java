package com.iwillow.app.samples.http.volley;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2017/4/17.
 */

public class FormImage {

    private String mName ;

    private String mFileName ;

    private String mValue ;

    private String mMime ;

    private Bitmap mBitmap ;

    public FormImage(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getName() {
//        return mName;
        return "uploadimg" ;
    }

    public String getFileName() {
        return "test.png";
    }

    public byte[] getValue() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        mBitmap.compress(Bitmap.CompressFormat.JPEG,80,bos) ;
        return bos.toByteArray();
    }

    public String getMime() {
        return "image/png";
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmFileName() {
        return mFileName;
    }

    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }

    public String getmMime() {
        return mMime;
    }

    public void setmMime(String mMime) {
        this.mMime = mMime;
    }
}
