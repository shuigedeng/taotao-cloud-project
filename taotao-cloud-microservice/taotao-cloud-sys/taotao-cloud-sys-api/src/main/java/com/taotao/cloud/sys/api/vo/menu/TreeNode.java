/*
 * Copyright (c) 2020 taotao cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 *
 * 此类来自 https://gitee.com/geek_qi/cloud-platform/blob/master/ace-common/src/main/java/com/github/wxiaoqi/security/common/vo/TreeNode.java
 * @ Apache-2.0
 */

package com.taotao.cloud.sys.api.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * TreeNode
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/21 11:02
 */
@Schema(name = "TreeNode", description = "树节点")
public class TreeNode implements Serializable {

	@Serial
	private static final long serialVersionUID = -4546704465269983480L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "parentId")
	private Long parentId;

	@Schema(description = "children")
	private List<TreeNode> children;

	@Schema(description = "是否包含子节点")
	private Boolean hasChildren = false;

	public void add(TreeNode node) {
		children.add(node);
	}

	public TreeNode() {
	}

	public TreeNode(Long id, Long parentId,
		List<TreeNode> children, Boolean hasChildren) {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public Boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
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

	public static final class TreeNodeBuilder {

		private Long id;
		private Long parentId;
		private List<TreeNode> children;
		private Boolean hasChildren = false;

		private TreeNodeBuilder() {
		}

		public static TreeNodeBuilder aTreeNode() {
			return new TreeNodeBuilder();
		}

		public TreeNodeBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public TreeNodeBuilder parentId(Long parentId) {
			this.parentId = parentId;
			return this;
		}

		public TreeNodeBuilder children(List<TreeNode> children) {
			this.children = children;
			return this;
		}

		public TreeNodeBuilder hasChildren(Boolean hasChildren) {
			this.hasChildren = hasChildren;
			return this;
		}

		public TreeNode build() {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(id);
			treeNode.setParentId(parentId);
			treeNode.setChildren(children);
			treeNode.setHasChildren(hasChildren);
			return treeNode;
		}
	}
}
