package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 字典数据查询DTO
 */
@Data
public class SysDictDataQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 字典标签（模糊查询）
     */
    private String dictLabel;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典状态（0-禁用，1-正常）
     */
    private Integer dictStatus;
}
