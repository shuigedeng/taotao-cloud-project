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
package com.taotao.cloud.sys.api.bo.resource;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:27:42
 */
public record ResourceBO(
	/**
	 * id
	 */
	Long id,
	/**
	 * 资源名称
	 */
	String name,
	/**
	 * 资源类型 1：目录 2：菜单 3：按钮
	 */
	int type,
	/**
	 * 权限标识
	 */
	String perms,
	/**
	 * 前端path / 即跳转路由
	 */
	String path,
	/**
	 * 菜单组件
	 */
	String component,
	/**
	 * 父菜单ID
	 */
	long parentId,
	/**
	 * 图标
	 */
	String icon,
	/**
	 * 是否缓存页面: 0:否 1:是 (默认值0)
	 */
	boolean keepAlive,
	/**
	 * 是否隐藏路由菜单: 0否,1是（默认值0）
	 */
	boolean hidden,
	/**
	 * 聚合路由 0否,1是（默认值0）
	 */
	boolean alwaysShow,
	/**
	 * 重定向
	 */
	String redirect,
	/**
	 * 是否为外链 0否,1是（默认值0）
	 */
	boolean isFrame,
	/**
	 * 排序值
	 */
	int sortNum,
	/**
	 * 创建时间
	 */
	LocalDateTime createTime,
	/**
	 * 最后修改时间
	 */
	LocalDateTime lastModifiedTime) implements Serializable {

	static final long serialVersionUID = 5126530068827085130L;


}

