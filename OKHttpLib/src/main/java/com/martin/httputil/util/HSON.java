package com.martin.httputil.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Desc:
 * Author:Martin
 * Date:2016/7/7
 */
public class HSON {
    private static final String DEFAULT_OBJ = "{}";
    private static Gson sGson;

    public static Gson getGson() {
        if (sGson == null) {
            sGson = new Gson();
        }
        return sGson;
    }

    private static Gson checkGson(Gson gson) {
        return gson == null ? getGson() : gson;
    }

    public static <T> T parse(String json, Class<T> clazz) {
        return parse(null, json, clazz);
    }

    public static <T> T parse(Gson gson, String json, Class<T> clazz) {
        if (json == null || clazz == null) return null;
        gson = checkGson(gson);
        return gson.fromJson(json, clazz);
    }

    public static <T> T parse(String json, TypeToken<T> token) {
        return parse(null, json, token);
    }

    public static <T> T parse(Gson gson, String json, TypeToken<T> token) {
        if (json == null || token == null) return null;
        gson = checkGson(gson);
        return gson.fromJson(json, token.getType());
    }

    public static <T> String toJson(Gson gson, T obj) {
        if (obj == null) return null;
        gson = checkGson(gson);
        return gson.toJson(obj);
    }

    public static <T> String toJson(T obj) {
        return toJson(null, obj);
    }

    public static String toJson(Map<String, Object> params) throws JSONException {
        if (params == null || params.size() == 0) return DEFAULT_OBJ;
        JSONObject o = new JSONObject();
        Set<String> set = params.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = params.get(key);
            o.put(key, value);
        }
        return o.toString();
    }
}
