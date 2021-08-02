package com.taotao.cloud.uc.api.dto;

import com.taotao.cloud.uc.api.vo.DeptTreeVo.DeptTreeVoBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * shuigedeng
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Schema(name = "DeptDTO", description = "用户注册DTO")
public class DeptDTO implements Serializable {

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

	public DeptDTO() {
	}

	public DeptDTO(Integer deptId, String name, Integer parentId, Integer sort, String remark) {
		this.deptId = deptId;
		this.name = name;
		this.parentId = parentId;
		this.sort = sort;
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "DeptDTO{" +
			"deptId=" + deptId +
			", name='" + name + '\'' +
			", parentId=" + parentId +
			", sort=" + sort +
			", remark='" + remark + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DeptDTO deptDTO = (DeptDTO) o;
		return Objects.equals(deptId, deptDTO.deptId) && Objects.equals(name,
			deptDTO.name) && Objects.equals(parentId, deptDTO.parentId)
			&& Objects.equals(sort, deptDTO.sort) && Objects.equals(remark,
			deptDTO.remark);
	}

	@Override
	public int hashCode() {
		return Objects.hash(deptId, name, parentId, sort, remark);
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

	public static DeptDTOBuilder builder() {
		return new DeptDTOBuilder();
	}


	public static final class DeptDTOBuilder {

		private Integer deptId;
		private String name;
		private Integer parentId;
		private Integer sort;
		private String remark;

		private DeptDTOBuilder() {
		}

		public static DeptDTOBuilder aDeptDTO() {
			return new DeptDTOBuilder();
		}

		public DeptDTOBuilder deptId(Integer deptId) {
			this.deptId = deptId;
			return this;
		}

		public DeptDTOBuilder name(String name) {
			this.name = name;
			return this;
		}

		public DeptDTOBuilder parentId(Integer parentId) {
			this.parentId = parentId;
			return this;
		}

		public DeptDTOBuilder sort(Integer sort) {
			this.sort = sort;
			return this;
		}

		public DeptDTOBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public DeptDTO build() {
			DeptDTO deptDTO = new DeptDTO();
			deptDTO.setDeptId(deptId);
			deptDTO.setName(name);
			deptDTO.setParentId(parentId);
			deptDTO.setSort(sort);
			deptDTO.setRemark(remark);
			return deptDTO;
		}
	}
}
