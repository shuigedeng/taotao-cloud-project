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
public record PageModel<R>(
	/**
	 * 总条数
	 */
	@Schema(description = "总条数")
	long totalSize,
	/**
	 * 总页数
	 */
	@Schema(description = "总页数")
	int totalPage,
	/**
	 * 当前第几页
	 */
	@Schema(description = "当前第几页")
	int currentPage,
	/**
	 * 每页显示条数
	 */
	@Schema(description = "每页显示条数")
	int pageSize,
	/**
	 * 返回数据
	 */
	@Schema(description = "返回数据")
	List<R> data) implements Serializable {

	@Serial
	private static final long serialVersionUID = -275582248840137389L;

	/**
	 * convertJpaPage
	 *
	 * @param page page
	 * @param <R>  R
	 * @return {@link PageModel }
	 * @author shuigedeng
	 * @since 2021-09-02 19:10:45
	 */
	public static <R, T> PageModel<R> convertJpaPage(Page<T> page, Class<R> r) {
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

	/**
	 * convertMybatisPage
	 *
	 * @param page page
	 * @param <R>  R
	 * @return {@link PageModel }
	 * @author shuigedeng
	 * @since 2021-09-02 19:10:49
	 */
	public static <R, T> PageModel<R> convertMybatisPage(IPage<T> page, Class<R> r) {
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

	/**
	 * of
	 *
	 * @param totalSize   totalSize
	 * @param totalPage   totalPage
	 * @param currentPage currentPage
	 * @param pageSize    pageSize
	 * @param data        data
	 * @param <R>         R
	 * @return {@link com.taotao.cloud.common.model.PageModel }
	 * @author shuigedeng
	 * @since 2021-09-02 19:11:10
	 */
	public static <R> PageModel<R> of(
		long totalSize,
		int totalPage,
		int currentPage,
		int pageSize,
		List<R> data) {
		return new PageModel<>(totalSize, totalPage, currentPage, pageSize, data);
	}
}
