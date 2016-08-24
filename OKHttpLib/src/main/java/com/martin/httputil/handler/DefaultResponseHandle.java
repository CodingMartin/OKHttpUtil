package com.martin.httputil.handler;

import com.martin.httputil.builder.BaseResponse;
import com.martin.httputil.builder.IResponseHandle;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/14
 */
public abstract class DefaultResponseHandle<T> extends BaseResponse<T> {
    public DefaultResponseHandle(IResponseHandle<T> responseHandleEx) {
        super(responseHandleEx);
    }
}
