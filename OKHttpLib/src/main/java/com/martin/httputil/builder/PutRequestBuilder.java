package com.martin.httputil.builder;

import com.martin.httputil.annotation.Method;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/12
 */
public class PutRequestBuilder extends PostRequestBuilder {

    public PutRequestBuilder(@Method String method) {
        super(method);
    }
}
