package com.taotao.cloud.sys.biz.tools.core.dtos.param;

import org.apache.commons.lang3.math.NumberUtils;

public class PageParam {
	/**
	 * 页号
	 */
	private String pageNo;
	/**
	 * 页大小
	 */
	private String pageSize;

	public Integer getPageNo() {
		return NumberUtils.toInt(pageNo,1);
	}

	public Integer getPageSize() {
		return NumberUtils.toInt(pageSize,10);
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
}
