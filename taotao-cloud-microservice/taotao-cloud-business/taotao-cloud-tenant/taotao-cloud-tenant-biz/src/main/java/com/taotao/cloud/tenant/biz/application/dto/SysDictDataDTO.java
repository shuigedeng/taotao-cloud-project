package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 字典数据新增/修改DTO
 */
@Data
public class SysDictDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典编码（修改时必传）
     */
    private Long dictCode;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 字典排序
     */
    private Integer dictSort;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 样式属性
     */
    private String cssClass;

    /**
     * 表格回显样式
     */
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    private String isDefault;

    /**
     * 上级字典编码
     */
    private Long parentDictCode;

    /**
     * 关联的字典类型
     */
    private String linkedDictType;

    /**
     * 关联的字典值
     */
    private String linkedDictValue;

    /**
     * 字典状态（0-禁用，1-正常）
     */
    private Integer dictStatus;

    /**
     * 备注
     */
    private String remark;
}
