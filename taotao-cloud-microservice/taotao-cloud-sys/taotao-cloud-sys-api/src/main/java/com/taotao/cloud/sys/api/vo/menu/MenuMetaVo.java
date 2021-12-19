package com.taotao.cloud.sys.api.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * 菜单DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "MenuMetaVo", description = "菜单元数据VO")
public class MenuMetaVo {

	@Schema(description = "名称")
	private String title;

	@Schema(description = "icon")
	private String icon;

	public MenuMetaVo() {
	}

	public MenuMetaVo(String title, String icon) {
		this.title = title;
		this.icon = icon;
	}


	@Override
	public String toString() {
		return "MenuMetaVo{" +
			"title='" + title + '\'' +
			", icon='" + icon + '\'' +
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
		MenuMetaVo that = (MenuMetaVo) o;
		return Objects.equals(title, that.title) && Objects.equals(icon, that.icon);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, icon);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public static MenuMetaVoBuilder builder() {
		return new MenuMetaVoBuilder();
	}

	public static final class MenuMetaVoBuilder {

		private String title;
		private String icon;

		private MenuMetaVoBuilder() {
		}

		public static MenuMetaVoBuilder aMenuMetaVo() {
			return new MenuMetaVoBuilder();
		}

		public MenuMetaVoBuilder title(String title) {
			this.title = title;
			return this;
		}

		public MenuMetaVoBuilder icon(String icon) {
			this.icon = icon;
			return this;
		}

		public MenuMetaVo build() {
			MenuMetaVo menuMetaVo = new MenuMetaVo();
			menuMetaVo.setTitle(title);
			menuMetaVo.setIcon(icon);
			return menuMetaVo;
		}
	}
}
