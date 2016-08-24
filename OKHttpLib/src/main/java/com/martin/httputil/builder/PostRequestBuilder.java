package com.martin.httputil.builder;

import android.text.TextUtils;

import com.martin.httputil.handler.HttpConfigController;
import com.martin.httputil.util.HttpConstants;
import com.martin.httputil.util.Method;
import com.martin.httputil.OKHttpUtil;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
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
    public Response execute() throws IOException {
        if (TextUtils.isEmpty(api)) {
            throw new IllegalArgumentException("api can not be null before this method execute");
        }
        Request request = getRequest();
        Call call = OKHttpUtil.getDefault().newCall(request);
        return call.execute();
    }

    private Request getRequest() {
        if (tag == null) tag = api;
        Request.Builder builder = new Request.Builder();
        FormBody requestBody = new FormBody.Builder()
                .add(HttpConstants.SYS_PARAM, sysParams)
                .add(HttpConstants.BIZ_PARAM, duplicateEncode ? URLEncoder.encode(bizParams) : bizParams)
                .build();
        return builder.post(requestBody).url(HttpConstants.BASE_URL).tag(tag).build();
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
