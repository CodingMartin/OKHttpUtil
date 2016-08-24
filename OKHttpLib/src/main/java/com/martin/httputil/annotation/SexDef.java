package com.martin.httputil.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Desc:
 * Author:Martin
 * Date:2016/8/9
 */
@IntDef({Constants.MAIL,Constants.FEMAIL})
@Retention(RetentionPolicy.SOURCE)
public @interface SexDef {
}
