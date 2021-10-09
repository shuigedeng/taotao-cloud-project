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
package com.taotao.cloud.uc.api.query.resource;

import com.taotao.cloud.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 资源分页查询query
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:29:15
 */
@Schema(name = "ResourcePageQuery", description = "资源查询query")
public class ResourcePageQuery extends PageQuery {

	private static final long serialVersionUID = -7605952923416404638L;

	public ResourcePageQuery() {
	}

	public static ResourcePageQueryBuilder builder() {
		return new ResourcePageQueryBuilder();
	}


	public static final class ResourcePageQueryBuilder {

		private Integer currentPage = 1;
		private Integer pageSize = 10;

		private ResourcePageQueryBuilder() {
		}

		public static ResourcePageQueryBuilder aResourcePageQuery() {
			return new ResourcePageQueryBuilder();
		}

		public ResourcePageQueryBuilder currentPage(Integer currentPage) {
			this.currentPage = currentPage;
			return this;
		}

		public ResourcePageQueryBuilder pageSize(Integer pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public ResourcePageQuery build() {
			ResourcePageQuery resourcePageQuery = new ResourcePageQuery();
			resourcePageQuery.setCurrentPage(currentPage);
			resourcePageQuery.setPageSize(pageSize);
			return resourcePageQuery;
		}
	}
}
