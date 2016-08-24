package com.martin.httputil.monitor;

import android.app.Application;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/25
 */
public class NetworkMonitorApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkMonitor.getInstance().initialise(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        NetworkMonitor.getInstance().release();
    }
}
