package com.taotao.cloud.web.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 树根节点
 *
 * @author shuigedeng
 */
public class TreeNode implements INode {

	/**
	 * 主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	protected Long id;

	/**
	 * 父节点ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	protected Long parentId;

	/**
	 * 子孙节点
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	protected List<INode> children = new ArrayList<>();

	/**
	 * 是否有子孙节点
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Boolean hasChildren;

	/**
	 * 是否有子孙节点
	 *
	 * @return Boolean
	 */
	@Override
	public Boolean getHasChildren() {
		if (children.size() > 0) {
			return true;
		} else {
			return this.hasChildren;
		}
	}

	public TreeNode() {
	}

	public TreeNode(Long id, Long parentId, List<INode> children, Boolean hasChildren) {
		this.id = id;
		this.parentId = parentId;
		this.children = children;
		this.hasChildren = hasChildren;
	}

	@Override
	public String toString() {
		return "TreeNode{" +
			"id=" + id +
			", parentId=" + parentId +
			", children=" + children +
			", hasChildren=" + hasChildren +
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
		TreeNode treeNode = (TreeNode) o;
		return Objects.equals(id, treeNode.id) && Objects.equals(parentId,
			treeNode.parentId) && Objects.equals(children, treeNode.children)
			&& Objects.equals(hasChildren, treeNode.hasChildren);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, parentId, children, hasChildren);
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Override
	public List<INode> getChildren() {
		return children;
	}

	public void setChildren(List<INode> children) {
		this.children = children;
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
}
