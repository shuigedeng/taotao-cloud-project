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

import com.taotao.cloud.common.tree.INode;
import com.taotao.cloud.common.tree.MapperNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 树形菜单列表
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/21 11:09
 */
@Schema(name = "MenuTreeVO", description = "树形菜单列表")
public class MenuTreeVO extends MapperNode implements Serializable {

	@Serial
	private static final long serialVersionUID = -5853343562172855421L;

	@Schema(description = "图标")
	private String icon;

	@Schema(description = "菜单名称")
	private String name;

	@Schema(description = "权限标识")
	private String perms;

	@Schema(description = "spread")
	private Boolean spread = false;

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

	private String component;

	private Boolean hidden;

	private String redirect;

	private Boolean alwaysShow;

	private Boolean target;

	private String typeName;

	private LocalDateTime createTime;

	private MenuMetaVO meta;

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

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public MenuMetaVO getMeta() {
		return meta;
	}

	public void setMeta(MenuMetaVO meta) {
		this.meta = meta;
	}

	public Boolean getSpread() {
		return spread;
	}

	public void setSpread(Boolean spread) {
		this.spread = spread;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public Boolean getAlwaysShow() {
		return alwaysShow;
	}

	public void setAlwaysShow(Boolean alwaysShow) {
		this.alwaysShow = alwaysShow;
	}

	public Boolean getTarget() {
		return target;
	}

	public void setTarget(Boolean target) {
		this.target = target;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public static MenuTreeVOBuilder builder() {
		return new MenuTreeVOBuilder();
	}

	public static final class MenuTreeVOBuilder {

		protected Long id;
		protected Long parentId;
		protected List<INode> children = new ArrayList<>();
		private String title;
		private Long key;
		private Long value;
		private Boolean hasChildren;
		private String icon;
		private String name;
		private String perms;
		private Boolean spread = false;
		private String path;
		private boolean keepAlive;
		private int type;
		private String label;
		private int sort;
		private String component;
		private Boolean hidden;
		private String redirect;
		private Boolean alwaysShow;
		private Boolean target;
		private String typeName;
		private LocalDateTime createTime;
		private MenuMetaVO meta;

		private MenuTreeVOBuilder() {
		}

		public MenuTreeVOBuilder title(String title) {
			this.title = title;
			return this;
		}

		public MenuTreeVOBuilder key(Long key) {
			this.key = key;
			return this;
		}

		public MenuTreeVOBuilder value(Long value) {
			this.value = value;
			return this;
		}

		public MenuTreeVOBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public MenuTreeVOBuilder parentId(Long parentId) {
			this.parentId = parentId;
			return this;
		}

		public MenuTreeVOBuilder children(List<INode> children) {
			this.children = children;
			return this;
		}

		public MenuTreeVOBuilder hasChildren(Boolean hasChildren) {
			this.hasChildren = hasChildren;
			return this;
		}

		public MenuTreeVOBuilder icon(String icon) {
			this.icon = icon;
			return this;
		}

		public MenuTreeVOBuilder name(String name) {
			this.name = name;
			return this;
		}

		public MenuTreeVOBuilder perms(String perms) {
			this.perms = perms;
			return this;
		}

		public MenuTreeVOBuilder spread(Boolean spread) {
			this.spread = spread;
			return this;
		}

		public MenuTreeVOBuilder path(String path) {
			this.path = path;
			return this;
		}

		public MenuTreeVOBuilder keepAlive(boolean keepAlive) {
			this.keepAlive = keepAlive;
			return this;
		}

		public MenuTreeVOBuilder type(int type) {
			this.type = type;
			return this;
		}

		public MenuTreeVOBuilder label(String label) {
			this.label = label;
			return this;
		}

		public MenuTreeVOBuilder sort(int sort) {
			this.sort = sort;
			return this;
		}

		public MenuTreeVOBuilder component(String component) {
			this.component = component;
			return this;
		}

		public MenuTreeVOBuilder hidden(Boolean hidden) {
			this.hidden = hidden;
			return this;
		}

		public MenuTreeVOBuilder redirect(String redirect) {
			this.redirect = redirect;
			return this;
		}

		public MenuTreeVOBuilder alwaysShow(Boolean alwaysShow) {
			this.alwaysShow = alwaysShow;
			return this;
		}

		public MenuTreeVOBuilder target(Boolean target) {
			this.target = target;
			return this;
		}

		public MenuTreeVOBuilder typeName(String typeName) {
			this.typeName = typeName;
			return this;
		}

		public MenuTreeVOBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public MenuTreeVOBuilder meta(MenuMetaVO meta) {
			this.meta = meta;
			return this;
		}

		public MenuTreeVO build() {
			MenuTreeVO menuTreeVO = new MenuTreeVO();
			menuTreeVO.setTitle(title);
			menuTreeVO.setKey(key);
			menuTreeVO.setValue(value);
			menuTreeVO.setId(id);
			menuTreeVO.setParentId(parentId);
			menuTreeVO.setChildren(children);
			menuTreeVO.setHasChildren(hasChildren);
			menuTreeVO.setIcon(icon);
			menuTreeVO.setName(name);
			menuTreeVO.setPerms(perms);
			menuTreeVO.setSpread(spread);
			menuTreeVO.setPath(path);
			menuTreeVO.setKeepAlive(keepAlive);
			menuTreeVO.setType(type);
			menuTreeVO.setLabel(label);
			menuTreeVO.setSort(sort);
			menuTreeVO.setComponent(component);
			menuTreeVO.setHidden(hidden);
			menuTreeVO.setRedirect(redirect);
			menuTreeVO.setAlwaysShow(alwaysShow);
			menuTreeVO.setTarget(target);
			menuTreeVO.setTypeName(typeName);
			menuTreeVO.setCreateTime(createTime);
			menuTreeVO.setMeta(meta);
			return menuTreeVO;
		}
	}
}
