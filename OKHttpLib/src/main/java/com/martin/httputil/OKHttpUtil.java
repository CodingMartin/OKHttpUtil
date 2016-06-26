package com.martin.httputil;

import android.os.Handler;
import android.os.Looper;

import okhttp3.OkHttpClient;

/**
 * 描述:
 * Author:Martin
 * CREATE:26/06/2016
 */
public class OKHttpUtil {
    private static OkHttpClient sOkHttpClient;
    private static Handler mDispense;
    private static Object mlock = new Object();

    private OKHttpUtil() {
    }

    public static Handler getDispenseHandler() {
        if (mDispense == null) {
            synchronized (mlock) {
                if (mDispense == null)
                    mDispense = new Handler(Looper.getMainLooper());
            }
        }
        return mDispense;
    }

    public static OkHttpClient getDefault() {
        if (sOkHttpClient == null) {
            synchronized (OKHttpUtil.class) {
                if (sOkHttpClient == null) {
                    sOkHttpClient = new OkHttpClient();
                }
            }
        }
        return sOkHttpClient;
    }

    public static void setDefault(OkHttpClient okHttpClient) {
        sOkHttpClient = okHttpClient;
    }

    public static GetRequestBuilder get() {
        return new GetRequestBuilder(Method.GET);
    }

    public static PostRequestBuilder post() {
        return new PostRequestBuilder(Method.POST);
    }

}
