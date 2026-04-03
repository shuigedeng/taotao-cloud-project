package com.taotao.cloud.tenant.biz.application.dto;

import com.mdframe.forge.starter.core.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysResourceQuery extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 资源名称（模糊查询）
     */
    private String resourceName;

    /**
     * 父级资源ID
     */
    private Long parentId;

    /**
     * 资源类型（1-目录，2-菜单，3-按钮，4-API接口）
     */
    private Integer resourceType;

    /**
     * 显示状态（0-隐藏，1-显示）
     */
    private Integer visible;
}
