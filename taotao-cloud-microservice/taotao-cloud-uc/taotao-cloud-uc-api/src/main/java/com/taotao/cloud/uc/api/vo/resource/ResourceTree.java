/*
 * Copyright (c) 2020 taotao cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.uc.api.vo.resource;

import com.taotao.cloud.uc.api.vo.TreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 树形菜单列表
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/10/21 11:09
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name = "ResourceTree", description = "树形菜单列表")
public class ResourceTree extends TreeNode implements Serializable {

	private static final long serialVersionUID = -5853343562172855421L;

	@Schema(description = "图标")
	private String icon;

	@Schema(description = "资源名称")
	private String name;

	@Schema(description = "权限标识")
	private String perms;

	@Schema(description = "spread")
	@Builder.Default
	private boolean spread = false;

	@Schema(description = "前端path / 即跳转路由")
	private String path;

	@Schema(description = "是否缓存页面: 0:否 1:是 (默认值0)")
	private Boolean keepAlive;

	@Schema(description = "资源类型 1：目录 2：菜单 3：按钮")
	private Byte type;

	@Schema(description = "菜单标签")
	private String label;

	@Schema(description = "排序值")
	private Integer sort;

	public ResourceTree() {
	}

	public ResourceTree(Long id, String name, Long parentId) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.label = name;
	}

	public ResourceTree(Long id, String name, ResourceTree parent) {
		this.id = id;
		this.parentId = parent.getId();
		this.name = name;
		this.label = name;
	}

	public ResourceTree(ResourceVO resourceVO) {
		this.id = resourceVO.getId();
		this.parentId = resourceVO.getParentId();
		this.icon = resourceVO.getIcon();
		this.name = resourceVO.getName();
		this.path = resourceVO.getPath();
		this.type = resourceVO.getType();
		this.perms = resourceVO.getPerms();
		this.label = resourceVO.getName();
		this.sort = resourceVO.getSortNum();
		this.keepAlive = resourceVO.getKeepAlive();
	}

}
