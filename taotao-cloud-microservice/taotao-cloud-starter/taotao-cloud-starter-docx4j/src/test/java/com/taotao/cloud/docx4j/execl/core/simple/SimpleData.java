package com.taotao.cloud.docx4j.execl.core.simple;

import com.alibaba.excel.annotation.ExcelProperty;



/**

 */
public class SimpleData {
    @ExcelProperty("姓名")
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
