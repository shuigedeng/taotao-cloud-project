package com.taotao.cloud.uc.api.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 部门查询对象
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Schema(description = "部门查询对象")
public record DeptQueryVO(
	@Schema(description = "部门id", required = true)
	Integer deptId,

	@Schema(description = "部门名称")
	String name,

	@Schema(description = "上级部门id")
	Integer parentId,

	@Schema(description = "排序")
	Integer sort,

	@Schema(description = "备注")
	String remark
) implements Serializable {

	@Serial
	static final long serialVersionUID = 1L;


}
