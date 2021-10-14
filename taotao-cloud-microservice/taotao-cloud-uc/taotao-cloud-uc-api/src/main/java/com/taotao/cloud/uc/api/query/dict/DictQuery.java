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

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 字典查询query
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/30 08:49
 */
@Schema(name = "DictQuery", description = "字典查询query")
public class DictQuery implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "字典名称")
	private String dictName;

	@Schema(description = "字典编码")
	private String dictCode;

	@Schema(description = "描述")
	private String description;

	@Schema(description = "备注信息")
	private String remark;
	public DictQuery(){}

	public DictQuery(String dictName, String dictCode, String description, String remark) {
		this.dictName = dictName;
		this.dictCode = dictCode;
		this.description = description;
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "DictQuery{" +
			"dictName='" + dictName + '\'' +
			", dictCode='" + dictCode + '\'' +
			", description='" + description + '\'' +
			", remark='" + remark + '\'' +
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
		DictQuery dictQuery = (DictQuery) o;
		return Objects.equals(dictName, dictQuery.dictName) && Objects.equals(
			dictCode, dictQuery.dictCode) && Objects.equals(description,
			dictQuery.description) && Objects.equals(remark, dictQuery.remark);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dictName, dictCode, description, remark);
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

	public static DictQueryBuilder builder() {
		return new DictQueryBuilder();
	}



	public static final class DictQueryBuilder {

		private String dictName;
		private String dictCode;
		private String description;
		private String remark;

		private DictQueryBuilder() {
		}

		public static DictQueryBuilder aDictQuery() {
			return new DictQueryBuilder();
		}

		public DictQueryBuilder dictName(String dictName) {
			this.dictName = dictName;
			return this;
		}

		public DictQueryBuilder dictCode(String dictCode) {
			this.dictCode = dictCode;
			return this;
		}

		public DictQueryBuilder description(String description) {
			this.description = description;
			return this;
		}

		public DictQueryBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public DictQuery build() {
			DictQuery dictQuery = new DictQuery();
			dictQuery.setDictName(dictName);
			dictQuery.setDictCode(dictCode);
			dictQuery.setDescription(description);
			dictQuery.setRemark(remark);
			return dictQuery;
		}
	}
}
