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

import com.taotao.cloud.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * 字典项分页查询query
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/30 08:49
 */
@Schema(name = "DictItemPageQuery", description = "字典项分页查询query")
public class DictItemPageQuery extends PageQuery {

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

	public DictItemPageQuery() {
	}

	public DictItemPageQuery(Long dictId, String itemText, String itemValue,
		String description, Boolean status) {
		this.dictId = dictId;
		this.itemText = itemText;
		this.itemValue = itemValue;
		this.description = description;
		this.status = status;
	}

	public DictItemPageQuery(Integer currentPage, Integer pageSize, Long dictId,
		String itemText, String itemValue, String description, Boolean status) {
		super(currentPage, pageSize);
		this.dictId = dictId;
		this.itemText = itemText;
		this.itemValue = itemValue;
		this.description = description;
		this.status = status;
	}


	@Override
	public String toString() {
		return "DictItemPageQuery{" +
			"dictId=" + dictId +
			", itemText='" + itemText + '\'' +
			", itemValue='" + itemValue + '\'' +
			", description='" + description + '\'' +
			", status=" + status +
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
		DictItemPageQuery that = (DictItemPageQuery) o;
		return Objects.equals(dictId, that.dictId) && Objects.equals(itemText,
			that.itemText) && Objects.equals(itemValue, that.itemValue)
			&& Objects.equals(description, that.description) && Objects.equals(
			status, that.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), dictId, itemText, itemValue, description, status);
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

	public static DictItemPageQueryBuilder builder() {
		return new DictItemPageQueryBuilder();
	}


	public static final class DictItemPageQueryBuilder {

		private Integer currentPage = 1;
		private Integer pageSize = 10;
		private Long dictId;
		private String itemText;
		private String itemValue;
		private String description;
		private Boolean status;

		private DictItemPageQueryBuilder() {
		}

		public static DictItemPageQueryBuilder aDictItemPageQuery() {
			return new DictItemPageQueryBuilder();
		}

		public DictItemPageQueryBuilder currentPage(Integer currentPage) {
			this.currentPage = currentPage;
			return this;
		}

		public DictItemPageQueryBuilder pageSize(Integer pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public DictItemPageQueryBuilder dictId(Long dictId) {
			this.dictId = dictId;
			return this;
		}

		public DictItemPageQueryBuilder itemText(String itemText) {
			this.itemText = itemText;
			return this;
		}

		public DictItemPageQueryBuilder itemValue(String itemValue) {
			this.itemValue = itemValue;
			return this;
		}

		public DictItemPageQueryBuilder description(String description) {
			this.description = description;
			return this;
		}

		public DictItemPageQueryBuilder status(Boolean status) {
			this.status = status;
			return this;
		}

		public DictItemPageQuery build() {
			DictItemPageQuery dictItemPageQuery = new DictItemPageQuery();
			dictItemPageQuery.setCurrentPage(currentPage);
			dictItemPageQuery.setPageSize(pageSize);
			dictItemPageQuery.setDictId(dictId);
			dictItemPageQuery.setItemText(itemText);
			dictItemPageQuery.setItemValue(itemValue);
			dictItemPageQuery.setDescription(description);
			dictItemPageQuery.setStatus(status);
			return dictItemPageQuery;
		}
	}
}
