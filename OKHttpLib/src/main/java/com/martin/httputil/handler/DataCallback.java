package com.martin.httputil.handler;

import okhttp3.Call;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/14
 */
public interface DataCallback {

    /**
     * 请求成功,在UI线程
     */
    void onSuccess(int id, String body);

    /**
     * 请求失败,在UI线程
     */
    void onFailed(int id, Call call, Exception e);
}
