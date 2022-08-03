package com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.in;

import lombok.Data;

@Data
public class SerializerParam {
    /**
     * key序列化
     */
    private String keySerializer;
    /**
     * 值序列化
     */
    private String value;
    /**
     * hashKey 序列化
     */
    private String hashKey;
    /**
     * hash 值序列化
     */
    private String hashValue;
    /**
     * 类加载器名称
     */
    private String classloaderName;
}
