package com.martin.httputil.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/7
 */
public class HSON {
    private static final String DEFAULT_OBJ = "{}";
    private static Gson sGson;
    private static Gson sGsonBuidler;

    public static Gson getGson() {
        if (sGson == null) {
            sGson = new Gson();
        }
        return sGson;
    }

    public static Gson getGsonBuilder() {
        if (sGsonBuidler == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.disableHtmlEscaping();
            sGsonBuidler = gsonBuilder.create();
        }
        return sGsonBuidler;
    }

    private static Gson checkGson(Gson gson) {
        return gson == null ? getGson() : gson;
    }

    public static <T> T parse(String json, Class<T> clazz) throws Exception{
        return parse(null, json, clazz);
    }

    public static <T> T parse(Gson gson, String json, Class<T> clazz) throws Exception {
        if (json == null || clazz == null) return null;
        gson = checkGson(gson);
        return gson.fromJson(json, clazz);
    }

    public static <T> T parse(String json, TypeToken<T> token) throws Exception {
        return parse(null, json, token);
    }

    public static <T> T parse(Gson gson, String json, TypeToken<T> token) throws Exception{
        if (json == null || token == null) return null;
        gson = checkGson(gson);
        return gson.fromJson(json, token.getType());
    }

    public static <T> String toJson(Gson gson, T obj) {
        if (obj == null) return null;
        gson = checkGson(gson);
        return gson.toJson(obj);
    }

    public static <T> String toJson(T obj) throws Exception {
        return toJson(null, obj);
    }

    public static String toJsonWithoutEscape(Map<String, Object> params) throws Exception {
        if (params == null || params.size() == 0) return DEFAULT_OBJ;
        return getGsonBuilder().toJson(params);
    }

    public static String toJson(Map<String, Object> params) throws Exception {
        if (params == null) return null;
        return getGson().toJson(params);
    }
}
