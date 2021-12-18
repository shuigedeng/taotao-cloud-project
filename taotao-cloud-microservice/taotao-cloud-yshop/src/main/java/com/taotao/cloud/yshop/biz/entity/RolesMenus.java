/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;

import java.io.Serializable;

@TableName("roles_menus")
public class RolesMenus extends SuperEntity<RolesMenus, Long> implements Serializable {

    /** 菜单ID */
    private Long menuId;


    /** 角色ID */
    private Long roleId;

    public void copy(RolesMenus source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
