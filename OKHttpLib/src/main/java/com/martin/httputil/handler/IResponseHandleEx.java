package com.martin.httputil.handler;

import com.martin.httputil.builder.IResponseHandle;
import com.martin.httputil.pojo.Result;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/14
 */
public interface IResponseHandleEx<T> extends IResponseHandle<T> {


    /**
     * 这个方法运行在UI线程
     * <br>请求开始执行前
     */
    void onStart(int id);

    /**
     * 执行在异步线程,如果需要在返回结果之后进行其它操作可以在这里处理
     */
    void resultOnWorkThread(int id, Result<T> result);

}
