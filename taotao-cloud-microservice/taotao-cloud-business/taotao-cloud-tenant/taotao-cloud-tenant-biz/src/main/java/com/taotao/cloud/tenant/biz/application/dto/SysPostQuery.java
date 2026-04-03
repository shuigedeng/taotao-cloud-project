package com.taotao.cloud.tenant.biz.application.dto;

import com.mdframe.forge.starter.core.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPostQuery extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 所属组织ID
     */
    private Long orgId;

    /**
     * 岗位名称（模糊查询）
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
}
