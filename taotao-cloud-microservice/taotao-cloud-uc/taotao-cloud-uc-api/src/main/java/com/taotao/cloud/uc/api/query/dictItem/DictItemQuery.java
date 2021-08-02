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
package com.taotao.cloud.uc.api.query.dictItem;

import com.taotao.cloud.uc.api.vo.QueryRegionByParentIdVO.QueryRegionByParentIdVOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * 字典查询query
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/30 08:49
 */
@Schema(name = "DictItemQuery", description = "字典查询query")
public class DictItemQuery implements Serializable {

	private static final long serialVersionUID = -7605952923416404638L;
	@Schema(description = "字典id")
	private Long dictId;
	@Schema(description = "字典项文本")
	private String itemText;
	@Schema(description = "字典项值")
	private String itemValue;
	@Schema(description = "描述")
	private String description;
	@Schema(description = "状态(1不启用 2启用)")
	private Boolean status;

	public DictItemQuery (){}

	public DictItemQuery(Long dictId, String itemText, String itemValue, String description,
		Boolean status) {
		this.dictId = dictId;
		this.itemText = itemText;
		this.itemValue = itemValue;
		this.description = description;
		this.status = status;
	}

	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public String getItemText() {
		return itemText;
	}

	public void setItemText(String itemText) {
		this.itemText = itemText;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "DictItemQuery{" +
			"dictId=" + dictId +
			", itemText='" + itemText + '\'' +
			", itemValue='" + itemValue + '\'' +
			", description='" + description + '\'' +
			", status=" + status +
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
		DictItemQuery that = (DictItemQuery) o;
		return Objects.equals(dictId, that.dictId) && Objects.equals(itemText,
			that.itemText) && Objects.equals(itemValue, that.itemValue)
			&& Objects.equals(description, that.description) && Objects.equals(
			status, that.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dictId, itemText, itemValue, description, status);
	}

	public static DictItemQueryBuilder builder() {
		return new DictItemQueryBuilder();
	}


	public static final class DictItemQueryBuilder {

		private Long dictId;
		private String itemText;
		private String itemValue;
		private String description;
		private Boolean status;

		private DictItemQueryBuilder() {
		}

		public static DictItemQueryBuilder aDictItemQuery() {
			return new DictItemQueryBuilder();
		}

		public DictItemQueryBuilder dictId(Long dictId) {
			this.dictId = dictId;
			return this;
		}

		public DictItemQueryBuilder itemText(String itemText) {
			this.itemText = itemText;
			return this;
		}

		public DictItemQueryBuilder itemValue(String itemValue) {
			this.itemValue = itemValue;
			return this;
		}

		public DictItemQueryBuilder description(String description) {
			this.description = description;
			return this;
		}

		public DictItemQueryBuilder status(Boolean status) {
			this.status = status;
			return this;
		}

		public DictItemQuery build() {
			DictItemQuery dictItemQuery = new DictItemQuery();
			dictItemQuery.setDictId(dictId);
			dictItemQuery.setItemText(itemText);
			dictItemQuery.setItemValue(itemValue);
			dictItemQuery.setDescription(description);
			dictItemQuery.setStatus(status);
			return dictItemQuery;
		}
	}
}
