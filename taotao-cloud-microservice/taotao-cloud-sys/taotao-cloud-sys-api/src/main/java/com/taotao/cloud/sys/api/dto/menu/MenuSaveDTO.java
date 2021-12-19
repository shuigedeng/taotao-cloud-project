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
package com.taotao.cloud.sys.api.dto.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * 菜单添加对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:26:19
 */
@Schema(description = "菜单添加对象")
public record MenuSaveDTO(

	/**
	 * 菜单名称
	 */
	@Schema(description = "菜单名称", required = true)
	@NotBlank(message = "菜单名称不能超过为空")
	@Length(max = 20, message = "菜单名称不能超过20个字符")
	String name,
	/**
	 * 菜单类型 1：目录 2：菜单 3：按钮
	 */
	@Schema(description = "菜单类型 1：目录 2：菜单 3：按钮", required = true)
	@NotBlank(message = "菜单类型不能超过为空")
	//@IntEnums(value = {1, 2, 3})
	Byte type,
	/**
	 * 权限标识
	 */
	@Schema(description = "权限标识")
	String perms,
	/**
	 * 前端path / 即跳转路由
	 */
	@Schema(description = "前端path / 即跳转路由")
	String path,
	/**
	 * 菜单组件
	 */
	@Schema(description = "菜单组件")
	String component,
	/**
	 * 父菜单ID
	 */
	@Schema(description = "父菜单ID")
	Long parentId,
	/**
	 * 图标
	 */
	@Schema(description = "图标")
	String icon,
	/**
	 * 是否缓存页面: 0:否 1:是 (默认值0)
	 */
	@Schema(description = "是否缓存页面: 0:否 1:是 (默认值0)")
	Boolean keepAlive,
	/**
	 * 是否隐藏路由菜单: 0否,1是（默认值0
	 */
	@Schema(description = "是否隐藏路由菜单: 0否,1是（默认值0）")
	Boolean hidden,
	/**
	 * 聚合路由 0否,1是（默认值0）
	 */
	@Schema(description = "聚合路由 0否,1是（默认值0）")
	Boolean alwaysShow,
	/**
	 * 重定向
	 */
	@Schema(description = "重定向")
	String redirect,
	/**
	 * 是否为外链 0否,1是（默认值0）
	 */
	@Schema(description = "是否为外链 0否,1是（默认值0）")
	Boolean isFrame,
	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	Integer sortNum) implements Serializable {

	static final long serialVersionUID = -1972549738577159538L;

}

