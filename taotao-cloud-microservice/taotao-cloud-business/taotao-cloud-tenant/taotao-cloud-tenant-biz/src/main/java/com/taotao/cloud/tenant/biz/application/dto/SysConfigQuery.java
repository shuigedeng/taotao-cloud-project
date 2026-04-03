package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置查询DTO
 */
@Data
public class SysConfigQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 参数名称（模糊查询）
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;
}
