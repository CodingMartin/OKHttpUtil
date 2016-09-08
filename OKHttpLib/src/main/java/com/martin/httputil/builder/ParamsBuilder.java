package com.martin.httputil.builder;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.martin.httputil.crypto.SignUtils;
import com.martin.httputil.util.HSON;
import com.martin.httputil.util.HttpConstants;
import com.martin.httputil.util.Type;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * 描述: 请求参数构建
 * Author:Martin
 * CREATE:26/06/2016
 */
public class ParamsBuilder {
    private final RequestBuilder requestBuilder;
    private HashMap<String, Object> mParams;
    private String apiName;
    @ParamType
    private int mParamsType = Type.COMMAND;
    private String signKey;
    private String signValue;

    @IntDef(value = {Type.ORDINARY, Type.COMMAND, Type.RAW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ParamType {
    }

    public ParamsBuilder(RequestBuilder requestBuilder, String api) {
        if (mParams == null) mParams = new HashMap<>();
        this.requestBuilder = requestBuilder;
        this.apiName = api;
    }

    public ParamsBuilder addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public ParamsBuilder addParams(HashMap<String, Object> params) {
        if (params != null)
            mParams.putAll(params);
        return this;
    }

    /**
     * 请求参数包裹类型
     */
    public ParamsBuilder paramType(@ParamType int type) {
        this.mParamsType = type;
        return this;
    }

    /**
     * 请求参数包裹类型
     */
    public ParamsBuilder paramType(@ParamType int type, String signKey) {
        this.mParamsType = type;
        this.signKey = signKey;
        return this;
    }


    /**
     * 请求参数包裹类型
     */
    public ParamsBuilder paramType(@ParamType int type, String signKey, String signValue) {
        this.mParamsType = type;
        this.signKey = signKey;
        this.signValue = signValue;
        return this;
    }

    /***
     * 逻辑 : 如果请求的参数为空,那么业务参数就是空的,系统参数则会加上{@link #signKey} 和 {@link #signValue}的值,如果他们存在
     * 如过请求参数不为空,那么业务参数就会根据{@link Type}的类型来确认是什么样的业务参数,系统参数:如果有手动设置{@link #signKey} 和 {@link #signValue},那么优先使用手动设置的值.
     * 如果{@link #signKey} 和 {@link #signValue}为空那么先会根据{@link Type}来设置这两个值,如果{@link #mParamsType}为{@link Type#ORDINARY},那么不会去设置这两个值
     * 会查看下{@link #mParams}是否有内容,如果有那么使用它,如果没有那么整个系统参数就为默认的
     *
     * @return
     */
    public RequestBuilder sign() {
        try {
            String sysParams = null;
            String bizParams = null;
            //组装业务参数
            if (mParams != null && mParams.size() > 0) {
                if (mParamsType == Type.COMMAND) {
                    HashMap<String, Object> commandJson = new HashMap<>();
                    commandJson.put(HttpConstants.COMMAND, mParams);
                    if (signKey == null) signKey = HttpConstants.COMMAND;
                    if (signValue == null) signValue = HSON.toJsonWithoutEscape(mParams);
                    bizParams = HSON.toJsonWithoutEscape(commandJson);
                } else if (mParamsType == Type.RAW) {
                    HashMap<String, Object> rawJson = new HashMap<>();
                    rawJson.put(HttpConstants.RAW, mParams);
                    HashMap<String, Object> commandJson = new HashMap<>();
                    commandJson.put(HttpConstants.COMMAND, rawJson);
                    if (signKey == null) signKey = HttpConstants.COMMAND;
                    if (signValue == null) signValue = HSON.toJsonWithoutEscape(rawJson);
                    bizParams = HSON.toJsonWithoutEscape(commandJson);
                } else if (mParamsType == Type.ORDINARY) {
                    bizParams = HSON.toJsonWithoutEscape(mParams);
                }
            }
            sysParams = assembleSysParam();
            if (HttpConstants.DEBUG) {
                Log.d(HttpConstants.TAG, String.format("sysParams:%s bizParams:%s", sysParams, bizParams));
            }
            requestBuilder.setSysParams(sysParams);
            requestBuilder.setBizParams(bizParams);
        } catch (Exception e) {
            e.printStackTrace();
            if (HttpConstants.DEBUG) {
                Log.d(HttpConstants.TAG, "请求参数组装失败 :" + e.getMessage());
            }
        }

        return requestBuilder;
    }

    /**
     * 组装公共参数
     *
     * @return
     */
    @Nullable
    private String assembleSysParam() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        HashMap<String, Object> jsonObject = new HashMap<>();
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            jsonObject.put("apiName", this.apiName);
            jsonObject.put("appKey", HttpConstants.APP_KEY);
            jsonObject.put("timestamp", timestamp);
            jsonObject.put("format", "json");
            jsonObject.put("v", "1.0.0");
            jsonObject.put("session", "1");

            if (signKey != null && signValue != null) {
                treeMap.put(signKey, signValue);
            } else if (mParamsType == Type.ORDINARY && (mParams != null && mParams.size() > 0)) { //如果没有写入签名key并且参数类型为ORDINARY那么认为是要将所有请求参数作为签名内容
                treeMap.putAll(mParams);
            }

            treeMap.put("session", "1");
            treeMap.put("apiName", this.apiName);
            treeMap.put("appKey", HttpConstants.APP_KEY);
            treeMap.put("timestamp", timestamp);
            treeMap.put("format", "json");
            treeMap.put("v", "1.0.0");
            List<String> ignoreParams = new ArrayList<>();
            ignoreParams.add("sign");
            String sign = SignUtils.signUseMD5(treeMap, ignoreParams, HttpConstants.SECRET, true);

            jsonObject.put("sign", sign);
            return HSON.toJsonWithoutEscape(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            if (HttpConstants.DEBUG) {
                Log.d(HttpConstants.TAG, "签名数据组装失败 :" + e.getMessage());
            }
        }
        return null;
    }
}
