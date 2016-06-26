package com.martin.httputil;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 描述:
 * Author:Martin
 * CREATE:26/06/2016
 */
public class PostRequestBuilder extends RequestBuilder {

    public PostRequestBuilder(Method method) {
        super(method);
    }

    @Override
    public Response execute() {
        return null;
    }

    @Override
    public Call enqueue(Callback callback) {
        return null;
    }
}
