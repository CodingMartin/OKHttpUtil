package com.martin.httputil;

import java.util.TreeMap;

/**
 * 描述:
 * Author:Martin
 * CREATE:26/06/2016
 */
public class ParamsBuilder {
    private final RequestBuilder requestBuilder;
    private TreeMap<String, Object> mParams;

    public ParamsBuilder(RequestBuilder requestBuilder) {
        if (mParams == null) mParams = new TreeMap<>();
        this.requestBuilder = requestBuilder;
    }

    public RequestBuilder noParams() {
        requestBuilder.setSign(false);
        return requestBuilder;
    }

    public ParamsBuilder addParams(String key, Object value) {
        return this;
    }

    public ParamsBuilder setParams(TreeMap<String, Object> params) {
        return this;
    }

    public RequestBuilder sign() {
        //这里将参数进行签名并返回给RequestBuild
        String signedStr = null;
        requestBuilder.setJsonRequestParam(signedStr);
        requestBuilder.setSign(true);
        return requestBuilder;
    }

    /**如果不需要签名那么我们认为他是需要完整的参数,那么还是将参数列表返回就好了*/
    public RequestBuilder notSign() {
        requestBuilder.setParams(mParams);
        requestBuilder.setSign(false);
        return requestBuilder;
    }

}
