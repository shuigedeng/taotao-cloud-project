package com.taotao.cloud.sys.api.vo.dept;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.taotao.cloud.common.tree.INode;
import com.taotao.cloud.common.tree.MapperNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DepartVO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-22 20:59:37
 */
public class DeptTreeVO extends MapperNode implements INode {

	/**
	 * 主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 父节点ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

	/**
	 * 子孙节点
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<INode> children;

	/**
	 * 是否有子孙节点
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Boolean hasChildren;

	/**
	 * 部门名称
	 */
	private String name;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 删除标识
	 */
	private String isDeleted;
	/**
	 * 租户ID
	 */
	private Long tenantId;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "修改时间")
	private LocalDateTime updateTime;

	@Override
	public List<INode> getChildren() {
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		return this.children;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Override
	public void setChildren(List<INode> children) {
		this.children = children;
	}

	@Override
	public Boolean getHasChildren() {
		return hasChildren;
	}

	@Override
	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "DepartVO{" +
			"id=" + id +
			", parentId=" + parentId +
			", children=" + children +
			", hasChildren=" + hasChildren +
			", name='" + name + '\'' +
			", sort=" + sort +
			", isDeleted='" + isDeleted + '\'' +
			", tenantId=" + tenantId +
			", remark='" + remark + '\'' +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			'}';
	}
}
