package com.taotao.cloud.docx4j.execl.core.template;

import com.alibaba.excel.annotation.ExcelProperty;



/**

 */
public class TemplateData {
    @ExcelProperty("字符串0")
    private String string0;
    @ExcelProperty("字符串1")
    private String string1;

	public String getString0() {
		return string0;
	}

	public void setString0(String string0) {
		this.string0 = string0;
	}

	public String getString1() {
		return string1;
	}

	public void setString1(String string1) {
		this.string1 = string1;
	}
}
