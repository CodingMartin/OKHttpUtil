package com.martin.httputil.builder;

import com.martin.httputil.OKHttpUtil;
import com.martin.httputil.handler.HttpConfigController;
import com.martin.httputil.util.Method;

import java.io.File;
import java.io.IOException;

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
    protected String api;
    protected Object tag;
    protected Method mMethod;
    protected String sysParams;
    protected String bizParams;
    protected boolean duplicateEncode;
    protected boolean cache;
    protected File cacheFileDir;
    //请求的id 用来区分不同的请求,便于处理(可选)
    protected int id = 1024;

    protected static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    protected static final MediaType TEXT
            = MediaType.parse("text/plain; charset=utf-8");
    protected static final MediaType ENCODE = MediaType.parse("application/x-www-form-urlencoded");

    public RequestBuilder(Method method) {
        this.mMethod = method;
    }

    public ParamsBuilder api(String api) {
        this.api = api;
        return new ParamsBuilder(this, api);
    }

    public RequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Deprecated
    public RequestBuilder cache() {
        this.cache = true;
        HttpConfigController mController = OKHttpUtil.getController();
        if (mController != null) cacheFileDir = mController.cacheDir();
        return this;
    }

    public RequestBuilder id(int id) {
        this.id = id;
        return this;
    }

    public abstract Response execute() throws IOException;

    public abstract Call enqueue(Callback callback);

    void setBizParams(String bizParams) {
        this.bizParams = bizParams;
    }

    void setSysParams(String sysParams) {
        this.sysParams = sysParams;
    }

    public RequestBuilder encode() {
        duplicateEncode = true;
        return this;
    }
}
