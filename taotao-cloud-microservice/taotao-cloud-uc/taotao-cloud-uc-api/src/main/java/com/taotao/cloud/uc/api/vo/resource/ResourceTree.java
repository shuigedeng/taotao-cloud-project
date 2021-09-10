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
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 树形菜单列表
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/21 11:09
 */
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
	private boolean spread = false;

	@Schema(description = "前端path / 即跳转路由")
	private String path;

	@Schema(description = "是否缓存页面: 0:否 1:是 (默认值0)")
	private boolean keepAlive;

	@Schema(description = "资源类型 1：目录 2：菜单 3：按钮")
	private int type;

	@Schema(description = "菜单标签")
	private String label;

	@Schema(description = "排序值")
	private int sort;

	public ResourceTree() {
	}

	public ResourceTree(String icon, String name, String perms, boolean spread, String path,
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

	public ResourceTree(Long id, Long parentId,
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

	public ResourceTree(Long id, String name, Long parentId) {
		super();
		super.setId(id);
		super.setParentId(parentId);
		this.name = name;
		this.label = name;
	}

	public ResourceTree(Long id, String name, ResourceTree parent) {
		super();
		super.setId(id);
		super.setParentId(parent.getId());
		this.name = name;
		this.label = name;
	}

	public ResourceTree(ResourceVO resourceVO) {
		super();
		super.setId(resourceVO.getId());
		super.setParentId(resourceVO.getParentId());
		this.icon = resourceVO.getIcon();
		this.name = resourceVO.getName();
		this.path = resourceVO.getPath();
		this.type = resourceVO.getType();
		this.perms = resourceVO.getPerms();
		this.label = resourceVO.getName();
		this.sort = resourceVO.getSortNum();
		this.keepAlive = resourceVO.getKeepAlive();
	}

	@Override
	public String toString() {
		return "ResourceTree{" +
			"icon='" + icon + '\'' +
			", name='" + name + '\'' +
			", perms='" + perms + '\'' +
			", spread=" + spread +
			", path='" + path + '\'' +
			", keepAlive=" + keepAlive +
			", type=" + type +
			", label='" + label + '\'' +
			", sort=" + sort +
			"} " + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ResourceTree that = (ResourceTree) o;
		return spread == that.spread && Objects.equals(icon, that.icon)
			&& Objects.equals(name, that.name) && Objects.equals(perms,
			that.perms) && Objects.equals(path, that.path) && Objects.equals(
			keepAlive, that.keepAlive) && Objects.equals(type, that.type)
			&& Objects.equals(label, that.label) && Objects.equals(sort,
			that.sort);
	}

	@Override
	public int hashCode() {
		return Objects.hash(icon, name, perms, spread, path, keepAlive, type, label, sort);
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

	public void setType(Byte type) {
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

		public ResourceTree build() {
			ResourceTree resourceTree = new ResourceTree();
			resourceTree.setId(id);
			resourceTree.setParentId(parentId);
			resourceTree.setChildren(children);
			resourceTree.setHasChildren(hasChildren);
			resourceTree.setIcon(icon);
			resourceTree.setName(name);
			resourceTree.setPerms(perms);
			resourceTree.setSpread(spread);
			resourceTree.setPath(path);
			resourceTree.setKeepAlive(keepAlive);
			resourceTree.setType(type);
			resourceTree.setLabel(label);
			resourceTree.setSort(sort);
			return resourceTree;
		}
	}
}
