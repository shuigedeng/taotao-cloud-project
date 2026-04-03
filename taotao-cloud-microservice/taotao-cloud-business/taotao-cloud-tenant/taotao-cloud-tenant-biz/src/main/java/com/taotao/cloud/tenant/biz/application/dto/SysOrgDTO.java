package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 组织新增/修改DTO
 */
@Data
public class SysOrgDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织ID（修改时必传）
     */
    private Long id;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 父级组织ID（0为顶级）
     */
    private Long parentId;

    /**
     * 祖级编码（逗号分隔，如：1,2,3）
     */
    private String ancestors;

    /**
     * 排序（值越小越靠前）
     */
    private Integer sort;

    /**
     * 组织类型（1-公司，2-部门，3-小组）
     */
    private Integer orgType;

    /**
     * 组织状态（0-禁用，1-正常）
     */
    private Integer orgStatus;

    /**
     * 负责人ID（关联sys_user.id）
     */
    private Long leaderId;

    /**
     * 负责人姓名
     */
    private String leaderName;

    /**
     * 组织联系电话
     */
    private String phone;

    /**
     * 组织地址
     */
    private String address;
    
    /**
     * 行政区划编码
     */
    private String regionCode;

    /**
     * 备注
     */
    private String remark;
}
