package com.martin.httputil.monitor;

import android.content.Context;

import java.util.Observable;
import java.util.Observer;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/25
 */
public class NetworkObservable extends Observable {

    private Context mContext;

    public NetworkObservable(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public void addObserver(Observer observer) {
        try {
            super.addObserver(observer);
//            NetworkInfo info = NetworkUtil.getCurrentActiveNetwork(mContext);
//            if (info != null) {
//                if (!info.isAvailable()) {
//                    observer.update(this, new NetworkObserver.Action(false, false, NetworkUtil.getSubType(mContext, info)));
//                } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
//                    observer.update(this, new NetworkObserver.Action(true, true, NetworkUtil.getSubType(mContext, info)));
//                } else if (info.getType() != ConnectivityManager.TYPE_BLUETOOTH) {
//                    observer.update(this, new NetworkObserver.Action(true, false, NetworkUtil.getSubType(mContext, info)));
//                } else {
//                    observer.update(this, new NetworkObserver.Action(false, false, NetworkUtil.getSubType(mContext, info)));
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObservers(Object data) {
        try {
            setChanged();
            super.notifyObservers(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
