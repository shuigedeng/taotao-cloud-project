/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.workflow.biz.common.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.serializer.SerializerFeature;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/** */
public class JsonUtil {

    /**
     * list转成JSONField
     *
     * @param lists
     * @return
     */
    public static List listToJsonField(List lists) {
        // 空的也显示
        String jsonStr = JSONArray.toJSONString(lists, SerializerFeature.WriteMapNullValue);
        // 空的不显示
        List list = JSONArray.parseObject(jsonStr, List.class);
        return list;
    }

    /**
     * 对象转成Map
     *
     * @param object
     * @return
     */
    public static Map<String, Object> entityToMap(Object object) {
        String jsonStr = JSONObject.toJSONString(object);
        Map<String, Object> map = JSONObject.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {});
        return map;
    }

    /**
     * String转成Map
     *
     * @param object
     * @return
     */
    public static Map<String, Object> stringToMap(String object) {
        Map<String, Object> map = JSONObject.parseObject(object, new TypeReference<Map<String, Object>>() {});
        return map;
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象
     *
     * @param jsonData JSON数据
     * @param clazz 指定的java对象
     * @return 指定的java对象
     */
    public static <T> T getJsonToBean(String jsonData, Class<T> clazz) {
        return JSON.parseObject(jsonData, clazz);
    }

    /**
     * 功能描述：把JSON数据转换成JSONArray数据
     *
     * @param json
     * @return
     */
    public static JSONArray getJsonToJsonArray(String json) {
        return JSONArray.parseArray(json);
    }

    /**
     * 功能描述：把List数据转换成JSONArray数据
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> JSONArray getListToJsonArray(List<T> list) {
        return JSONArray.parseArray(JsonUtil.getObjectToString(list));
    }

    /**
     * 功能描述：把java对象转换成JSON数据
     *
     * @param object java对象
     * @return JSON数据
     */
    public static String getObjectToString(Object object) {
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 功能描述：把java对象转换成JSON数据,时间格式化
     *
     * @param object java对象
     * @return JSON数据
     */
    public static String getObjectToStringDateFormat(Object object, String dateFormat) {
        JSON.defaultTimeZone = TimeZone.getTimeZone(ZoneId.of("+8"));
        return JSON.toJSONStringWithDateFormat(object, dateFormat, SerializerFeature.WriteMapNullValue);
    }

    //    /**
    //     * 功能描述：把JSON数据转换成指定的java对象列表
    //     * @param jsonData JSON数据
    //     * @param clazz 指定的java对象
    //     * @return List<T>
    //     */
    //    public static <T> List<T> getJsonToListStringDateFormat(String jsonData, Class<T>
    // clazz,String dateFormat) {
    //        JSONArray jsonArray=JSONUtil.getJsonToJsonArray(jsonData);
    //        JSONArray newJsonArray=JSONUtil.getJsonToJsonArray(jsonData);
    //        for (int i = 0; i < jsonArray.size(); i++) {
    //            JSONObject jsonObject = jsonArray.getJSONObject(i);
    //            newJsonArray.add(JSON.toJSONStringWithDateFormat(jsonObject,
    // dateFormat,SerializerFeature.WriteMapNullValue));
    //        }
    //        jsonData=JSONUtil.getObjectToString(newJsonArray);
    //        return JSON.parseArray(jsonData, clazz);
    //    }
    //
    //    public static void main(String[] args) {
    //        Date date=new Date();
    //        String obk="[" +
    //                "{\"date\":\""+date+"\"},{\"date\":\"1603165505\"}" +
    //                "]";
    //       List<String> list1= getJsonToList(obk,String.class);
    //        List<String> list11= getJsonToListStringDateFormat(obk,String.class,"yyyy-MM-dd");
    //        LogUtils.info("aaa");
    //    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象列表
     *
     * @param jsonData JSON数据
     * @param clazz 指定的java对象
     * @return List<T>
     */
    public static <T> List<T> getJsonToList(String jsonData, Class<T> clazz) {
        return JSON.parseArray(jsonData, clazz);
    }

    /**
     * 功能描述：把JSON数据转换成较为复杂的List<Map<String, Object>>
     *
     * @param jsonData JSON数据
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getJsonToListMap(String jsonData) {
        return JSON.parseObject(jsonData, new TypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * 功能描述：把JSONArray数据转换成较为复杂的List<Map<String, Object>>
     *
     * @param jsonArray JSONArray数据
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getJsonToList(JSONArray jsonArray) {
        return JSON.parseObject(JSON.toJSONString(jsonArray), new TypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象
     *
     * @param dto dto对象
     * @param clazz 指定的java对象
     * @return 指定的java对象
     */
    public static <T> T getJsonToBean(Object dto, Class<T> clazz) {
        return JSON.parseObject(getObjectToString(dto), clazz);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象列表
     *
     * @param dto dto对象
     * @param clazz 指定的java对象
     * @return List<T>
     */
    public static <T> List<T> getJsonToList(Object dto, Class<T> clazz) {
        return JSON.parseArray(getObjectToString(dto), clazz);
    }
}
