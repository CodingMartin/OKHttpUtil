package com.martin.okhttputil;

import com.martin.httputil.HTTP;
import com.martin.httputil.handler.DataCallbackEx;
import com.martin.httputil.util.Type;

import okhttp3.Call;

/**
 * Desc:
 * Author:Martin
 * Date:2016/9/8
 */
public class GetDataImpl implements Type {

    public static void report() {
        HTTP.post()
                .api("report.add")
                .paramType(RAW)
                .sign()
                .id(100)
                .encode()
                .request(new DataCallbackEx() {
                    @Override public void onStart(int id) {

                    }

                    @Override public void resultOnWorkThread(int id, String result) {

                    }

                    @Override public void onSuccess(int id, String body) {

                    }

                    @Override public void onFailed(int id, Call call, Exception e) {

                    }
                });
    }

}
