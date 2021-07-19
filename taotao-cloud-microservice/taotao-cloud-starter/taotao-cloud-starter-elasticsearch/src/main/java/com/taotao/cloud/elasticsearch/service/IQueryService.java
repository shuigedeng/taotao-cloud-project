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
package com.taotao.cloud.elasticsearch.service;

import com.taotao.cloud.core.model.PageModel;
import com.taotao.cloud.elasticsearch.model.LogicDelDto;
import com.taotao.cloud.elasticsearch.model.SearchDto;
import java.io.IOException;
import java.util.Map;

/**
 * 查询服务
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/3 08:01
 */
public interface IQueryService {

	/**
	 * 查询文档列表
	 *
	 * @param indexName 索引名
	 * @param searchDto 搜索Dto
	 * @return com.taotao.cloud.core.model.PageResult<java.lang.String>
	 * @author shuigedeng
	 * @since 2021/2/26 08:59
	 */
	PageModel<String> strQuery(String indexName, SearchDto searchDto) throws IOException;

	/**
	 * 查询文档列表
	 *
	 * @param indexName   索引名
	 * @param searchDto   搜索Dto
	 * @param logicDelDto 逻辑删除Dto
	 * @return com.taotao.cloud.core.model.PageResult<java.lang.String>
	 * @author shuigedeng
	 * @since 2021/2/26 08:59
	 */
	PageModel<String> strQuery(String indexName, SearchDto searchDto, LogicDelDto logicDelDto)
		throws IOException;

	/**
	 * 访问统计聚合查询
	 *
	 * @param indexName 索引名
	 * @param routing   es的路由
	 * @return java.util.Map<java.lang.String, java.lang.Object>
	 * @author shuigedeng
	 * @since 2021/2/26 08:59
	 */
	Map<String, Object> requestStatAgg(String indexName, String routing) throws IOException;
}
