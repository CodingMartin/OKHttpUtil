package com.martin.httputil.util;

/**
 * 描述:
 * Author:Martin
 * CREATE:26/06/2016
 */
public enum Method {

    GET("GET"), POST("POST"), PUT("PUT");

    private String method;

    Method(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
