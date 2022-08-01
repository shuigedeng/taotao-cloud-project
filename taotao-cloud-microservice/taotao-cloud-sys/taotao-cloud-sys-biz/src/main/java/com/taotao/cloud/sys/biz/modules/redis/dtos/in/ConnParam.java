package com.taotao.cloud.sys.biz.modules.redis.dtos.in;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ConnParam {
    /**
     * 连接名称
     */
    @NotNull
    private String connName;
    /**
     * 数据库索引号
     */
    private int index;
}
