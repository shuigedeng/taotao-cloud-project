package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 字典类型新增/修改DTO
 */
@Data
public class SysDictTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典主键（修改时必传）
     */
    private Long dictId;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型（租户内唯一）
     */
    private String dictType;

    /**
     * 字典状态（0-禁用，1-正常）
     */
    private Integer dictStatus;

    /**
     * 备注
     */
    private String remark;
}
