package com.taotao.cloud.tenant.biz.application.dto.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户资源树VO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResourceTreeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    private Long id;

    /**
     * 父级资源ID
     */
    private Long parentId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源类型（1-目录，2-菜单，3-按钮，4-API接口）
     */
    private Integer resourceType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 资源路由
     */
    private String path;

    /**
     * 前端组件路径
     */
    private String component;

    /**
     * 是否外链（0-否，1-是）
     */
    private Integer isExternal;

    /**
     * 菜单状态（0-隐藏，1-显示）
     */
    private Integer menuStatus;

    /**
     * 显示状态（0-隐藏，1-显示）
     */
    private Integer visible;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 图标
     */
    private String icon;

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
     * API请求方法（GET/POST/PUT/DELETE）
     */
    private String apiMethod;

    /**
     * API接口地址
     */
    private String apiUrl;

    /**
     * 子资源列表
     */
    private List<UserResourceTreeVO> children;
}
