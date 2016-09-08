package com.martin.httputil.annotation;

import android.support.annotation.StringDef;

import com.martin.httputil.util.HttpConstants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Desc:
 * Author:Martin
 * Date:2016/8/26
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({HttpConstants.GET, HttpConstants.POST, HttpConstants.PUT})
public @interface Method {
}
