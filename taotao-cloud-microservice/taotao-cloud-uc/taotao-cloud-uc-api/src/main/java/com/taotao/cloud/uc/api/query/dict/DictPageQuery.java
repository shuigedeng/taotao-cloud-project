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
package com.taotao.cloud.uc.api.query.dict;

import com.taotao.cloud.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * 字典分页查询query
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/30 08:49
 */
@Schema(name = "DictPageQuery", description = "字典分页查询query")
public class DictPageQuery extends PageQuery {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "字典名称")
	private String dictName;

	@Schema(description = "字典编码")
	private String dictCode;

	@Schema(description = "描述")
	private String description;

	@Schema(description = "备注信息")
	private String remark;

	public DictPageQuery() {
	}

	public DictPageQuery(String dictName, String dictCode, String description, String remark) {
		this.dictName = dictName;
		this.dictCode = dictCode;
		this.description = description;
		this.remark = remark;
	}

	public DictPageQuery(Integer currentPage, Integer pageSize, String dictName,
		String dictCode, String description, String remark) {
		super(currentPage, pageSize);
		this.dictName = dictName;
		this.dictCode = dictCode;
		this.description = description;
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "DictPageQuery{" +
			"dictName='" + dictName + '\'' +
			", dictCode='" + dictCode + '\'' +
			", description='" + description + '\'' +
			", remark='" + remark + '\'' +
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
		DictPageQuery that = (DictPageQuery) o;
		return Objects.equals(dictName, that.dictName) && Objects.equals(dictCode,
			that.dictCode) && Objects.equals(description, that.description)
			&& Objects.equals(remark, that.remark);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), dictName, dictCode, description, remark);
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public static DictPageQueryBuilder builder() {
		return new DictPageQueryBuilder();
	}


	public static final class DictPageQueryBuilder {

		private Integer currentPage = 1;
		private Integer pageSize = 10;
		private String dictName;
		private String dictCode;
		private String description;
		private String remark;

		private DictPageQueryBuilder() {
		}

		public static DictPageQueryBuilder aDictPageQuery() {
			return new DictPageQueryBuilder();
		}

		public DictPageQueryBuilder currentPage(Integer currentPage) {
			this.currentPage = currentPage;
			return this;
		}

		public DictPageQueryBuilder pageSize(Integer pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public DictPageQueryBuilder dictName(String dictName) {
			this.dictName = dictName;
			return this;
		}

		public DictPageQueryBuilder dictCode(String dictCode) {
			this.dictCode = dictCode;
			return this;
		}

		public DictPageQueryBuilder description(String description) {
			this.description = description;
			return this;
		}

		public DictPageQueryBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public DictPageQuery build() {
			DictPageQuery dictPageQuery = new DictPageQuery();
			dictPageQuery.setCurrentPage(currentPage);
			dictPageQuery.setPageSize(pageSize);
			dictPageQuery.setDictName(dictName);
			dictPageQuery.setDictCode(dictCode);
			dictPageQuery.setDescription(description);
			dictPageQuery.setRemark(remark);
			return dictPageQuery;
		}
	}
}
