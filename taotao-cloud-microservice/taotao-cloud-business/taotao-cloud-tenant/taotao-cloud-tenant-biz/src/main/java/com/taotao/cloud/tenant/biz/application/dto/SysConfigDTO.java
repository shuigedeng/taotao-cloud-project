package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置新增/修改DTO
 */
@Data
public class SysConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 参数主键（修改时必传）
     */
    private Long configId;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;

    /**
     * 参数描述
     */
    private String configDesc;

    /**
     * 排序
     */
    private Integer sort;
}
