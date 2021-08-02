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
package com.taotao.cloud.uc.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;

/**
 * ApplicationDTO
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/03/12 16:31
 */
@Schema(name = "QueryRegionByParentIdVO", description = "查询应用列表数据VO")
public class QueryRegionByParentIdVO {

	@Schema(description = "主键ID")
	private String id;

	@Schema(description = "名称")
	private String label;

	@Schema(description = "应用名称")
	private String value;

	@Schema(description = "子数据")
	private List<QueryRegionByParentIdVO> children;

	public QueryRegionByParentIdVO() {
	}

	public QueryRegionByParentIdVO(String id, String label, String value,
		List<QueryRegionByParentIdVO> children) {
		this.id = id;
		this.label = label;
		this.value = value;
		this.children = children;
	}

	@Override
	public String toString() {
		return "QueryRegionByParentIdVO{" +
			"id='" + id + '\'' +
			", label='" + label + '\'' +
			", value='" + value + '\'' +
			", children=" + children +
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
		QueryRegionByParentIdVO that = (QueryRegionByParentIdVO) o;
		return Objects.equals(id, that.id) && Objects.equals(label, that.label)
			&& Objects.equals(value, that.value) && Objects.equals(children,
			that.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, label, value, children);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<QueryRegionByParentIdVO> getChildren() {
		return children;
	}

	public void setChildren(List<QueryRegionByParentIdVO> children) {
		this.children = children;
	}

	public static QueryRegionByParentIdVOBuilder builder() {
		return new QueryRegionByParentIdVOBuilder();
	}

	public static final class QueryRegionByParentIdVOBuilder {

		private String id;
		private String label;
		private String value;
		private List<QueryRegionByParentIdVO> children;

		private QueryRegionByParentIdVOBuilder() {
		}

		public static QueryRegionByParentIdVOBuilder aQueryRegionByParentIdVO() {
			return new QueryRegionByParentIdVOBuilder();
		}

		public QueryRegionByParentIdVOBuilder id(String id) {
			this.id = id;
			return this;
		}

		public QueryRegionByParentIdVOBuilder label(String label) {
			this.label = label;
			return this;
		}

		public QueryRegionByParentIdVOBuilder value(String value) {
			this.value = value;
			return this;
		}

		public QueryRegionByParentIdVOBuilder children(List<QueryRegionByParentIdVO> children) {
			this.children = children;
			return this;
		}

		public QueryRegionByParentIdVO build() {
			QueryRegionByParentIdVO queryRegionByParentIdVO = new QueryRegionByParentIdVO();
			queryRegionByParentIdVO.setId(id);
			queryRegionByParentIdVO.setLabel(label);
			queryRegionByParentIdVO.setValue(value);
			queryRegionByParentIdVO.setChildren(children);
			return queryRegionByParentIdVO;
		}
	}
}
