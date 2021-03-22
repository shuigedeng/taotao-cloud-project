package com.taotao.cloud.uc.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 菜单树DTO
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
