package com.taotao.cloud.common.execl.core.exception;

import com.alibaba.excel.annotation.ExcelProperty;


public class ExceptionData {
    @ExcelProperty("姓名")
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
