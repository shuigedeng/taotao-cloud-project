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

package com.taotao.cloud.realtime.datalake.mall.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.realtime.mall.utils.PhoenixUtil;
import com.taotao.cloud.realtime.mall.utils.RedisUtil;
import java.util.List;
import org.apache.flink.api.java.tuple.Tuple2;
import redis.clients.jedis.Jedis;

/**
 *
 * Date: 2021/2/5
 * Desc: 用于维度查询的工具类  底层调用的是PhoenixUtil
 * select * from dim_base_trademark where id=10 and name=zs;
 */
public class DimUtil {
    // 从Phoenix中查询数据，没有使用缓存
    public static JSONObject getDimInfoNoCache(
            String tableName, Tuple2<String, String>... cloNameAndValue) {
        // 拼接查询条件
        String whereSql = " where ";
        for (int i = 0; i < cloNameAndValue.length; i++) {
            Tuple2<String, String> tuple2 = cloNameAndValue[i];
            String filedName = tuple2.f0;
            String fieldValue = tuple2.f1;
            if (i > 0) {
                whereSql += " and ";
            }
            whereSql += filedName + "='" + fieldValue + "'";
        }

        String sql = "select * from " + tableName + whereSql;
        System.out.println("查询维度的SQL:" + sql);
        List<JSONObject> dimList = PhoenixUtil.queryList(sql, JSONObject.class);
        JSONObject dimJsonObj = null;
        // 对于维度查询来讲，一般都是根据主键进行查询，不可能返回多条记录，只会有一条
        if (dimList != null && dimList.size() > 0) {
            dimJsonObj = dimList.get(0);
        } else {
            System.out.println("维度数据没有找到:" + sql);
            System.out.println();
        }
        return dimJsonObj;
    }

    // 在做维度关联的时候，大部分场景都是通过id进行关联，所以提供一个方法，只需要将id的值作为参数传进来即可
    public static JSONObject getDimInfo(String tableName, String id) {
        return getDimInfo(tableName, Tuple2.of("id", id));
    }

    /*
        优化：从Phoenix中查询数据，加入了旁路缓存
             先从缓存查询，如果缓存没有查到数据，再到Phoenix查询，并将查询结果放到缓存中

        redis
            类型：    string
            Key:     dim:表名:值       例如：dim:DIM_BASE_TRADEMARK:10_xxx
            value：  通过PhoenixUtil到维度表中查询数据，取出第一条并将其转换为json字符串
            失效时间:  24*3600

        //"DIM_BASE_TRADEMARK", Tuple2.of("id", "13"),Tuple2.of("tm_name","zz"))

        redisKey= "dim:dim_base_trademark:"
        where id='13'  and tm_name='zz'


        dim:dim_base_trademark:13_zz ----->Json

        dim:dim_base_trademark:13_zz
    */

    public static JSONObject getDimInfo(
            String tableName, Tuple2<String, String>... cloNameAndValue) {
        // 拼接查询条件
        String whereSql = " where ";
        String redisKey = "dim:" + tableName.toLowerCase() + ":";
        for (int i = 0; i < cloNameAndValue.length; i++) {
            Tuple2<String, String> tuple2 = cloNameAndValue[i];
            String filedName = tuple2.f0;
            String fieldValue = tuple2.f1;
            if (i > 0) {
                whereSql += " and ";
                redisKey += "_";
            }
            whereSql += filedName + "='" + fieldValue + "'";
            redisKey += fieldValue;
        }

        // 从Redis中获取数据
        Jedis jedis = null;
        // 维度数据的json字符串形式
        String dimJsonStr = null;
        // 维度数据的json对象形式
        JSONObject dimJsonObj = null;
        try {
            // 获取jedis客户端
            jedis = RedisUtil.getJedis();
            // 根据key到Redis中查询
            dimJsonStr = jedis.get(redisKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("从redis中查询维度失败");
        }

        // 判断是否从Redis中查询到了数据
        if (dimJsonStr != null && dimJsonStr.length() > 0) {
            dimJsonObj = JSON.parseObject(dimJsonStr);
        } else {
            // 如果在Redis中没有查到数据，需要到Phoenix中查询
            String sql = "select * from " + tableName + whereSql;
            System.out.println("查询维度的SQL:" + sql);
            List<JSONObject> dimList = PhoenixUtil.queryList(sql, JSONObject.class);
            // 对于维度查询来讲，一般都是根据主键进行查询，不可能返回多条记录，只会有一条
            if (dimList != null && dimList.size() > 0) {
                dimJsonObj = dimList.get(0);
                // 将查询出来的数据放到Redis中缓存起来
                if (jedis != null) {
                    jedis.setex(redisKey, 3600 * 24, dimJsonObj.toJSONString());
                }
            } else {
                System.out.println("维度数据没有找到:" + sql);
            }
        }

        // 关闭Jedis
        if (jedis != null) {
            jedis.close();
        }

        return dimJsonObj;
    }

    // 根据key让Redis中的缓存失效
    public static void deleteCached(String tableName, String id) {
        String key = "dim:" + tableName.toLowerCase() + ":" + id;
        try {
            Jedis jedis = RedisUtil.getJedis();
            // 通过key清除缓存
            jedis.del(key);
            jedis.close();
        } catch (Exception e) {
            System.out.println("缓存异常！");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // System.out.println(PhoenixUtil.queryList("select * from DIM_BASE_TRADEMARK",
        // JSONObject.class));
        // JSONObject dimInfo = DimUtil.getDimInfoNoCache("DIM_BASE_TRADEMARK", Tuple2.of("id",
        // "14"));

        JSONObject dimInfo = DimUtil.getDimInfo("DIM_BASE_TRADEMARK", "14");

        System.out.println(dimInfo);
    }
}
