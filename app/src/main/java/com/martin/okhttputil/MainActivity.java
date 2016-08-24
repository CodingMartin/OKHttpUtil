package com.martin.okhttputil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.martin.httputil.OKHttpUtil;
import com.martin.httputil.annotation.Constants;
import com.martin.httputil.annotation.SexDef;
import com.martin.httputil.builder.IResponseHandle;
import com.martin.httputil.image.ImageImpl;
import com.martin.httputil.image.ImageLoader;
import com.martin.httputil.pojo.Result;
import com.martin.httputil.util.HttpConstants;
import com.martin.httputil.util.ParamsType;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements IResponseHandle<Object> {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String, Object> params = new HashMap<>();
        params.put("reportType", 5);
        params.put("infoType", 52);
        params.put("infoId", "2110021455562909019");
        params.put("content", "xxxxxxx");
        params.put("toAid", "2110021455562909019");
        params.put("fromAid", "2110021455562909019");

//
        OKHttpUtil
                .post()
                .api("report.add")
                .addParams(params)
                .paramType(ParamsType.RAW)
                .sign()
                .id(100)
                .cache()
                .encode()
                .enqueue(new ResponseHandle<>(this));
        ImageLoader.Builder builder = new ImageLoader.Builder();
        ImageImpl.instance().display("", new ImageView(this));
//
//
//        OKHttpUtil
//                .post()
//                .api("report.add")
//                .addParams(params)
//                .paramType(ParamsType.RAW)
//                .sign()
//                .id(101)
//                .encode()
//                .enqueue(new ResponseHandle<>(this));
//        OKHttpUtil
//                .post()
//                .api("report.add")
//                .addParams(params)
//                .paramType(ParamsType.RAW)
//                .sign()
//                .id(102)
//                .encode()
//                .enqueue(new ResponseHandle<>(this));
//        OKHttpUtil
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
//                    List<String> list = OKHttpUtil.upload(MainActivity.this).put(new File("/storage/emulated/0/DCIM/Camera/IMG_20150418_105516.jpg")).execute();
//                } catch (UploadException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();

    setSex(Constants.FEMAIL);
    }

    public void setSex(@SexDef int sex) {
        switch (sex) {
            case Constants.MAIL:
                break;
            case Constants.FEMAIL:
                break;
        }
    }

    @Override
    public void onSuccess(int id, Result<Object> result) {
        if (id == 100) {
            Toast.makeText(this, "打印一个东西", Toast.LENGTH_SHORT).show();
        }
        Log.d(HttpConstants.TAG, "success id:" + id);
    }

    @Override
    public void onFailed(int id, Call call, IOException e) {
        Log.d(HttpConstants.TAG, "failed id:" + id);
    }
}
