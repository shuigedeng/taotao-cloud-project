package com.taotao.cloud.realtime.mall.app.func;


import com.alibaba.fastjson2.JSONObject;

/**
 *
 * Date: 2021/2/19
 * Desc:  维度关联接口
 */
public interface DimJoinFunction<T> {

    //需要提供一个获取key的方法，但是这个方法如何实现不知道
    String getKey(T obj);

    //流中的事实数据和查询出来的维度数据进行关联
    void join(T obj, JSONObject dimInfoJsonObj) throws Exception;
}
