package com.taotao.cloud.sys.biz.tools.core.dtos.param;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 测试日期数据输入
 */
public class DateRangeParam {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date begin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}
