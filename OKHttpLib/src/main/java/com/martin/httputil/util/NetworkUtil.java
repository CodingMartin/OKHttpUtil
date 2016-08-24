package com.martin.httputil.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.martin.httputil.monitor.Network;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/25
 */
public class NetworkUtil {
    public static NetworkInfo getCurrentActiveNetwork(Context mContext) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                return connectivity.getActiveNetworkInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到当前的手机网络类型
     *
     * @param context
     * @return
     */
    public static Network.Type getSubType(Context context, NetworkInfo info) {
        NetworkInfo netWorkInfo = NetworkUtil.getCurrentActiveNetwork(context);
        if (netWorkInfo != null && netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            switch (netWorkInfo.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_GPRS://GPRS （联通2g）
                case TelephonyManager.NETWORK_TYPE_EDGE://EDGE（移动2g）
                case TelephonyManager.NETWORK_TYPE_CDMA: {// CDMA （电信2g）
                    return Network.Type.MOBILE2G;//2G网络
                }
                case TelephonyManager.NETWORK_TYPE_UMTS://UMTS（联通3g）
                case TelephonyManager.NETWORK_TYPE_HSDPA://HSDPA（联通3g）
                case TelephonyManager.NETWORK_TYPE_EVDO_B://EVDO  版本B（电信3g）
                case TelephonyManager.NETWORK_TYPE_EVDO_0://EVDO  版本0.（电信3g）
                case TelephonyManager.NETWORK_TYPE_EVDO_A: {//EVDO   版本A （电信3g）
                    return Network.Type.MOBILE3G;//3G网络
                }
                case TelephonyManager.NETWORK_TYPE_LTE: {
                    return Network.Type.MOBILE4G;//4G网络
                }
            }
            return Network.Type.MOBILE;//未知的移动网络
        } else if (netWorkInfo != null && netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return Network.Type.WIFI;
        }
        return Network.Type.UNKNOWN;//未知网络
    }

    public static boolean has(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null) && info.isAvailable();
    }
}
