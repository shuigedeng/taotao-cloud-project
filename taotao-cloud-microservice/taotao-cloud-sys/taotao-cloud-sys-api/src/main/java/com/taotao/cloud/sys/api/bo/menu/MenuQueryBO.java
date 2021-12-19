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
package com.taotao.cloud.sys.api.bo.menu;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:27:42
 */
public class MenuQueryBO implements Serializable {

	@Serial
	private static final long serialVersionUID = 5126530068827085130L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 菜单名称
	 */
	private String name;
	/**
	 * 菜单类型 1：目录 2：菜单 3：按钮
	 */
	private int type;
	/**
	 * 权限标识
	 */
	private String perms;
	/**
	 * 前端path / 即跳转路由
	 */
	private String path;
	/**
	 * 菜单组件
	 */
	private String component;
	/**
	 * 父菜单ID
	 */
	private long parentId;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 是否缓存页面: 0:否 1:是 (默认值0)
	 */
	private boolean keepAlive;
	/**
	 * 是否隐藏路由菜单: 0否,1是（默认值0）
	 */
	private boolean hidden;
	/**
	 * 聚合路由 0否,1是（默认值0）
	 */
	private boolean alwaysShow;
	/**
	 * 重定向
	 */
	private String redirect;
	/**
	 * 是否为外链 0否,1是（默认值0）
	 */
	private boolean isFrame;
	/**
	 * 排序值
	 */
	private int sortNum;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 最后修改时间
	 */
	private LocalDateTime lastModifiedTime;

	public MenuQueryBO() {
	}

	public MenuQueryBO(long id, String name, int type, String perms, String path,
		String component, long parentId, String icon, boolean keepAlive, boolean hidden,
		boolean alwaysShow, String redirect, boolean isFrame, int sortNum,
		LocalDateTime createTime, LocalDateTime lastModifiedTime) {
		this.id = id;
		this.name = name;
		this.type = type;
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
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isAlwaysShow() {
		return alwaysShow;
	}

	public void setAlwaysShow(boolean alwaysShow) {
		this.alwaysShow = alwaysShow;
	}

	public boolean isFrame() {
		return isFrame;
	}

	public void setFrame(boolean frame) {
		isFrame = frame;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
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

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
}

