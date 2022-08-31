package com.taotao.cloud.office.easyexecl.core.encrypt;

import com.alibaba.excel.annotation.ExcelProperty;


public class EncryptData {
    @ExcelProperty("姓名")
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
