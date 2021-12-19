package com.taotao.cloud.sys.api.dto.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单DTO
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Schema(description = "菜单DTO")
public record MenuDTO(
	@Schema(description = "菜单DTO")
	Integer menuId,

	@Schema(description = "菜单名称")
	String name,

	@Schema(description = "菜单权限")
	String perms,

	@Schema(description = "菜单路径")
	String path,

	@Schema(description = "菜单isFrame")
	Boolean isFrame,

	@Schema(description = "父菜单id")
	Integer parentId,

	@Schema(description = "菜单组件名称")
	String component,

	@Schema(description = "菜单icon")
	String icon,

	@Schema(description = "菜单排序")
	Integer sort,

	@Schema(description = "菜单类型")
	Integer type,

	@Schema(description = "菜单删除标识")
	String delFlag,

	@Schema(description = "菜单keepAlive")
	Boolean keepAlive,

	@Schema(description = "菜单是否隐藏")
	Boolean hidden,

	@Schema(description = "菜单是否一直展示")
	Boolean alwaysShow,

	@Schema(description = "菜单redirect")
	String redirect) implements Serializable {

	@Serial
	static final long serialVersionUID = 1L;


}
