package com.taotao.cloud.excel.execl.core.skip;

import com.alibaba.excel.annotation.ExcelProperty;



/**

 */
public class SkipData {

    @ExcelProperty("姓名")
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
