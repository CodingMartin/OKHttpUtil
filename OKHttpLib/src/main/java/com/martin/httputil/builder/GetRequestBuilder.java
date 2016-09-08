package com.martin.httputil.builder;

import android.net.Uri;
import android.util.Log;

import com.martin.httputil.annotation.Method;
import com.martin.httputil.util.HttpConstants;

import java.net.URLEncoder;

import okhttp3.Request;

/**
 * 描述: GET请求
 * Author:Martin
 * CREATE:26/06/2016
 */
public class GetRequestBuilder extends RequestBuilder {

    public GetRequestBuilder(@Method String method) {
        super(method);
    }

    @Override
    protected Request getRequest() {
        if (tag == null) this.tag = api;
        StringBuilder builder = new StringBuilder(HttpConstants.BASE_URL);
        if (sysParams != null || bizParams != null)
            builder.append("?");
        if (sysParams != null)
            builder.append(HttpConstants.SYS_PARAM).append("=").append(URLEncoder.encode(sysParams));
        if (sysParams != null && bizParams != null) {
            builder.append("&").append(HttpConstants.BIZ_PARAM).append("=");
            String encode = URLEncoder.encode(bizParams);
            if (duplicateEncode) { //2次编码
                encode = Uri.encode(encode);
            }
            builder.append(encode);
        }
        String url = builder.toString();
        if (HttpConstants.DEBUG) {
            Log.d(HttpConstants.TAG, url);
        }
        Request request = new Request.Builder()
                .get()
                .url(url)
                .tag(tag)
                .build();
        return request;
    }
}
