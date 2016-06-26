package com.martin.httputil;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 描述: 请求结果统一处理类
 * Author:Martin
 * CREATE:26/06/2016
 */
public abstract class ResponseHandler<T> implements Callback, Runnable {
    private int code;
    private boolean success;
    private T result;

    @Override
    public final void onFailure(Call call, IOException e) {
        code = 500;
        success = false;
        OKHttpUtil.getDispenseHandler().post(this);
    }

    @Override
    public final void onResponse(Call call, Response response) throws IOException {
        code = response.code();
        success = code == 200;
        if (success) { //请求成功
            String body = response.body().string();
            if (result instanceof List || result instanceof Array) {
                //
                Log.d("TAG", "请求结果需要的是集合");
            } else {
                Log.d("TAG", "这里还要考虑一下");
            }
            try {
                result = JSON.parseObject(body, new TypeReference<T>() {
                }.getType());
            } catch (Exception e) {
                System.err.print("Json 解析失败,可能是类型错误:" + e.getMessage());
                result = (T) body;
            }

            resultOnWorkThread(result);
        }
        OKHttpUtil.getDispenseHandler().post(this);
    }

    /**
     * 请求成功了之后可能要根据结果进行一些统一的处理
     *
     * @param result 请求结果
     * @return {@code true} 表示继续执行{@link #onSuccess(Object)} <br> {@code false} 表示不再继续往下执行
     */
    protected boolean unifiedDisposal(T result) {

        return true;
    }

    /**
     * 这个方法运行在UI线程
     * <br>请求开始执行前
     */
    protected void onStart() {
    }

    /**
     * 执行在异步线程,如果需要在返回结果之后进行其它操作可以在这里处理
     */
    protected void resultOnWorkThread(T result) {

    }

    /**
     * 请求成功,在UI线程
     */
    protected void onSuccess(T result) {

    }

    /**
     * 请求失败,在UI线程
     */
    protected void onFailed(int code) {

    }

    @Override
    public final void run() {
        if (success) {
            if (unifiedDisposal(result)) {
                onSuccess(result);
            }
        } else {
            onFailed(code);
        }
    }
}
