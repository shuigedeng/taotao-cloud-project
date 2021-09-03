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

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;

/**
 * 返回分页实体类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:09:19
 */
@Schema(description = "分页结果对象")
public class PageModel<T> implements Serializable {

	private static final long serialVersionUID = -275582248840137389L;
	/**
	 * 总条数
	 */
	@Schema(description = "总条数")
	private long totalSize;
	/**
	 * 总页数
	 */
	@Schema(description = "总页数")
	private int totalPage;
	/**
	 * 当前第几页
	 */
	@Schema(description = "当前第几页")
	private int currentPage;
	/**
	 * 每页显示条数
	 */
	@Schema(description = "每页显示条数")
	private int pageSize;
	/**
	 * 返回数据
	 */
	@Schema(description = "返回数据")
	private List<T> data;

	public PageModel() {
	}

	public PageModel(long totalSize, int totalPage, int currentPage, int pageSize,
		List<T> data) {
		this.totalSize = totalSize;
		this.totalPage = totalPage;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.data = data;
	}

	/**
	 * convertJpaPage
	 *
	 * @param page page
	 * @param <T>  T
	 * @return {@link PageModel }
	 * @author shuigedeng
	 * @since 2021-09-02 19:10:45
	 */
	public static <T> PageModel<T> convertJpaPage(Page<T> page) {
		return of(
			page.getTotalElements(),
			page.getTotalPages(),
			page.getNumber(),
			page.getSize(),
			page.getContent()
		);
	}

	/**
	 * convertMybatisPage
	 *
	 * @param page page
	 * @param <T>  T
	 * @return {@link PageModel }
	 * @author shuigedeng
	 * @since 2021-09-02 19:10:49
	 */
	public static <T> PageModel<T> convertMybatisPage(IPage<T> page) {
		return of(
			page.getTotal(),
			(int) page.getPages(),
			(int) page.getCurrent(),
			(int) page.getSize(),
			page.getRecords()
		);
	}

	/**
	 * of
	 *
	 * @param totalSize   totalSize
	 * @param totalPage   totalPage
	 * @param currentPage currentPage
	 * @param pageSize    pageSize
	 * @param data        data
	 * @param <T>         T
	 * @return {@link com.taotao.cloud.common.model.PageModel }
	 * @author shuigedeng
	 * @since 2021-09-02 19:11:10
	 */
	public static <T> PageModel<T> of(
		long totalSize,
		int totalPage,
		int currentPage,
		int pageSize,
		List<T> data) {
		return PageModel.<T>builder()
			.totalSize(totalSize)
			.totalPage(totalPage)
			.currentPage(currentPage)
			.pageSize(pageSize)
			.data(data)
			.build();
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PageModel<?> pageModel = (PageModel<?>) o;
		return totalSize == pageModel.totalSize && totalPage == pageModel.totalPage
			&& currentPage == pageModel.currentPage && pageSize == pageModel.pageSize
			&& Objects.equals(data, pageModel.data);
	}

	@Override
	public int hashCode() {
		return Objects.hash(totalSize, totalPage, currentPage, pageSize, data);
	}

	@Override
	public String toString() {
		return "PageModel{" +
			"totalSize=" + totalSize +
			", totalPage=" + totalPage +
			", currentPage=" + currentPage +
			", pageSize=" + pageSize +
			", data=" + data +
			'}';
	}

	public static <T> PageModelBuilder<T> builder() {
		return new PageModelBuilder<>();
	}

	public static final class PageModelBuilder<T> {

		private long totalSize;
		private int totalPage;
		private int currentPage;
		private int pageSize;
		private List<T> data;

		private PageModelBuilder() {
		}

		public PageModelBuilder totalSize(long totalSize) {
			this.totalSize = totalSize;
			return this;
		}

		public PageModelBuilder totalPage(int totalPage) {
			this.totalPage = totalPage;
			return this;
		}

		public PageModelBuilder currentPage(int currentPage) {
			this.currentPage = currentPage;
			return this;
		}

		public PageModelBuilder pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public PageModelBuilder data(List<T> data) {
			this.data = data;
			return this;
		}

		public PageModel build() {
			PageModel pageModel = new PageModel();
			pageModel.setTotalSize(totalSize);
			pageModel.setTotalPage(totalPage);
			pageModel.setCurrentPage(currentPage);
			pageModel.setPageSize(pageSize);
			pageModel.setData(data);
			return pageModel;
		}
	}
}
