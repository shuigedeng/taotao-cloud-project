package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 资源新增/修改DTO
 */
@Data
public class SysResourceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源ID（修改时必传）
     */
    private Long id;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 父级资源ID（0为顶级）
     */
    private Long parentId;

    /**
     * 资源类型（1-目录，2-菜单，3-按钮，4-API接口）
     */
    private Integer resourceType;

    /**
     * 排序（值越小越靠前）
     */
    private Integer sort;

    /**
     * 资源路由（菜单/目录用）
     */
    private String path;

    /**
     * 前端组件路径（菜单用）
     */
    private String component;

    /**
     * 是否外链（0-否，1-是）
     */
    private Integer isExternal;

    /**
     * 是否公开资源（0-否，1-是）
     */
    private Integer isPublic;

    /**
     * 菜单状态（0-隐藏，1-显示）
     */
    private Integer menuStatus;

    /**
     * 显示状态（0-隐藏，1-显示）
     */
    private Integer visible;

    /**
     * 权限标识（如：sys:user:list）
     */
    private String perms;

    /**
     * 图标（菜单/目录用）
     */
    private String icon;

    /**
     * API请求方法（GET/POST/PUT/DELETE）
     */
    private String apiMethod;

    /**
     * API接口地址
     */
    private String apiUrl;

    /**
     * 是否缓存（0-否，1-是）
     */
    private Integer keepAlive;

    /**
     * 是否总是显示（0-否，1-是）
     */
    private Integer alwaysShow;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 备注
     */
    private String remark;
}
