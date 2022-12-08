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
 * WIRHOUR WARRANRIES OR CONDIRIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.model;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

/**
 * 返回分页实体类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:09:19
 */
@Schema(description = "分页结果对象")
public class PageResult<R> implements Serializable {

	@Serial
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
	private List<R> data;

	public PageResult() {
	}

	public PageResult(long totalSize, int totalPage, int currentPage, int pageSize,
		List<R> data) {
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
	 * @param <R>  R
	 * @return {@link PageResult }
	 * @since 2021-09-02 19:10:45
	 */
	public static <R, T> PageResult<R> convertJpaPage(Page<T> page, Class<R> r) {
		List<T> records = page.getContent();
		List<R> collect = Optional.of(records)
			.orElse(new ArrayList<>())
			.stream().filter(Objects::nonNull)
			.map(t -> BeanUtil.toBean(t, r)).collect(Collectors.toList());

		return of(
			page.getTotalElements(),
			page.getTotalPages(),
			page.getNumber(),
			page.getSize(),
			collect
		);
	}


	public static <R, T> PageResult<R> convertMybatisPage(IPage<T> page, List<R> collect) {
		return of(
			page.getTotal(),
			(int) page.getPages(),
			(int) page.getCurrent(),
			(int) page.getSize(),
			collect
		);
	}

	public static <T> PageResult<T> convertMybatisPage(IPage<T> page) {
		return of(
			page.getTotal(),
			(int) page.getPages(),
			(int) page.getCurrent(),
			(int) page.getSize(),
			page.getRecords()
		);
	}

	/**
	 * convertMybatisPage
	 *
	 * @param page page
	 * @param <R>  R
	 * @return {@link PageResult }
	 * @since 2021-09-02 19:10:49
	 */
	public static <R, T> PageResult<R> convertMybatisPage(IPage<T> page, Class<R> r) {
		List<T> records = page.getRecords();
		List<R> collect = Optional.ofNullable(records)
			.orElse(new ArrayList<>())
			.stream().filter(Objects::nonNull)
			.map(t -> BeanUtil.toBean(t, r)).collect(Collectors.toList());

		return of(
			page.getTotal(),
			(int) page.getPages(),
			(int) page.getCurrent(),
			(int) page.getSize(),
			collect
		);
	}

	public static <R> PageResult<R> of(
		int currentPage,
		int pageSize,
		List<R> data) {
		return of(data.size(),
			getPages(data.size(), pageSize),
			currentPage, pageSize, data);
	}

	public static <R> PageResult<R> of(
		PageParam pageParam,
		List<R> data) {
		return of(data.size(),
			getPages(data.size(), pageParam.getPageSize()),
			pageParam.getCurrentPage(), pageParam.getPageSize(), data);
	}

	/**
	 * 从mybatis分页源码copy
	 *
	 * @param totalSize
	 * @param pageSize
	 * @return
	 */
	public static int getPages(long totalSize, int pageSize) {
		if (totalSize == 0L) {
			return 0;
		} else {
			int pages = (int) (totalSize / pageSize);
			if (totalSize % pageSize != 0L) {
				++pages;
			}

			return pages;
		}
	}

	/**
	 * of
	 *
	 * @param totalSize   totalSize
	 * @param totalPage   totalPage
	 * @param currentPage currentPage
	 * @param pageSize    pageSize
	 * @param data        data
	 * @param <R>         R
	 * @return {@link PageResult }
	 * @since 2021-09-02 19:11:10
	 */
	public static <R> PageResult<R> of(
		long totalSize,
		int totalPage,
		int currentPage,
		int pageSize,
		List<R> data) {
		return PageResult
			.<R>builder()
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

	public List<R> getData() {
		return data;
	}

	public void setData(List<R> data) {
		this.data = data;
	}

	public static <R> PageModelBuilder<R> builder() {
		return new PageModelBuilder<>();
	}

	public static final class PageModelBuilder<R> {

		private long totalSize;
		private int totalPage;
		private int currentPage;
		private int pageSize;
		private List<R> data;

		private PageModelBuilder() {
		}

		public PageModelBuilder<R> totalSize(long totalSize) {
			this.totalSize = totalSize;
			return this;
		}

		public PageModelBuilder<R> totalPage(int totalPage) {
			this.totalPage = totalPage;
			return this;
		}

		public PageModelBuilder<R> currentPage(int currentPage) {
			this.currentPage = currentPage;
			return this;
		}

		public PageModelBuilder<R> pageSize(int pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public PageModelBuilder<R> data(List<R> data) {
			this.data = data;
			return this;
		}

		public PageResult<R> build() {
			PageResult<R> pageModel = new PageResult<>();
			pageModel.setTotalSize(totalSize);
			pageModel.setTotalPage(totalPage);
			pageModel.setCurrentPage(currentPage);
			pageModel.setPageSize(pageSize);
			pageModel.setData(data);
			return pageModel;
		}
	}
}
