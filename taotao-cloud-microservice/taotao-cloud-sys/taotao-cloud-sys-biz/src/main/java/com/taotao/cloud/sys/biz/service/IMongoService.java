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

	public List<String> databaseNames();

	public List<CollectionDto> collectionNames(String databaseName);

	public PageModel<String> queryDataPage(MongoQueryParam mongoQueryParam, PageQuery pageQuery);
}
