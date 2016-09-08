package com.martin.httputil.util;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/12
 */
public interface Type {
    /**
     * 普通的请求参数
     */
    int ORDINARY = 0;
    /**
     * 包含rawRequest的请求参数
     */
    int RAW = 1;
    /**
     * 包含command的请求参数
     */
    int COMMAND = 2;

}
