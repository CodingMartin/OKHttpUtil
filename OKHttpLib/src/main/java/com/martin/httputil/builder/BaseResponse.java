package com.martin.httputil.builder;

import com.martin.httputil.HTTP;
import com.martin.httputil.handler.DataCallback;
import com.martin.httputil.handler.DataCallbackEx;
import com.martin.httputil.handler.HttpConfigController;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 描述: 请求结果统一处理类
 * Author:Martin
 * CREATE:26/06/2016
 */
public abstract class BaseResponse implements Callback {
    private int id;
    private DataCallback mIResponseHandle;

    void setId(int id) {
        this.id = id;
    }

    public BaseResponse(DataCallback responseHandleEx) {
        this.mIResponseHandle = responseHandleEx;
    }

    @Override
    public final void onFailure(Call call, IOException e) {
        deliverFailure(call, e);
    }

    @Override
    public final void onResponse(Call call, Response response) throws IOException {
        if (call.isCanceled()) return;
        try {
            if (response.isSuccessful()) { //请求成功
                String body = response.body().string();
                resultOnWorkThread(id, body);
                deliverSuccess(body);
            } else {
                deliverFailure(call, new IOException("request is failed, response' code is:" + response.code()));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            deliverFailure(call, e1);
        } finally {
            if (response.body() != null)
                response.body().close();
        }
    }

    private void deliverFailure(final Call call, final Exception e) {
        HTTP.getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                onFailed(id, call, e);
            }
        });
    }

    protected void deliverSuccess(String result) {
        final String finalResult = result;
        HTTP.getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                HttpConfigController mPreHandler = HTTP.getController();
                if (mPreHandler != null) {
                    if (mPreHandler.unitHandle(finalResult)) {
                        onSuccess(id, finalResult);
                    }
                } else {
                    onSuccess(id, finalResult);
                }
            }
        });
    }

    /**
     * 如果重写了这个方法务必返回 ,这样默认的解析就可以被覆盖
     */
    protected String parseResult(String body) {
        return null;
    }

    /**
     * 这个方法运行在UI线程
     * <br>请求开始执行前
     */
    protected void onStart(int id) {
        if (mIResponseHandle != null) {
            if (mIResponseHandle instanceof DataCallbackEx) {
                ((DataCallbackEx) mIResponseHandle).onStart(id);
            }
        }
    }

    /**
     * 执行在异步线程,如果需要在返回结果之后进行其它操作可以在这里处理
     */
    protected void resultOnWorkThread(int id, String result) {
        if (mIResponseHandle != null) {
            if (mIResponseHandle instanceof DataCallbackEx) {
                ((DataCallbackEx) mIResponseHandle).resultOnWorkThread(id, result);
            }
        }
    }

    /**
     * 请求成功,在UI线程
     */
    protected void onSuccess(int id, String result) {
        if (mIResponseHandle != null) {
            mIResponseHandle.onSuccess(id, result);
        }
    }

    /**
     * 请求失败,在UI线程
     */
    protected void onFailed(int id, Call call, Exception e) {
        if (mIResponseHandle != null) {
            mIResponseHandle.onFailed(id, call, e);
        }
    }
}
