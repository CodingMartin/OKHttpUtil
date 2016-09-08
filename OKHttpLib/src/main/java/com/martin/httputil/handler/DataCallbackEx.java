package com.martin.httputil.handler;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/14
 */
public interface DataCallbackEx extends DataCallback {


    /**
     * 这个方法运行在UI线程
     * <br>请求开始执行前
     */
    void onStart(int id);

    /**
     * 执行在异步线程,如果需要在返回结果之后进行其它操作可以在这里处理
     */
    void resultOnWorkThread(int id, String result);

}
