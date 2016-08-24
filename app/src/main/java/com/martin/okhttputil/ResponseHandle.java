package com.martin.okhttputil;

import com.martin.httputil.builder.BaseResponse;
import com.martin.httputil.builder.IResponseHandle;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/14
 */
public class ResponseHandle<T> extends BaseResponse<T> {
    public ResponseHandle(IResponseHandle<T> responseHandleEx) {
        super(responseHandleEx);
    }
}
