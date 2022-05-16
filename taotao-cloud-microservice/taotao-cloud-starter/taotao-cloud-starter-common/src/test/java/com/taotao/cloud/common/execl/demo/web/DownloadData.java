package com.taotao.cloud.common.execl.demo.web;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;



/**
 * 基础数据类
 *

 */
public class DownloadData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getDoubleData() {
		return doubleData;
	}

	public void setDoubleData(Double doubleData) {
		this.doubleData = doubleData;
	}
}
