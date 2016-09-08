package com.martin.httputil.builder;

import android.text.TextUtils;

import com.martin.httputil.HTTP;
import com.martin.httputil.annotation.Method;
import com.martin.httputil.handler.DataCallback;
import com.martin.httputil.handler.HttpConfigController;
import com.martin.httputil.handler.ResponseHandle;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述:
 * Author:Martin
 * CREATE:26/06/2016
 */
public abstract class RequestBuilder {
    protected String api;
    protected Object tag;
    protected String mMethod;
    protected String sysParams;
    protected String bizParams;
    protected boolean duplicateEncode;
    protected File cacheFileDir;
    //请求的id 用来区分不同的请求,便于处理(可选)
    protected int id = 1024;

    protected static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    protected static final MediaType TEXT
            = MediaType.parse("text/plain; charset=utf-8");
    protected static final MediaType ENCODE = MediaType.parse("application/x-www-form-urlencoded");

    public RequestBuilder(@Method String method) {
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


    public RequestBuilder id(int id) {
        this.id = id;
        return this;
    }

    public Response execute() throws IOException {
        if (TextUtils.isEmpty(api)) {
            throw new IllegalArgumentException("api can not be null before this method execute");
        }
        Request request = getRequest();
        Call call = HTTP.getDefault().newCall(request);
        return call.execute();
    }

    public Call enqueue(Callback callback) {
        if (TextUtils.isEmpty(api)) {
            throw new IllegalArgumentException("api can not be null before this method execute");
        }

        if (callback == null) {
            throw new IllegalArgumentException("callback can not be null");
        }

        if (callback instanceof BaseResponse) {
            BaseResponse handler = ((BaseResponse) callback);
            HttpConfigController mHandler = HTTP.getController();
            if (mHandler != null && mHandler.networkCheck()) { //前置检查
                return null;
            }
            handler.setId(id);
            handler.onStart(id);
        }

        Call call = HTTP.getDefault().newCall(getRequest());
        call.enqueue(callback);
        return call;
    }

    public void request(DataCallback callback) {
        if (callback == null) return;
        ResponseHandle handle = new ResponseHandle(callback);
        enqueue(handle);
    }

    protected abstract Request getRequest();

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
