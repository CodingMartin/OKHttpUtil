package com.martin.httputil;

import android.content.Context;

import com.martin.httputil.builder.GetRequestBuilder;
import com.martin.httputil.builder.OSSUploadBuilder;
import com.martin.httputil.builder.PostRequestBuilder;
import com.martin.httputil.builder.PutRequestBuilder;
import com.martin.httputil.handler.HttpConfigController;
import com.martin.httputil.util.Platform;
import com.martin.httputil.log.LoggerInterceptor;
import com.martin.httputil.util.HttpConstants;

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
public class HTTP {
    public static final long TIMEOUT = 30 * 1000L;
    public static final long READ_TIMEOUT = 30 * 1000L;
    public static final long WRITE_TIMEOUT = 30 * 1000L;
    private static HTTP sHTTP;
    private OkHttpClient sOkHttpClient;
    private Platform mPlatform;
    private HttpConfigController mController;

    private HTTP() {
        sOkHttpClient = get_p_OkHttpClient();
        mPlatform = Platform.get();
    }

    /***********************************************
     * 外部访问方法
     ***********************************************/
    /**
     * 获取统一处理器
     */
    public static HttpConfigController getController() {
        return instance().get_p_Controller();
    }

    /**
     * 请求成功了之后可能要根据结果进行一些统一的处理
     */
    public static void setConfigController(HttpConfigController controller) {
        instance().p_setConfigController(controller);
    }

    /**
     * 获取一个处理异步的Handler
     *
     * @return
     */
    public static Executor getDelivery() {
        return instance().get_p_Delivery();
    }

    /**
     * 获取一个{@link #sOkHttpClient}
     *
     * @return
     */
    public static OkHttpClient getDefault() {
        return instance().get_p_Default();
    }

    /**
     * GET 请求构造
     *
     * @return
     */
    public static GetRequestBuilder get() {
        return new GetRequestBuilder(HttpConstants.GET);
    }

    /**
     * POST 请求构造
     *
     * @return
     */
    public static PostRequestBuilder post() {
        return new PostRequestBuilder(HttpConstants.POST);
    }

    /**
     * PUT 请求构造
     *
     * @return
     */
    public static PutRequestBuilder put() {
        return new PutRequestBuilder(HttpConstants.PUT);
    }

    /**
     * 上传图片到OSS 请求构造
     *
     * @return
     */
    public static OSSUploadBuilder upload(Context context) {
        return new OSSUploadBuilder(context);
    }

    /**
     * 取消某一个或者某一类请求
     *
     * @param tag
     */
    public static void cancel(Object tag) {
        instance().p_cancel(tag);
    }

    /**
     * 取消所有请求
     */
    public static void cancelAll() {
        instance().p_cancelAll();
    }


    /***********************************************
     * 内部处理方法
     ***********************************************/
    private static HTTP instance() {
        if (sHTTP == null) {
            synchronized (HTTP.class) {
                if (sHTTP == null) {
                    sHTTP = new HTTP();
                }
            }
        }
        return sHTTP;
    }

    private Executor get_p_Delivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    private OkHttpClient get_p_Default() {
        return sOkHttpClient;
    }

    private void p_setConfigController(HttpConfigController configController) {
        this.mController = configController;
    }

    private HttpConfigController get_p_Controller() {
        return mController;
    }

    private OkHttpClient get_p_OkHttpClient() {
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

    private void p_cancel(Object tag) {
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

    private void p_cancelAll() {
        sOkHttpClient.dispatcher().cancelAll();
    }

    /*********************************
     * 外部访问接口
     *********************************/
    public interface DataCallback {
        void requestSuccess(int id,String result);

        void requestFailed(int id,Call response, Exception e);
    }

}
