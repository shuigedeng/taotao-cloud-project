package com.taotao.cloud.uc.api.query.resource;

import com.taotao.cloud.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 资源分页查询query
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
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
