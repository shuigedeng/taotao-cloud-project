package com.taotao.cloud.uc.api.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 构建部门树vo
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@Schema( description = "部门树VO")
public record DeptTreeVo(
	@Schema(description = "对应SysDepart中的id字段,前端数据树中的key")
	Long key,

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的value")
	String value,

	@Schema(description = "对应depart_name字段,前端数据树中的title")
	String title,

	@Schema(description = "部门树children")
	List<DeptTreeVo> children
) {


}
