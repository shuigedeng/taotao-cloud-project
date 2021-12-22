/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.tree;

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
 * @version 2021.9
 * @since 2021-09-02 22:28:02
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


	public TreeNode() {
	}

	public TreeNode(Long id, Long parentId, List<INode> children, Boolean hasChildren) {
		this.id = id;
		this.parentId = parentId;
		this.children = children;
		this.hasChildren = hasChildren;
	}

	@Override
	public Boolean getHasChildren() {
		if (children.size() > 0) {
			return true;
		} else {
			return this.hasChildren;
		}
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
