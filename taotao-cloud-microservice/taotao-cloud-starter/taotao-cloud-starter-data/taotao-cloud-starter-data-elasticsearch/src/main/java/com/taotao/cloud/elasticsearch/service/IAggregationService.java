/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

import java.io.IOException;
import java.util.Map;

/**
 * 聚合服务
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:23:50
 */
public interface IAggregationService {

	/**
	 * 访问统计聚合查询
	 *
	 * @param indexName 索引名
	 * @param routing   es的路由
	 * @return {@link Map }<{@link String }, {@link Object }>
	 * @since 2022-04-27 17:23:50
	 */
	Map<String, Object> requestStatAgg(String indexName, String routing) throws IOException;
}
