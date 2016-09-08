package com.martin.okhttputil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.martin.httputil.HTTP;
import com.martin.httputil.handler.DataCallback;
import com.martin.httputil.util.HttpConstants;
import com.martin.httputil.util.Type;

import java.util.HashMap;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements DataCallback {

    private static final java.lang.String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<java.lang.String, Object> params = new HashMap<>();
        params.put("reportType", 5);
        params.put("infoType", 52);
        params.put("infoId", "2110021455562909020");
        params.put("content", "/\\!@$!&$(*!$!&$*(!$");
        params.put("toAid", "2110021455562909020");
        params.put("fromAid", "2110021455562909019");

//
        HTTP.post()
                .api("report.add")
                .addParams(params)
                .paramType(Type.RAW)
                .sign()
                .id(110)
                .encode()
                .request(this);

        HTTP.post()
                .api("report.add")
                .addParams(params)
                .paramType(Type.RAW)
                .sign()
                .id(100)
                .encode()
                .request(this);

        HTTP.post()
                .api("report.add")
                .addParams(params)
                .paramType(Type.RAW)
                .sign()
                .id(130)
                .encode()
                .request(this);
//
//
//        HTTP
//                .post()
//                .api("report.add")
//                .addParams(params)
//                .paramType(ParamsType.RAW)
//                .sign()
//                .id(101)
//                .encode()
//                .enqueue(new ResponseHandle<>(this));
//        HTTP
//                .post()
//                .api("report.add")
//                .addParams(params)
//                .paramType(ParamsType.RAW)
//                .sign()
//                .id(102)
//                .encode()
//                .enqueue(new ResponseHandle<>(this));
//        HTTP
//                .post()
//                .api("report.add")
//                .addParams(params)
//                .paramType(ParamsType.RAW)
//                .sign()
//                .id(103)
//                .encode()
//                .enqueue(new ResponseHandle<>(this));
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    List<String> list = HTTP.upload(MainActivity.this).put(new File("/storage/emulated/0/DCIM/Camera/IMG_20150418_105516.jpg")).execute();
//                } catch (UploadException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
    }

    @Override
    public void onSuccess(int id, String result) {
        if (id == 100) {
            Toast.makeText(this, "打印一个东西", Toast.LENGTH_SHORT).show();
        }
        Log.d(HttpConstants.TAG, "success id: " + id + "/////" +result);
    }

    @Override public void onFailed(int id, Call call, Exception e) {
        Log.d(HttpConstants.TAG, "failed "+e+" id:" + id);
    }
}
