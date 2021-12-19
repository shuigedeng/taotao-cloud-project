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
package com.taotao.cloud.sys.api.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema( description = "菜单查询对象")
public record MenuQueryVO(

	/**
	 * id
	 */
	@Schema(description = "id")
	Long id,
	/**
	 * 菜单名称
	 */
	@Schema(description = "菜单名称")
	String name,
	/**
	 * 菜单类型 1：目录 2：菜单 3：按钮
	 */
	@Schema(description = "菜单类型 1：目录 2：菜单 3：按钮")
	int type,
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
	long parentId,
	/**
	 * 图标
	 */
	@Schema(description = "图标")
	String icon,
	/**
	 * 是否缓存页面: 0:否 1:是 (默认值0)
	 */
	@Schema(description = "是否缓存页面: 0:否 1:是 (默认值0)")
	boolean keepAlive,
	/**
	 * 是否隐藏路由菜单: 0否,1是（默认值0）
	 */
	@Schema(description = "是否隐藏路由菜单: 0否,1是（默认值0）")
	boolean hidden,
	/**
	 * 聚合路由 0否,1是（默认值0）
	 */
	@Schema(description = "聚合路由 0否,1是（默认值0）")
	boolean alwaysShow,
	/**
	 * 重定向
	 */
	@Schema(description = "重定向")
	String redirect,
	/**
	 * 是否为外链 0否,1是（默认值0）
	 */
	@Schema(description = "是否为外链 0否,1是（默认值0）")
	boolean isFrame,
	/**
	 * 排序值
	 */
	@Schema(description = "排序ø值")
	int sortNum,
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	LocalDateTime createTime,
	/**
	 * 最后修改时间
	 */
	@Schema(description = "最后修改时间")
	LocalDateTime lastModifiedTime) implements Serializable {

	@Serial
	static final long serialVersionUID = 5126530068827085130L;


}

