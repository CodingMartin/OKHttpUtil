package com.martin.okhttputil;

import android.util.Log;
import android.widget.Toast;

import com.martin.httputil.HTTP;
import com.martin.httputil.handler.HttpConfigController;
import com.martin.httputil.monitor.NetworkMonitor;
import com.martin.httputil.monitor.NetworkMonitorApp;
import com.martin.httputil.monitor.NetworkObserver;

import java.io.File;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/25
 */
public class App extends NetworkMonitorApp {


    NetworkObserver mObserver = new NetworkObserver() {
        @Override
        public void notify(Action action) {
            if (action.isAvailable) {
                Toast.makeText(App.this, "网络连接 wifi:" + action.isWifi, Toast.LENGTH_SHORT).show();
                Log.e(MainActivity.class.getSimpleName(), "网络可用 > " + "网络类型:" + action.type.toString());
            } else {
                Toast.makeText(App.this, "网络断开", Toast.LENGTH_SHORT).show();
                Log.e(MainActivity.class.getSimpleName(), "网络不可用 > " + "网络类型:" + action.type.toString());
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        NetworkMonitor.getInstance().addObserver(mObserver);
        HTTP.setConfigController(new HttpConfigController() {
            @Override
            public boolean unitHandle(String result) {
                return true;
            }

            @Override
            public boolean networkCheck() {
                return false;
            }

        });
    }
}
