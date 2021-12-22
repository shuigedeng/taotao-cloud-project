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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.List;
import java.util.Objects;

/**
 * Mapper数据模型节点
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 08:05:13
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
