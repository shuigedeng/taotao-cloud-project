package com.taotao.cloud.uc.api.query.role;

import com.taotao.cloud.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 角色分页查询query
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "RolePageQuery", description = "角色查询query")
public class RolePageQuery extends PageQuery {

	private static final long serialVersionUID = -7605952923416404638L;

	public RolePageQuery() {
	}

	public RolePageQuery(Integer currentPage, Integer pageSize) {
		super(currentPage, pageSize);
	}

	public static RolePageQueryBuilder builder() {
		return new RolePageQueryBuilder();
	}

	public static final class RolePageQueryBuilder {

		private Integer currentPage = 1;
		private Integer pageSize = 10;

		private RolePageQueryBuilder() {
		}

		public static RolePageQueryBuilder aRolePageQuery() {
			return new RolePageQueryBuilder();
		}

		public RolePageQueryBuilder currentPage(Integer currentPage) {
			this.currentPage = currentPage;
			return this;
		}

		public RolePageQueryBuilder pageSize(Integer pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public RolePageQuery build() {
			RolePageQuery rolePageQuery = new RolePageQuery();
			rolePageQuery.setCurrentPage(currentPage);
			rolePageQuery.setPageSize(pageSize);
			return rolePageQuery;
		}
	}
}
