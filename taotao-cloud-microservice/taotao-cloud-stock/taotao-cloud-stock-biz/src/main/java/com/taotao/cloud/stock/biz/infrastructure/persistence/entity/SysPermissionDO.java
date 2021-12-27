package com.taotao.cloud.stock.biz.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xtoon.cloud.common.mybatis.util.BaseDO;
import lombok.Data;

/**
 * 菜单权限DO
 *
 * @author haoxin
 * @date 2021-02-15
 **/
@Data
@TableName("sys_permission")
public class SysPermissionDO extends BaseDO {

    /**
     * 父级ID
     */
    private String parentId;

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
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remarks;

}
