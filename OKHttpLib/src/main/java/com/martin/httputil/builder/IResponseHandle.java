package com.martin.httputil.builder;

import com.martin.httputil.pojo.Result;

import java.io.IOException;

import okhttp3.Call;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/14
 */
public interface IResponseHandle<T> {

    /**
     * 请求成功,在UI线程
     */
    void onSuccess(int id, Result<T> result);

    /**
     * 请求失败,在UI线程
     */
    void onFailed(int id, Call call, IOException e);
}
