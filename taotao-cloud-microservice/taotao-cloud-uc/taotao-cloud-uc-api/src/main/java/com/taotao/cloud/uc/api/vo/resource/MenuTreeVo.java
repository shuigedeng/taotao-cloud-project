package com.taotao.cloud.uc.api.vo.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;


/**
 * 菜单树DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "MenuTreeVo", description = "菜单树VO")
public class MenuTreeVo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的key")
	private Long key;

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的value")
	private String value;

	@Schema(description = "对应depart_name字段,前端数据树中的title")
	private String title;

	@Schema(description = "菜单ID")
	private Integer menuId;

	@Schema(description = "菜单名称")
	private String name;

	@Schema(description = "父菜单ID")
	private Integer parentMenuId;

	@Schema(description = " 菜单类型 （类型   0：目录   1：菜单   2：按钮）")
	private Integer type;

	@Schema(description = "排序")
	private Integer sort;

	@Schema(description = "父菜单名称")
	private String parentName;

	@Schema(description = "菜单等级")
	private Integer level;

	@Schema(description = "children")
	private List<MenuTreeVo> children;

	public MenuTreeVo() {
	}

	public MenuTreeVo(Long key, String value, String title, Integer menuId, String name,
		Integer parentMenuId, Integer type, Integer sort, String parentName,
		Integer level, List<MenuTreeVo> children) {
		this.key = key;
		this.value = value;
		this.title = title;
		this.menuId = menuId;
		this.name = name;
		this.parentMenuId = parentMenuId;
		this.type = type;
		this.sort = sort;
		this.parentName = parentName;
		this.level = level;
		this.children = children;
	}

	@Override
	public String toString() {
		return "MenuTreeVo{" +
			"key=" + key +
			", value='" + value + '\'' +
			", title='" + title + '\'' +
			", menuId=" + menuId +
			", name='" + name + '\'' +
			", parentMenuId=" + parentMenuId +
			", type=" + type +
			", sort=" + sort +
			", parentName='" + parentName + '\'' +
			", level=" + level +
			", children=" + children +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MenuTreeVo that = (MenuTreeVo) o;
		return Objects.equals(key, that.key) && Objects.equals(value, that.value)
			&& Objects.equals(title, that.title) && Objects.equals(menuId,
			that.menuId) && Objects.equals(name, that.name) && Objects.equals(
			parentMenuId, that.parentMenuId) && Objects.equals(type, that.type)
			&& Objects.equals(sort, that.sort) && Objects.equals(parentName,
			that.parentName) && Objects.equals(level, that.level)
			&& Objects.equals(children, that.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value, title, menuId, name, parentMenuId, type, sort, parentName,
			level, children);
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(Integer parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<MenuTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<MenuTreeVo> children) {
		this.children = children;
	}

	public static MenuTreeVoBuilder builder() {
		return new MenuTreeVoBuilder();
	}


	public static final class MenuTreeVoBuilder {

		private Long key;
		private String value;
		private String title;
		private Integer menuId;
		private String name;
		private Integer parentMenuId;
		private Integer type;
		private Integer sort;
		private String parentName;
		private Integer level;
		private List<MenuTreeVo> children;

		private MenuTreeVoBuilder() {
		}

		public static MenuTreeVoBuilder aMenuTreeVo() {
			return new MenuTreeVoBuilder();
		}

		public MenuTreeVoBuilder key(Long key) {
			this.key = key;
			return this;
		}

		public MenuTreeVoBuilder value(String value) {
			this.value = value;
			return this;
		}

		public MenuTreeVoBuilder title(String title) {
			this.title = title;
			return this;
		}

		public MenuTreeVoBuilder menuId(Integer menuId) {
			this.menuId = menuId;
			return this;
		}

		public MenuTreeVoBuilder name(String name) {
			this.name = name;
			return this;
		}

		public MenuTreeVoBuilder parentMenuId(Integer parentMenuId) {
			this.parentMenuId = parentMenuId;
			return this;
		}

		public MenuTreeVoBuilder type(Integer type) {
			this.type = type;
			return this;
		}

		public MenuTreeVoBuilder sort(Integer sort) {
			this.sort = sort;
			return this;
		}

		public MenuTreeVoBuilder parentName(String parentName) {
			this.parentName = parentName;
			return this;
		}

		public MenuTreeVoBuilder level(Integer level) {
			this.level = level;
			return this;
		}

		public MenuTreeVoBuilder children(List<MenuTreeVo> children) {
			this.children = children;
			return this;
		}

		public MenuTreeVo build() {
			MenuTreeVo menuTreeVo = new MenuTreeVo();
			menuTreeVo.setKey(key);
			menuTreeVo.setValue(value);
			menuTreeVo.setTitle(title);
			menuTreeVo.setMenuId(menuId);
			menuTreeVo.setName(name);
			menuTreeVo.setParentMenuId(parentMenuId);
			menuTreeVo.setType(type);
			menuTreeVo.setSort(sort);
			menuTreeVo.setParentName(parentName);
			menuTreeVo.setLevel(level);
			menuTreeVo.setChildren(children);
			return menuTreeVo;
		}
	}
}
