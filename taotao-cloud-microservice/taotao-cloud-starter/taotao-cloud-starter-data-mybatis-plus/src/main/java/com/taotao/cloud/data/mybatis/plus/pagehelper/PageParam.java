package com.taotao.cloud.data.mybatis.plus.pagehelper;

import java.io.Serializable;

public class PageParam<T> implements Serializable {

	private static final long serialVersionUID = -7916211163897873899L;
	private int pageNum = 1;
	private int pageSize = 10;
	//条件参数
	private T param;
	// 排序字段
	private String orderBy;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public T getParam() {
		return param;
	}

	public void setParam(T param) {
		this.param = param;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(
		String orderBy) {
		//需要注意sql注入
		this.orderBy = orderBy;
	}
}

//分页结果类



