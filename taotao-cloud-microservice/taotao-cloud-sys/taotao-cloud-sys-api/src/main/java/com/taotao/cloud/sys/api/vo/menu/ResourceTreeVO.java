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
package com.taotao.cloud.sys.api.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 树形菜单列表
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/21 11:09
 */
@Schema(name = "ResourceTree", description = "树形菜单列表")
public class ResourceTreeVO extends TreeNode implements Serializable {

	@Serial
	private static final long serialVersionUID = -5853343562172855421L;

	@Schema(description = "图标")
	private String icon;

	@Schema(description = "菜单名称")
	private String name;

	@Schema(description = "权限标识")
	private String perms;

	@Schema(description = "spread")
	private boolean spread = false;

	@Schema(description = "前端path / 即跳转路由")
	private String path;

	@Schema(description = "是否缓存页面: 0:否 1:是 (默认值0)")
	private boolean keepAlive;

	@Schema(description = "菜单类型 1：目录 2：菜单 3：按钮")
	private int type;

	@Schema(description = "菜单标签")
	private String label;

	@Schema(description = "排序值")
	private int sort;

	public ResourceTreeVO() {
	}

	public ResourceTreeVO(String icon, String name, String perms, boolean spread, String path,
		Boolean keepAlive, Byte type, String label, Integer sort) {
		this.icon = icon;
		this.name = name;
		this.perms = perms;
		this.spread = spread;
		this.path = path;
		this.keepAlive = keepAlive;
		this.type = type;
		this.label = label;
		this.sort = sort;
	}

	public ResourceTreeVO(Long id, Long parentId,
		List<TreeNode> children, Boolean hasChildren, String icon, String name,
		String perms, boolean spread, String path, Boolean keepAlive, Byte type,
		String label, Integer sort) {
		super(id, parentId, children, hasChildren);
		this.icon = icon;
		this.name = name;
		this.perms = perms;
		this.spread = spread;
		this.path = path;
		this.keepAlive = keepAlive;
		this.type = type;
		this.label = label;
		this.sort = sort;
	}

	public ResourceTreeVO(Long id, String name, Long parentId) {
		super();
		super.setId(id);
		super.setParentId(parentId);
		this.name = name;
		this.label = name;
	}

	public ResourceTreeVO(Long id, String name, ResourceTreeVO parent) {
		super();
		super.setId(id);
		super.setParentId(parent.getId());
		this.name = name;
		this.label = name;
	}

	public ResourceTreeVO(MenuQueryVO menuQueryVO) {
		super();
		super.setId(menuQueryVO.id());
		super.setParentId(menuQueryVO.parentId());
		this.icon = menuQueryVO.icon();
		this.name = menuQueryVO.name();
		this.path = menuQueryVO.path();
		this.type = menuQueryVO.type();
		this.perms = menuQueryVO.perms();
		this.label = menuQueryVO.name();
		this.sort = menuQueryVO.sortNum();
		this.keepAlive = menuQueryVO.keepAlive();
	}


	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

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

	public boolean isSpread() {
		return spread;
	}

	public void setSpread(boolean spread) {
		this.spread = spread;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Boolean getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(Boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public static ResourceTreeBuilder builder() {
		return new ResourceTreeBuilder();
	}

	public static final class ResourceTreeBuilder {

		private Long id;
		private Long parentId;
		private List<TreeNode> children;
		private Boolean hasChildren = false;
		private String icon;
		private String name;
		private String perms;
		private boolean spread = false;
		private String path;
		private Boolean keepAlive;
		private Byte type;
		private String label;
		private Integer sort;

		private ResourceTreeBuilder() {
		}

		public static ResourceTreeBuilder aResourceTree() {
			return new ResourceTreeBuilder();
		}

		public ResourceTreeBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public ResourceTreeBuilder parentId(Long parentId) {
			this.parentId = parentId;
			return this;
		}

		public ResourceTreeBuilder children(List<TreeNode> children) {
			this.children = children;
			return this;
		}

		public ResourceTreeBuilder hasChildren(Boolean hasChildren) {
			this.hasChildren = hasChildren;
			return this;
		}

		public ResourceTreeBuilder icon(String icon) {
			this.icon = icon;
			return this;
		}

		public ResourceTreeBuilder name(String name) {
			this.name = name;
			return this;
		}

		public ResourceTreeBuilder perms(String perms) {
			this.perms = perms;
			return this;
		}

		public ResourceTreeBuilder spread(boolean spread) {
			this.spread = spread;
			return this;
		}

		public ResourceTreeBuilder path(String path) {
			this.path = path;
			return this;
		}

		public ResourceTreeBuilder keepAlive(Boolean keepAlive) {
			this.keepAlive = keepAlive;
			return this;
		}

		public ResourceTreeBuilder type(Byte type) {
			this.type = type;
			return this;
		}

		public ResourceTreeBuilder label(String label) {
			this.label = label;
			return this;
		}

		public ResourceTreeBuilder sort(Integer sort) {
			this.sort = sort;
			return this;
		}

		public ResourceTreeVO build() {
			ResourceTreeVO resourceTreeVO = new ResourceTreeVO();
			resourceTreeVO.setId(id);
			resourceTreeVO.setParentId(parentId);
			resourceTreeVO.setChildren(children);
			resourceTreeVO.setHasChildren(hasChildren);
			resourceTreeVO.setIcon(icon);
			resourceTreeVO.setName(name);
			resourceTreeVO.setPerms(perms);
			resourceTreeVO.setSpread(spread);
			resourceTreeVO.setPath(path);
			resourceTreeVO.setKeepAlive(keepAlive);
			resourceTreeVO.setType(type);
			resourceTreeVO.setLabel(label);
			resourceTreeVO.setSort(sort);
			return resourceTreeVO;
		}
	}
}
