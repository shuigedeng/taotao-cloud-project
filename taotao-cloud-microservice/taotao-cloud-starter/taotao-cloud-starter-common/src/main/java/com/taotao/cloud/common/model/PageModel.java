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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * 返回分页实体类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/29 15:40
 */
@Data
@Builder
@AllArgsConstructor
@Schema(description = "分页结果对象")
public class PageModel<T> implements Serializable {

	private static final long serialVersionUID = -275582248840137389L;

	@Schema(description = "总条数")
	private long totalSize;
	@Schema(description = "总页数")
	private int totalPage;
	@Schema(description = "当前第几页")
	private int currentPage;
	@Schema(description = "每页显示条数")
	private int pageSize;
	@Schema(description = "返回数据")
	private List<T> data;

	public PageModel() {
	}

	public static <T> PageModel<T> convertJpaPage(Page<T> page) {
		return of(
			page.getTotalElements(),
			page.getTotalPages(),
			page.getNumber(),
			page.getSize(),
			page.getContent()
		);
	}

	public static <T> PageModel<T> convertMybatisPage(IPage<T> page) {
		return of(
			page.getTotal(),
			(int) page.getPages(),
			(int) page.getCurrent(),
			(int) page.getSize(),
			page.getRecords()
		);
	}

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
}
