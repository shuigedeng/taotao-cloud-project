package com.taotao.cloud.uc.api.bo.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 菜单
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "MenuVo", description = "菜单VO")
public class MenuVo implements Serializable {

	@Schema(description = "菜单名称")
	private String name;

	@Schema(description = "菜单路径")
	private String path;

	@Schema(description = "菜单redirect")
	private String redirect;

	@Schema(description = "菜单组件名称")
	private String component;

	@Schema(description = "菜单alwaysShow")
	private Boolean alwaysShow;

	@Schema(description = "菜单meta")
	private MenuMetaVo meta;

	@Schema(description = "菜单children")
	private List<MenuVo> children;

	public MenuVo(String name, String path, String redirect, String component,
		Boolean alwaysShow, MenuMetaVo meta,
		List<MenuVo> children) {
		this.name = name;
		this.path = path;
		this.redirect = redirect;
		this.component = component;
		this.alwaysShow = alwaysShow;
		this.meta = meta;
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public Boolean getAlwaysShow() {
		return alwaysShow;
	}

	public void setAlwaysShow(Boolean alwaysShow) {
		this.alwaysShow = alwaysShow;
	}

	public MenuMetaVo getMeta() {
		return meta;
	}

	public void setMeta(MenuMetaVo meta) {
		this.meta = meta;
	}

	public List<MenuVo> getChildren() {
		return children;
	}

	public void setChildren(List<MenuVo> children) {
		this.children = children;
	}

	@Override
	public String
	toString() {
		return "MenuVo{" +
			"name='" + name + '\'' +
			", path='" + path + '\'' +
			", redirect='" + redirect + '\'' +
			", component='" + component + '\'' +
			", alwaysShow=" + alwaysShow +
			", meta=" + meta +
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
		MenuVo menuVo = (MenuVo) o;
		return Objects.equals(name, menuVo.name) && Objects.equals(path,
			menuVo.path) && Objects.equals(redirect, menuVo.redirect)
			&& Objects.equals(component, menuVo.component) && Objects.equals(
			alwaysShow, menuVo.alwaysShow) && Objects.equals(meta, menuVo.meta)
			&& Objects.equals(children, menuVo.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, path, redirect, component, alwaysShow, meta, children);
	}

	public static MenuVoBuilder builder() {
		return new MenuVoBuilder();
	}


	public static final class MenuVoBuilder {

		private String name;
		private String path;
		private String redirect;
		private String component;
		private Boolean alwaysShow;
		private MenuMetaVo meta;
		private List<MenuVo> children;

		private MenuVoBuilder() {
		}

		public static MenuVoBuilder aMenuVo() {
			return new MenuVoBuilder();
		}

		public MenuVoBuilder name(String name) {
			this.name = name;
			return this;
		}

		public MenuVoBuilder path(String path) {
			this.path = path;
			return this;
		}

		public MenuVoBuilder redirect(String redirect) {
			this.redirect = redirect;
			return this;
		}

		public MenuVoBuilder component(String component) {
			this.component = component;
			return this;
		}

		public MenuVoBuilder alwaysShow(Boolean alwaysShow) {
			this.alwaysShow = alwaysShow;
			return this;
		}

		public MenuVoBuilder meta(MenuMetaVo meta) {
			this.meta = meta;
			return this;
		}

		public MenuVoBuilder children(List<MenuVo> children) {
			this.children = children;
			return this;
		}

		public MenuVo build() {
			return new MenuVo(name, path, redirect, component, alwaysShow, meta, children);
		}
	}
}
