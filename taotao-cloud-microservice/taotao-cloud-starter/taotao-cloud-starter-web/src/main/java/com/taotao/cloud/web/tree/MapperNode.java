package com.taotao.cloud.web.tree;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.List;
import java.util.Objects;

/**
 * Mapper数据模型节点
 */
public class MapperNode extends TreeNode {

	private String title;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long key;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long value;

	public MapperNode() {
	}

	public MapperNode(String title, Long key, Long value) {
		this.title = title;
		this.key = key;
		this.value = value;
	}

	public MapperNode(Long id, Long parentId, List<INode> children,
		Boolean hasChildren, String title, Long key, Long value) {
		super(id, parentId, children, hasChildren);
		this.title = title;
		this.key = key;
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "MapperNode{" +
			"title='" + title + '\'' +
			", key=" + key +
			", value=" + value +
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
		MapperNode that = (MapperNode) o;
		return Objects.equals(title, that.title) && Objects.equals(key, that.key)
			&& Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), title, key, value);
	}
}
