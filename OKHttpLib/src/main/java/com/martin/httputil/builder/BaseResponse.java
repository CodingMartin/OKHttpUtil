package com.martin.httputil.builder;

import com.google.gson.reflect.TypeToken;
import com.martin.httputil.OKHttpUtil;
import com.martin.httputil.handler.IResponseHandleEx;
import com.martin.httputil.handler.HttpConfigController;
import com.martin.httputil.pojo.Result;
import com.martin.httputil.util.HSON;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 描述: 请求结果统一处理类
 * Author:Martin
 * CREATE:26/06/2016
 */
public abstract class BaseResponse<T> implements Callback {
    private int id;
    private IResponseHandle<T> mIResponseHandle;
    private boolean cache;

    void setId(int id) {
        this.id = id;
    }

    public BaseResponse(IResponseHandle<T> responseHandleEx) {
        this.mIResponseHandle = responseHandleEx;
    }

    public BaseResponse() {
    }


    @Override
    public final void onFailure(Call call, IOException e) {
        sendFailure(call, e);
    }

    protected void sendFailure(final Call call, final IOException e) {
        OKHttpUtil.getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                onFailed(id, call, e);
            }
        });
    }

    protected void sendSuccess(Result<T> result) {
        final Result<T> finalResult = result;
        OKHttpUtil.getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                HttpConfigController mPreHandler = OKHttpUtil.getController();
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

    @Override
    public final void onResponse(Call call, Response response) throws IOException {
        try {
            if (call.isCanceled()) {
                sendFailure(call, new IOException("Request is Canceled!"));
            }

            if (response.isSuccessful()) { //请求成功
                String body = response.body().string();
                Result<T> result = parseResult(body);
                if (result == null) {
                    result = HSON.parse(body, new TypeToken<Result<T>>() {
                    });
                }
                resultOnWorkThread(id, result);
                sendSuccess(result);
            } else {
                sendFailure(call, new IOException("request is failed, response' code is:" + response.code()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            sendFailure(call, e);
        } finally {
            if (response.body() != null)
                response.body().close();
        }
    }

    /**
     * 如果重写了这个方法务必返回true ,这样默认的解析就可以被覆盖
     */
    protected Result<T> parseResult(String body) {
        return null;
    }

    /**
     * 这个方法运行在UI线程
     * <br>请求开始执行前
     */
    protected void onStart(int id) {
        if (mIResponseHandle != null) {
            if (mIResponseHandle instanceof IResponseHandleEx) {
                ((IResponseHandleEx) mIResponseHandle).onStart(id);
            }
        }
    }

    /**
     * 执行在异步线程,如果需要在返回结果之后进行其它操作可以在这里处理
     */
    protected void resultOnWorkThread(int id, Result<T> result) {
        if (mIResponseHandle != null) {
            if (mIResponseHandle instanceof IResponseHandleEx) {
                ((IResponseHandleEx) mIResponseHandle).resultOnWorkThread(id, result);
            }
        }
    }

    /**
     * 请求成功,在UI线程
     */
    protected void onSuccess(int id, Result<T> result) {
        if (mIResponseHandle != null) {
            mIResponseHandle.onSuccess(id, result);
        }
    }

    /**
     * 请求失败,在UI线程
     */
    protected void onFailed(int id, Call call, IOException e) {
        if (mIResponseHandle != null) {
            mIResponseHandle.onFailed(id, call, e);
        }
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }
}
