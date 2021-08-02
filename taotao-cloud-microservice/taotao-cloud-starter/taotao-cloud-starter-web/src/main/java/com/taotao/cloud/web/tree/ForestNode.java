package com.taotao.cloud.web.tree;

import java.util.List;
import java.util.Objects;

/**
 * 森林节点
 * @author shuigedeng
 */
public class ForestNode extends TreeNode {

    private static final long serialVersionUID = -5188222097134746118L;

    /**
     * 节点内容
     */
    private Object content;

    public ForestNode(Long id, Long parentId, Object content) {
        this.id = id;
        this.parentId = parentId;
        this.content = content;
    }

	public ForestNode(Object content) {
		this.content = content;
	}

	public ForestNode(Long id, Long parentId, List<INode> children,
		Boolean hasChildren, Object content) {
		super(id, parentId, children, hasChildren);
		this.content = content;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ForestNode{" +
			"content=" + content +
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
		ForestNode that = (ForestNode) o;
		return Objects.equals(content, that.content);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), content);
	}
}
