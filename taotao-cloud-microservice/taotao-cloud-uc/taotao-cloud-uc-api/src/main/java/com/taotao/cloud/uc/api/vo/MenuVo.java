package com.taotao.cloud.uc.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 菜单
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
