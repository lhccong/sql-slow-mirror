package com.cong.sql.slowmirror.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * gson util
 *
 * @author cong
 * @date 2024/04/15
 */
public class GsonUtil {

    private static final Gson GSON = new GsonBuilder().create();

    /**
     * 对象、集合转json
     *
     * @param obj 对象
     * @return json
     */
    public static String bean2Json(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * json转对象
     *
     * @param jsonString json
     * @param objClass   对象类型
     * @param <T>        对象类型
     * @return 对象
     */
    public static <T> T json2Bean(String jsonString, Class<T> objClass) {
        return GSON.fromJson(jsonString, objClass);
    }

}
