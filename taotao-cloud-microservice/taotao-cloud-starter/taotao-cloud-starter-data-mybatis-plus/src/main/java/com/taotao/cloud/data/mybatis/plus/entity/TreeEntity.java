package com.taotao.cloud.data.mybatis.plus.entity;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * TreeEntity 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:51
 */
public class TreeEntity<E, T extends Serializable> extends Entity<T> {

	/**
	 * 名称
	 */
	@NotEmpty(message = "名称不能为空")
	@Size(max = 255, message = "名称长度不能超过255")
	@TableField(value = "label", condition = LIKE)
	protected String label;

	/**
	 * 父ID
	 */
	@TableField(value = "parent_id")
	protected T parentId;

	/**
	 * 排序
	 */
	@TableField(value = "sort_value")
	protected Integer sortValue;


	@TableField(exist = false)
	protected List<E> children;


	/**
	 * 初始化子类
	 */
	public void initChildren() {
		if (getChildren() == null) {
			this.setChildren(new ArrayList<>());
		}
	}

	@Override
	public String
	toString() {
		return "TreeEntity{" +
			"label='" + label + '\'' +
			", parentId=" + parentId +
			", sortValue=" + sortValue +
			", children=" + children +
			"} " + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		TreeEntity<?, ?> that = (TreeEntity<?, ?>) o;
		return Objects.equals(label, that.label) && Objects.equals(parentId,
			that.parentId) && Objects.equals(sortValue, that.sortValue)
			&& Objects.equals(children, that.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), label, parentId, sortValue, children);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public T getParentId() {
		return parentId;
	}

	public void setParentId(T parentId) {
		this.parentId = parentId;
	}

	public Integer getSortValue() {
		return sortValue;
	}

	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}

	public List<E> getChildren() {
		return children;
	}

	public void setChildren(List<E> children) {
		this.children = children;
	}

	public TreeEntity(T id, LocalDateTime createTime, T createdBy,
		LocalDateTime updateTime, T updatedBy, String label, T parentId,
		Integer sortValue, List<E> children) {
		super(id, createTime, createdBy, updateTime, updatedBy);
		this.label = label;
		this.parentId = parentId;
		this.sortValue = sortValue;
		this.children = children;
	}

	public TreeEntity(String label, T parentId, Integer sortValue, List<E> children) {
		this.label = label;
		this.parentId = parentId;
		this.sortValue = sortValue;
		this.children = children;
	}

	public TreeEntity(LocalDateTime updateTime, T updatedBy, String label, T parentId,
		Integer sortValue, List<E> children) {
		super(updateTime, updatedBy);
		this.label = label;
		this.parentId = parentId;
		this.sortValue = sortValue;
		this.children = children;
	}
}
