package com.martin.httputil.builder;

import com.martin.httputil.annotation.Method;
import com.martin.httputil.util.HttpConstants;

import java.net.URLEncoder;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * 描述:
 * Author:Martin
 * CREATE:26/06/2016
 */
public class PostRequestBuilder extends RequestBuilder {

    public PostRequestBuilder(@Method String method) {
        super(method);
    }

    protected Request getRequest() {
        if (tag == null) tag = api;
        Request.Builder builder = new Request.Builder();
        FormBody requestBody = new FormBody.Builder()
                .add(HttpConstants.SYS_PARAM, sysParams)
                .add(HttpConstants.BIZ_PARAM, duplicateEncode ? URLEncoder.encode(bizParams) : bizParams)
                .build();
        return builder.post(requestBody).url(HttpConstants.BASE_URL).tag(tag).build();
    }
}
