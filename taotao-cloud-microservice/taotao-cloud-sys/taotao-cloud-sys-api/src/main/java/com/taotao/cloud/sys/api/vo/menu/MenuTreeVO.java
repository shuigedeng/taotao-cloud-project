package com.taotao.cloud.sys.api.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;


/**
 * 菜单树DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(description = "菜单树VO")
public record MenuTreeVO(
	@Schema(description = "对应SysDepart中的id字段,前端数据树中的key")
	Long key,

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的value")
	String value,

	@Schema(description = "对应depart_name字段,前端数据树中的title")
	String title,

	@Schema(description = "菜单ID")
	Integer menuId,

	@Schema(description = "菜单名称")
	String name,

	@Schema(description = "父菜单ID")
	Integer parentMenuId,

	@Schema(description = " 菜单类型 （类型   0：目录   1：菜单   2：按钮）")
	Integer type,

	@Schema(description = "排序")
	Integer sort,

	@Schema(description = "父菜单名称")
	String parentName,

	@Schema(description = "菜单等级")
	Integer level,

	@Schema(description = "children")
	List<MenuTreeVO> children) {

	static final long serialVersionUID = 1L;


}
