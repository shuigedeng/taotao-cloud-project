package com.taotao.cloud.sys.api.dto.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * 部门查询对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-23 08:50:11
 */
@Schema(description = "部门查询对象")
public record DeptQueryDTO(

	@Schema(description = "部门id", required = true)
	Integer deptId,

	@Schema(description = "部门名称")
	String name,

	@Schema(description = "上级部门id")
	Integer parentId,

	@Schema(description = "排序")
	Integer sort,

	@Schema(description = "备注")
	String remark) implements Serializable {

	static final long serialVersionUID = 1L;

}
