package com.taotao.cloud.uc.api.bo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 部门查询对象
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Schema(name = "DeptQueryVO", description = "部门查询对象")
public class DeptBO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "部门id", required = true)
	private Integer deptId;

	@Schema(description = "部门名称")
	private String name;

	@Schema(description = "上级部门id")
	private Integer parentId;

	@Schema(description = "排序")
	private Integer sort;

	@Schema(description = "备注")
	private String remark;

	public DeptBO() {
	}

	public DeptBO(Integer deptId, String name, Integer parentId, Integer sort,
		String remark) {
		this.deptId = deptId;
		this.name = name;
		this.parentId = parentId;
		this.sort = sort;
		this.remark = remark;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
