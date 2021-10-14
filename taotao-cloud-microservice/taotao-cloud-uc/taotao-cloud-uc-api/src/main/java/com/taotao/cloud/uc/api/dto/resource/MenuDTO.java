package com.taotao.cloud.uc.api.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * shuigedeng
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Schema(name = "MenuDTO", description = "菜单DTO")
public class MenuDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "菜单DTO")
	private Integer menuId;

	@Schema(description = "菜单名称")
	private String name;

	@Schema(description = "菜单权限")
	private String perms;

	@Schema(description = "菜单路径")
	private String path;

	@Schema(description = "菜单isFrame")
	private Boolean isFrame;

	@Schema(description = "父菜单id")
	private Integer parentId;

	@Schema(description = "菜单组件名称")
	private String component;

	@Schema(description = "菜单icon")
	private String icon;

	@Schema(description = "菜单排序")
	private Integer sort;

	@Schema(description = "菜单类型")
	private Integer type;

	@Schema(description = "菜单删除标识")
	private String delFlag;

	@Schema(description = "菜单keepAlive")
	private Boolean keepAlive;

	@Schema(description = "菜单是否隐藏")
	private Boolean hidden;

	@Schema(description = "菜单是否一直展示")
	private Boolean alwaysShow;

	@Schema(description = "菜单redirect")
	private String redirect;

	@Override
	public String toString() {
		return "MenuDTO{" +
			"menuId=" + menuId +
			", name='" + name + '\'' +
			", perms='" + perms + '\'' +
			", path='" + path + '\'' +
			", isFrame=" + isFrame +
			", parentId=" + parentId +
			", component='" + component + '\'' +
			", icon='" + icon + '\'' +
			", sort=" + sort +
			", type=" + type +
			", delFlag='" + delFlag + '\'' +
			", keepAlive=" + keepAlive +
			", hidden=" + hidden +
			", alwaysShow=" + alwaysShow +
			", redirect='" + redirect + '\'' +
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
		MenuDTO menuDTO = (MenuDTO) o;
		return Objects.equals(menuId, menuDTO.menuId) && Objects.equals(name,
			menuDTO.name) && Objects.equals(perms, menuDTO.perms)
			&& Objects.equals(path, menuDTO.path) && Objects.equals(isFrame,
			menuDTO.isFrame) && Objects.equals(parentId, menuDTO.parentId)
			&& Objects.equals(component, menuDTO.component) && Objects.equals(
			icon, menuDTO.icon) && Objects.equals(sort, menuDTO.sort)
			&& Objects.equals(type, menuDTO.type) && Objects.equals(delFlag,
			menuDTO.delFlag) && Objects.equals(keepAlive, menuDTO.keepAlive)
			&& Objects.equals(hidden, menuDTO.hidden) && Objects.equals(
			alwaysShow, menuDTO.alwaysShow) && Objects.equals(redirect, menuDTO.redirect);
	}

	@Override
	public int hashCode() {
		return Objects.hash(menuId, name, perms, path, isFrame, parentId, component, icon, sort,
			type,
			delFlag, keepAlive, hidden, alwaysShow, redirect);
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

	public Boolean getFrame() {
		return isFrame;
	}

	public void setFrame(Boolean frame) {
		isFrame = frame;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
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

	public MenuDTO(){}

	public MenuDTO(Integer menuId, String name, String perms, String path, Boolean isFrame,
		Integer parentId, String component, String icon, Integer sort, Integer type,
		String delFlag, Boolean keepAlive, Boolean hidden, Boolean alwaysShow,
		String redirect) {
		this.menuId = menuId;
		this.name = name;
		this.perms = perms;
		this.path = path;
		this.isFrame = isFrame;
		this.parentId = parentId;
		this.component = component;
		this.icon = icon;
		this.sort = sort;
		this.type = type;
		this.delFlag = delFlag;
		this.keepAlive = keepAlive;
		this.hidden = hidden;
		this.alwaysShow = alwaysShow;
		this.redirect = redirect;
	}
	public static MenuDTOBuilder builder() {
		return new MenuDTOBuilder();
	}

	public static final class MenuDTOBuilder {

		private Integer menuId;
		private String name;
		private String perms;
		private String path;
		private Boolean isFrame;
		private Integer parentId;
		private String component;
		private String icon;
		private Integer sort;
		private Integer type;
		private String delFlag;
		private Boolean keepAlive;
		private Boolean hidden;
		private Boolean alwaysShow;
		private String redirect;

		private MenuDTOBuilder() {
		}

		public static MenuDTOBuilder aMenuDTO() {
			return new MenuDTOBuilder();
		}

		public MenuDTOBuilder menuId(Integer menuId) {
			this.menuId = menuId;
			return this;
		}

		public MenuDTOBuilder name(String name) {
			this.name = name;
			return this;
		}

		public MenuDTOBuilder perms(String perms) {
			this.perms = perms;
			return this;
		}

		public MenuDTOBuilder path(String path) {
			this.path = path;
			return this;
		}

		public MenuDTOBuilder isFrame(Boolean isFrame) {
			this.isFrame = isFrame;
			return this;
		}

		public MenuDTOBuilder parentId(Integer parentId) {
			this.parentId = parentId;
			return this;
		}

		public MenuDTOBuilder component(String component) {
			this.component = component;
			return this;
		}

		public MenuDTOBuilder icon(String icon) {
			this.icon = icon;
			return this;
		}

		public MenuDTOBuilder sort(Integer sort) {
			this.sort = sort;
			return this;
		}

		public MenuDTOBuilder type(Integer type) {
			this.type = type;
			return this;
		}

		public MenuDTOBuilder delFlag(String delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public MenuDTOBuilder keepAlive(Boolean keepAlive) {
			this.keepAlive = keepAlive;
			return this;
		}

		public MenuDTOBuilder hidden(Boolean hidden) {
			this.hidden = hidden;
			return this;
		}

		public MenuDTOBuilder alwaysShow(Boolean alwaysShow) {
			this.alwaysShow = alwaysShow;
			return this;
		}

		public MenuDTOBuilder redirect(String redirect) {
			this.redirect = redirect;
			return this;
		}

		public MenuDTO build() {
			return new MenuDTO(menuId, name, perms, path, isFrame, parentId, component, icon, sort,
				type, delFlag, keepAlive, hidden, alwaysShow, redirect);
		}
	}
}
