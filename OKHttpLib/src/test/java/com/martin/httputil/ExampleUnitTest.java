package com.martin.httputil;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void jsonTest() throws Exception {
        TreeMap<String,Object> params = new TreeMap<>();
        params.put("type","json");
        params.put("age",12);
        params.put("name","jack");

        try {
            JSONObject object = new JSONObject();
            Set<String> keySet = params.keySet();
            Iterator<String> keys = keySet.iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = params.get(key);
                object.put(key,value);
            }
            String json = object.toString();
            String mJson = "{'type':'json','age':12,'name':'jack'}";
            Log.d("TAG",json);
            Log.d("TAG",mJson);
            System.out.println(json);
            assertEquals(json,mJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}