package com.martin.httputil.builder;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.martin.httputil.OKHttpUtil;
import com.martin.httputil.handler.HttpConfigController;
import com.martin.httputil.util.HttpConstants;
import com.martin.httputil.util.Method;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述: GET请求
 * Author:Martin
 * CREATE:26/06/2016
 */
public class GetRequestBuilder extends RequestBuilder {

    public GetRequestBuilder(Method method) {
        super(method);
    }

    @Override
    public Response execute() throws IOException {
        if (TextUtils.isEmpty(api)) {
            throw new IllegalArgumentException("api can not be null before this method execute");
        }
        return OKHttpUtil.getDefault().newCall(getRequest()).execute();
    }

    private Request getRequest() {
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

    @Override
    public Call enqueue(Callback callback) {
        if (TextUtils.isEmpty(api)) {
            throw new IllegalArgumentException("api can not be null before this method execute");
        }

        if (callback == null) {
            throw new IllegalArgumentException("callback can not be null");
        }

        if (callback != null && callback instanceof BaseResponse) {
            BaseResponse handler = ((BaseResponse) callback);
            HttpConfigController mHandler = OKHttpUtil.getController();
            if (mHandler != null && mHandler.networkCheck() && !cache) { //前置检查
                return null;
            }
            handler.setId(id);
            handler.onStart(id);
            handler.setCache(cache);
        }
        Call call = OKHttpUtil.getDefault().newCall(getRequest());
        call.enqueue(callback);
        return call;
    }

}
