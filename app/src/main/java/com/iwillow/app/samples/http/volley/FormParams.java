package com.iwillow.app.samples.http.volley;
//表单数据格式
//Connection: keep-alive
//Content-Length: 123
//X-Requested-With: ShockwaveFlash/16.0.0.296
//User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36
//Content-Type: multipart/form-data; boundary=----------Ij5ei4KM7KM7ae0KM7cH2ae0Ij5Ef1
//Accept: */*
//Accept-Encoding: gzip, deflate
//Accept-Language: zh-CN,zh;q=0.8
//Cookie: bdshare_firstime=1409052493497
//
//------------Ij5ei4KM7KM7ae0KM7cH2ae0Ij5Ef1
//Content-Disposition: form-data; name="position"
//
//1425264476444
//------------Ij5ei4KM7KM7ae0KM7cH2ae0Ij5Ef1
//Content-Disposition: form-data; name="editorIndex"
//
//ue_con_1425264252856
//------------Ij5ei4KM7KM7ae0KM7cH2ae0Ij5Ef1
//Content-Disposition: form-data; name="cm"
//
//100672
//------------Ij5ei4KM7KM7ae0KM7cH2ae0Ij5Ef1--
/**
 * Created by Administrator on 2017/4/17.
 */

public class FormParams {
    /*参数的名称*/
    private String mName ;
    /*参数的值*/
    private String mValue ;

    public FormParams(String mName, String mValue) {
        this.mName = mName;
        this.mValue = mValue;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }


}
