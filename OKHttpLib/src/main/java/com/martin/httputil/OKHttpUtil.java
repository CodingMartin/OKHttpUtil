package com.martin.httputil;

import android.content.Context;

import com.martin.httputil.builder.GetRequestBuilder;
import com.martin.httputil.builder.OSSUploadBuilder;
import com.martin.httputil.builder.PostRequestBuilder;
import com.martin.httputil.builder.PutRequestBuilder;
import com.martin.httputil.handler.HttpConfigController;
import com.martin.httputil.handler.Platform;
import com.martin.httputil.log.LoggerInterceptor;
import com.martin.httputil.util.HttpConstants;
import com.martin.httputil.util.Method;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * 描述:
 * Author:Martin
 * CREATE:26/06/2016
 */
public class OKHttpUtil {
    public static final long TIMEOUT = 30 * 1000L;
    public static final long READ_TIMEOUT = 30 * 1000L;
    public static final long WRITE_TIMEOUT = 30 * 1000L;
    private static OkHttpClient sOkHttpClient;
    private static Platform mPlatform;
    private static HttpConfigController mController;

    public static Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public static OkHttpClient getDefault() {
        if (sOkHttpClient == null) {
            synchronized (OKHttpUtil.class) {
                if (sOkHttpClient == null) {
                    sOkHttpClient = getOkHttpClient();
                    mPlatform = Platform.get();
                }
            }
        }
        return sOkHttpClient;
    }

    /**
     * 请求成功了之后可能要根据结果进行一些统一的处理
     */
    public static void initConfigController(HttpConfigController controller) {
        mController = controller;
    }

    public static HttpConfigController getController() {
        return mController;
    }

    private static OkHttpClient getOkHttpClient() {
        //LOG打印
        LoggerInterceptor loggerInterceptor = new LoggerInterceptor(HttpConstants.TAG, false);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(true);
        builder.addInterceptor(loggerInterceptor);//增加LOG打印

        builder.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(Runtime.getRuntime().availableProcessors() * 2);
        builder.dispatcher(dispatcher);

        return builder.build();
    }

    public static GetRequestBuilder get() {
        return new GetRequestBuilder(Method.GET);
    }

    public static PostRequestBuilder post() {
        return new PostRequestBuilder(Method.POST);
    }

    public static PutRequestBuilder put() {
        return new PutRequestBuilder(Method.PUT);
    }

    public static OSSUploadBuilder upload(Context context) {
        return new OSSUploadBuilder(context);
    }

    public void cancle(Object tag) {
        if (tag == null) return;
        for (Call call : sOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                if (!call.isCanceled()) call.cancel();
            }
        }

        for (Call call : sOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                if (!call.isCanceled()) call.cancel();
            }
        }
    }

    public void cancelAll() {
        sOkHttpClient.dispatcher().cancelAll();
    }
}
