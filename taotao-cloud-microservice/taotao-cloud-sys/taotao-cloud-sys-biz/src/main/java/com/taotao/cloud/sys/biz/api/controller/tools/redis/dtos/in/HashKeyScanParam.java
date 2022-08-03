package com.taotao.cloud.sys.biz.api.controller.tools.redis.dtos.in;

import lombok.Data;

@Data
public class HashKeyScanParam extends BaseKeyScanParam{
    /**
     *  hash key
     */
    private String key;
    /**
     * 是否扫描所有数据
     */
    private boolean all;
    /**
     * hash 字段列表
     */
    private String [] fields;
}