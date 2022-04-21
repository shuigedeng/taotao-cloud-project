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
package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.sys.api.dto.mongo.CollectionDto;
import com.taotao.cloud.sys.api.dto.mongo.MongoQueryParam;
import java.util.List;

/**
 * IMongoService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-23 08:59:28
 */
public interface IMongoService {

	/**
	 * 获取数据库名称
	 *

	 * @return 数据库名称
	 * @since 2022-03-25 14:30:04
	 */
	public List<String> databaseNames();


	/**
	 * 获取集合数据
	 *
	 * @param databaseName 数据库名称
	 * @return 集合数据
	 * @since 2022-03-25 14:30:30
	 */
	public List<CollectionDto> collectionNames(String databaseName);

	/**
	 * 分页 mongo 数据查询
	 *
	 * @param mongoQueryParam
	 * @param pageQuery
	 */
	public PageModel<String> queryDataPage(MongoQueryParam mongoQueryParam, PageQuery pageQuery);
}
