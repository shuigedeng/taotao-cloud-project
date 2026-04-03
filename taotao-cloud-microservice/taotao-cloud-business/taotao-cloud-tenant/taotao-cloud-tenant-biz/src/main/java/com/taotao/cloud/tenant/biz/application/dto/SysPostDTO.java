package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 岗位新增/修改DTO
 */
@Data
public class SysPostDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID（修改时必传）
     */
    private Long id;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 岗位编码（租户内唯一）
     */
    private String postCode;

    /**
     * 所属组织ID
     */
    private Long orgId;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 岗位状态（0-禁用，1-正常）
     */
    private Integer postStatus;

    /**
     * 岗位类型（1-管理岗，2-技术岗，3-业务岗，4-其他）
     */
    private Integer postType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;
}
