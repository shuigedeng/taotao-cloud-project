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
import com.taotao.cloud.elasticsearch.model.SearchDto;
import java.io.IOException;

/**
 * 搜索服务
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/3 08:01
 */
public interface ISearchService {

	/**
	 * StringQuery通用搜索
	 *
	 * @param indexName 索引名
	 * @param searchDto 搜索Dto
	 * @author dengtao
	 * @since 2020/9/29 15:37
	 */
	PageModel<String> strQuery(String indexName, SearchDto searchDto) throws IOException;
}
