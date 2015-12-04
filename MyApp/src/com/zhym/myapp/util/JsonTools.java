package com.zhym.myapp.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by lenovo on 2015/11/6.
 */
public class JsonTools {
    /**
     * 得到一个json类型的字符串对象
     * */
    public static String getJsonString(String key, Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);                 //向JSONObject对象放入key/value
        return jsonObject.toString();
    }

    /**
     * 得到一个json对象
     * */
    public static JSONObject getJsonObject(String key, Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);
        return jsonObject;
    }



  }
