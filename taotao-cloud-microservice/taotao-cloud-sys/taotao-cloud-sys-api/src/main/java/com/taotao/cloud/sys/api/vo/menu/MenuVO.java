package com.taotao.cloud.sys.api.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(description = "菜单VO")
public record MenuVO(
	@Schema(description = "菜单名称")
	String name,

	@Schema(description = "菜单路径")
	String path,

	@Schema(description = "菜单redirect")
	String redirect,

	@Schema(description = "菜单组件名称")
	String component,

	@Schema(description = "菜单alwaysShow")
	Boolean alwaysShow,

	@Schema(description = "菜单meta")
	MenuMetaVO meta,

	@Schema(description = "菜单children")
	List<MenuVO> children
) implements Serializable {


}
