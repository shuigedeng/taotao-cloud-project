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
package com.taotao.cloud.uc.api.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * 资源更新对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:26:19
 */
@Schema(name = "ResourceUpdateDTO", description = "资源更新对象")
public class ResourceUpdateDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -1972549738577159538L;

	/**
	 * 资源名称
	 */
	@Schema(description = "资源名称", required = true)
	@NotBlank(message = "资源名称不能超过为空")
	@Length(max = 20, message = "资源名称不能超过20个字符")
	private String name;
	/**
	 * 资源类型 1：目录 2：菜单 3：按钮
	 */
	@Schema(description = "资源类型 1：目录 2：菜单 3：按钮", required = true)
	@NotBlank(message = "资源类型不能超过为空")
	//@IntEnums(value = {1, 2, 3})
	private Byte type;
	/**
	 * 权限标识
	 */
	@Schema(description = "权限标识")
	private String perms;
	/**
	 * 前端path / 即跳转路由
	 */
	@Schema(description = "前端path / 即跳转路由")
	private String path;
	/**
	 * 菜单组件
	 */
	@Schema(description = "菜单组件")
	private String component;
	/**
	 * 父菜单ID
	 */
	@Schema(description = "父菜单ID")
	private Long parentId;
	/**
	 * 图标
	 */
	@Schema(description = "图标")
	private String icon;
	/**
	 * 是否缓存页面: 0:否 1:是 (默认值0)
	 */
	@Schema(description = "是否缓存页面: 0:否 1:是 (默认值0)")
	private Boolean keepAlive;
	/**
	 * 是否隐藏路由菜单: 0否,1是（默认值0
	 */
	@Schema(description = "是否隐藏路由菜单: 0否,1是（默认值0）")
	private Boolean hidden;
	/**
	 * 聚合路由 0否,1是（默认值0）
	 */
	@Schema(description = "聚合路由 0否,1是（默认值0）")
	private Boolean alwaysShow;
	/**
	 * 重定向
	 */
	@Schema(description = "重定向")
	private String redirect;
	/**
	 * 是否为外链 0否,1是（默认值0）
	 */
	@Schema(description = "是否为外链 0否,1是（默认值0）")
	private Boolean isFrame;
	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sortNum;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
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

	public ResourceUpdateDTO() {
	}

	public ResourceUpdateDTO(String name, Byte type, String perms, String path, String component,
		Long parentId, String icon, Boolean keepAlive, Boolean hidden, Boolean alwaysShow,
		String redirect, Boolean isFrame, Integer sortNum) {
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
	}

}

