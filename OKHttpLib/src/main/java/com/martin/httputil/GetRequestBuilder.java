package com.martin.httputil;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 描述:
 * Author:Martin
 * CREATE:26/06/2016
 */
public class GetRequestBuilder extends RequestBuilder {

    public GetRequestBuilder(Method method) {
        super(method);
    }

    @Override
    public Response execute() throws IOException {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be null before this method execute");
        }
        MediaType type = sign ? JSON : TEXT;
        RequestBody requestBody = RequestBody.create(type, "");
//        FormBody body = new FormBody.Builder().add()

        Request request = new Request.Builder().url(url).method(mMethod.getMethod(), requestBody).build();


        return OKHttpUtil.getDefault().newCall(request).execute();
    }

    @Override
    public Call enqueue(Callback callback) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be null before this method execute");
        }
        if (callback != null && callback instanceof ResponseHandler) {
            ((ResponseHandler) callback).onStart();
        }
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        Call call = OKHttpUtil.getDefault().newCall(request);
        call.enqueue(callback);
        return call;
    }

}
