package com.taotao.cloud.sys.api.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * 菜单
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@Schema(description = "菜单VO")
public class MenuVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -5853343562172855421L;

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
	private MenuMetaVO meta;

	@Schema(description = "菜单children")
	private List<MenuVO> children;

}
