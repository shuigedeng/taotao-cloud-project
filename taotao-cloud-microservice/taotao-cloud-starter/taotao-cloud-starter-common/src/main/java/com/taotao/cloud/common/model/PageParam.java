package com.taotao.cloud.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 查询参数
 */
public class PageParam implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前第几页
	 */
	@Schema(description = "当前第几页，默认1", example = "1", required = true)
	@NotNull(message = "当前页显示数量不能为空")
	@Min(value = 0)
	@Max(value = Integer.MAX_VALUE)
	private Integer currentPage;

	/**
	 * 每页显示条数
	 */
	@Schema(description = "每页显示条数，默认10", example = "10", required = true)
	@NotNull(message = "每页数据显示数量不能为空")
	@Min(value = 5)
	@Max(value = 100)
	private Integer pageSize;

	/**
	 * 排序字段
	 */
	@Schema(description = "排序字段")
	private String sort;

	/**
	 * 排序方式 asc/desc
	 */
	@Schema(description = "排序方式 asc/desc")
	private String order;

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
