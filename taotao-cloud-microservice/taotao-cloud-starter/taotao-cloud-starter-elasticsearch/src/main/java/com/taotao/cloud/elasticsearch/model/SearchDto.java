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
package com.taotao.cloud.elasticsearch.model;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SearchDto
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/3 07:49
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SearchDto implements Serializable {

	private static final long serialVersionUID = -2084416068307485742L;
	/**
	 * 搜索关键字
	 */
	private String queryStr;
	/**
	 * 当前页数
	 */
	private Integer page;
	/**
	 * 每页显示数
	 */
	private Integer limit;
	/**
	 * 排序字段
	 */
	private String sortCol;
	/**
	 * 是否显示高亮
	 */
	private Boolean isHighlighter;
	/**
	 * es的路由
	 */
	private String routing;
}
