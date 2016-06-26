package com.martin.httputil;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * 描述:
 * Author:Martin
 * CREATE:26/06/2016
 */
public abstract class RequestBuilder {
    protected String url;
    protected String jsonRequestParam;
    protected TreeMap<String, Object> mParams;
    protected Object tag;
    protected Method mMethod;
    protected boolean sign;
    protected static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    protected static final MediaType TEXT
            = MediaType.parse("text/plain; charset=utf-8");

    public RequestBuilder(Method method) {
        this.mMethod = method;
    }

    public ParamsBuilder url(String url) {
        this.url = url;
        return new ParamsBuilder(this);
    }

    public RequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    void setJsonRequestParam(String jsonRequestParam) {
        this.jsonRequestParam = jsonRequestParam;
    }

    void setParams(TreeMap<String, Object> params) {
        this.mParams = params;
    }

    void setSign(boolean sign) {
        this.sign = sign;
    }

    public abstract Response execute() throws IOException;

    public abstract Call enqueue(Callback callback);

}
