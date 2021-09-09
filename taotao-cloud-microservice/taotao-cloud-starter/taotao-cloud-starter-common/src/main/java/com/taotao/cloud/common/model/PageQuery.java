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
package com.taotao.cloud.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 基础分页查询
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:09:09
 */
@Schema(name = "PageQuery", description = "基础分页查询对象")
public class PageQuery implements Serializable {

	private static final long serialVersionUID = -2483306509077581330L;

	/**
	 * 当前第几页
	 */
	@Schema(description = "当前第几页，默认1", example = "1", required = true)
	@NotNull(message = "当前页显示数量不能为空")
	@Min(value = 0)
	@Max(value = Integer.MAX_VALUE)
	private Integer currentPage = 1;

	/**
	 * 每页显示条数
	 */
	@Schema(description = "每页显示条数，默认10", example = "10", required = true)
	@NotNull(message = "每页数据显示数量不能为空")
	@Min(value = 5)
	@Max(value = 100)
	private Integer pageSize = 10;

	public PageQuery() {
	}

	public PageQuery(Integer currentPage, Integer pageSize) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "BasePageQuery{" +
			"currentPage=" + currentPage +
			", pageSize=" + pageSize +
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
		PageQuery that = (PageQuery) o;
		return Objects.equals(currentPage, that.currentPage) && Objects.equals(
			pageSize, that.pageSize);
	}

	@Override
	public int hashCode() {
		return Objects.hash(currentPage, pageSize);
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
