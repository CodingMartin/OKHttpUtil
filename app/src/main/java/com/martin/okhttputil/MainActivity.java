package com.martin.okhttputil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.martin.httputil.OKHttpUtil;
import com.martin.httputil.ResponseHandler;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OKHttpUtil.get().url("http://www.baidu.com").noParams().enqueue(new ResponseHandler<String>() {
            @Override
            protected void onStart() {
                String name = Thread.currentThread().getName();
                Log.d(TAG, "onStart: " + name);
            }

            @Override
            protected void resultOnWorkThread(String result) {
                String name = Thread.currentThread().getName();
                Log.d(TAG, "resultOnWorkThread: " + name);
            }

            @Override
            protected void onSuccess(String result) {
                String name = Thread.currentThread().getName();
                Log.d(TAG, "onSuccess: " + name);
                Log.d(TAG, "onSuccess: " + result);
                Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onFailed(int code) {
                String name = Thread.currentThread().getName();
                Log.d(TAG, "onFailed: " + name);
            }


        });
    }
}
