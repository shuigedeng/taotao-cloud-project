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
import lombok.Builder;
import lombok.Data;

/**
 * 菜单查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:27:42
 */
@Data
@Builder
@Schema( description = "菜单查询对象")
public class MenuQueryVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4132785717179910025L;

	/**
	 * id
	 */
	@Schema(description = "id")
	private Long id;
	/**
	 * 菜单名称
	 */
	@Schema(description = "菜单名称")
	private String name;
	/**
	 * 菜单类型 1：目录 2：菜单 3：按钮
	 */
	@Schema(description = "菜单类型 1：目录 2：菜单 3：按钮")
	private Integer type;
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
	 * 是否隐藏路由菜单: 0否;1是（默认值0）
	 */
	@Schema(description = "是否隐藏路由菜单: 0否;1是（默认值0）")
	private Boolean hidden;
	/**
	 * 聚合路由 0否;1是（默认值0）
	 */
	@Schema(description = "聚合路由 0否;1是（默认值0）")
	private Boolean alwaysShow;
	/**
	 * 重定向
	 */
	@Schema(description = "重定向")
	private String redirect;
	/**
	 * 是否为外链 0否;1是（默认值0）
	 */
	@Schema(description = "是否为外链 0否;1是（默认值0）")
	private Boolean isFrame;
	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer sortNum;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 最后修改时间
	 */
	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;
}

