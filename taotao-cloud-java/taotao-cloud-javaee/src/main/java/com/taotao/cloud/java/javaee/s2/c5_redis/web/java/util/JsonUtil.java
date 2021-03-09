package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * JSON工具类
 */
public class JsonUtil {

    /**
     * 将Java对象转化为JSON字符串
     */
    public static String getJSON(Object obj) {
        if (null == obj) {
            return "";
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            //转换date类型的时候，时间戳
            mapper.getSerializationConfig().with(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            String jsonStr = mapper.writeValueAsString(obj);
            return jsonStr;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将JSON字符串转化为Java对象，集合
     */
    public static <T> T getObj(String json, TypeReference<T> ref)
            throws IOException {
        if (null == json || json.length() == 0) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.getDeserializationConfig().with(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return (T) mapper.readValue(json, ref);
    }

    /**
     * 将JSON字符串转化为Java对象，一个对象
     */
    public static Object getObj(String json, Class pojoClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, pojoClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
