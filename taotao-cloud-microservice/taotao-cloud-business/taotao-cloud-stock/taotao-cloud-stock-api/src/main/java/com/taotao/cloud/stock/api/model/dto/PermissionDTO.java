package com.taotao.cloud.stock.api.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 权限DTO
 *
 * @author shuigedeng
 * @date 2021-02-17
 */
@Data
public class PermissionDTO implements Serializable {

    public PermissionDTO() {
    }

    public PermissionDTO(String id, String parentId, String permissionName, String permissionType, String permissionLevel, String permissionCodes, String menuIcon, int orderNum, String menuUrl) {
        this.id = id;
        this.parentId = parentId;
        this.permissionName = permissionName;
        this.permissionType = permissionType;
        this.permissionLevel = permissionLevel;
        this.permissionCodes = permissionCodes;
        this.menuIcon = menuIcon;
        this.orderNum = orderNum;
        this.menuUrl = menuUrl;
    }

    /**
     * id
     */
    private String id;

    /**
     * 父级ID
     */
    private String parentId;

    /**
     * 父级名称
     */
    private String parentName;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限类型
     */
    private String permissionType;

    /**
     * 权限级别
     */
    private String permissionLevel;

    /**
     * 权限编码
     */
    private String permissionCodes;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 排序
     */
    private int orderNum;

    /**
     * 菜单url
     */
    private String menuUrl;

    /**
     * ztree属性
     */
    private Boolean open;

    /**
     * 下级权限
     */
    private List<?> subList;
}
