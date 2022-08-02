package com.taotao.cloud.sys.biz.modules.core.dtos.param;

import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.math.NumberUtils;

@Setter
@ToString
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
}
