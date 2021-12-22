package com.taotao.cloud.sys.api.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 菜单DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "MenuMetaVo", description = "菜单元数据VO")
public class MenuMetaVO {

	@Schema(description = "名称")
	private String title;

	@Schema(description = "icon")
	private String icon;

	public MenuMetaVO() {
	}

	public MenuMetaVO(String title, String icon) {
		this.title = title;
		this.icon = icon;
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
}
