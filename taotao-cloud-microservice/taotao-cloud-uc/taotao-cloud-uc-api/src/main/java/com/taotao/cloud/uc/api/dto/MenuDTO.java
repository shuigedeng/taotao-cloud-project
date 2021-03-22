package com.taotao.cloud.uc.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.*;

/**
 * dengtao
 *
 * @author dengtao
 * @since 2020/6/15 11:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
