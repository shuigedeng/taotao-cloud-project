package com.taotao.cloud.sys.biz.tools.redis.dtos.in;

import lombok.Data;

@Data
public class BaseKeyScanParam {
    /**
     * keyPattern
     */
    protected String pattern;
    /**
     * 每页搜索数量
     */
    protected int limit;
    /**
     * 搜索游标信息
     */
    protected String cursor;
}
