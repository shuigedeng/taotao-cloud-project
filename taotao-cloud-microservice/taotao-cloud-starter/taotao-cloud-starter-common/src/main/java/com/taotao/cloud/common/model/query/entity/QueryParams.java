package com.taotao.cloud.common.model.query.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
* 查询参数
*/
@Schema(title = "查询参数")
public class QueryParams {

    @Schema(description= "参数集合")
    private List<QueryParam> queryParams;

    @Schema(description= "排序集合")
    private List<QueryOrder> queryOrders;

	public List<QueryParam> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(
		List<QueryParam> queryParams) {
		this.queryParams = queryParams;
	}

	public List<QueryOrder> getQueryOrders() {
		return queryOrders;
	}

	public void setQueryOrders(
		List<QueryOrder> queryOrders) {
		this.queryOrders = queryOrders;
	}
}
