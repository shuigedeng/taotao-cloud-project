/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.uc.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资源表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:08:15
 */
@Entity
@Table(name = SysResource.TABLE_NAME)
@TableName(SysResource.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SysResource.TABLE_NAME, comment = "资源表")
public class SysResource extends BaseSuperEntity<Long> {

	public static final String TABLE_NAME = "uc_sys_resource";

	/**
	 * 资源名称
	 */
	@Column(name = "name", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '资源名称'")
	private String name;

	/**
	 * 权限标识
	 */
	@Column(name = "perms", columnDefinition = "varchar(255) comment '权限标识'")
	private String perms;

	/**
	 * 前端path / 即跳转路由
	 */
	@Column(name = "path", columnDefinition = "varchar(255) comment '前端path / 即跳转路由'")
	private String path;

	/**
	 * 菜单组件
	 */
	@Column(name = "component", columnDefinition = "varchar(255) comment '菜单组件'")
	private String component;

	/**
	 * 父菜单ID
	 */
	@Column(name = "parent_id", columnDefinition = "bigint not null default 0 comment '父菜单ID'")
	private Long parentId = 0L;

	/**
	 * 图标
	 */
	@Column(name = "icon", columnDefinition = "varchar(255) comment '图标'")
	private String icon;

	/**
	 * 是否缓存页面: 0:否 1:是 (默认值0)
	 */
	@Column(name = "keep_alive", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否缓存页面: 0:否 1:是 (默认值0)'")
	private Boolean keepAlive = false;

	/**
	 * 是否隐藏路由菜单: 0否,1是（默认值0）
	 */
	@Column(name = "hidden", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否隐藏路由菜单: 0否,1是（默认值0)'")
	private Boolean hidden = false;

	/**
	 * 聚合路由 0否,1是（默认值0）
	 */
	@Column(name = "always_show", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '聚合路由 0否,1是（默认值0)'")
	private Boolean alwaysShow = false;

	/**
	 * 重定向
	 */
	@Column(name = "redirect", columnDefinition = "varchar(255) comment '重定向'")
	private String redirect;

	/**
	 * 是否为外链 0否,1是（默认值0）
	 */
	@Column(name = "is_frame", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 comment '是否为外链 0否,1是（默认值0)'")
	private Boolean isFrame = false;

	/**
	 * 排序值
	 */
	@Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
	private Integer sortNum = 0;

	/**
	 * 资源类型 (1:一级(左侧)菜单 2:二级(顶部)菜单 3：按钮)
	 *
	 * @see com.taotao.cloud.common.enums.ResourceTypeEnum
	 */
	@Column(name = "type", nullable = false, columnDefinition = "tinyint(1) unsigned not null default 1 comment '资源类型 (1:一级(左侧)菜单 2:二级(顶部)菜单 3：按钮)'")
	private byte type = 1;

	/**
	 * 租户id
	 */
	@Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
	private String tenantId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Boolean getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(Boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public Boolean getAlwaysShow() {
		return alwaysShow;
	}

	public void setAlwaysShow(Boolean alwaysShow) {
		this.alwaysShow = alwaysShow;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public Boolean getFrame() {
		return isFrame;
	}

	public void setFrame(Boolean frame) {
		isFrame = frame;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public SysResource() {
	}

	public SysResource(String name, String perms, String path, String component,
		Long parentId, String icon, Boolean keepAlive, Boolean hidden, Boolean alwaysShow,
		String redirect, Boolean isFrame, Integer sortNum, byte type, String tenantId) {
		this.name = name;
		this.perms = perms;
		this.path = path;
		this.component = component;
		this.parentId = parentId;
		this.icon = icon;
		this.keepAlive = keepAlive;
		this.hidden = hidden;
		this.alwaysShow = alwaysShow;
		this.redirect = redirect;
		this.isFrame = isFrame;
		this.sortNum = sortNum;
		this.type = type;
		this.tenantId = tenantId;
	}
}

