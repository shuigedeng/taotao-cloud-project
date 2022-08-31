package com.taotao.cloud.office.easyexecl.core.head;

import com.alibaba.excel.annotation.ExcelProperty;


public class ComplexHeadData {
    @ExcelProperty({"顶格", "顶格", "两格"})
    private String string0;
    @ExcelProperty({"顶格", "顶格", "两格"})
    private String string1;
    @ExcelProperty({"顶格", "四联", "四联"})
    private String string2;
    @ExcelProperty({"顶格", "四联", "四联"})
    private String string3;
    @ExcelProperty({"顶格"})
    private String string4;

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

	public String getString2() {
		return string2;
	}

	public void setString2(String string2) {
		this.string2 = string2;
	}

	public String getString3() {
		return string3;
	}

	public void setString3(String string3) {
		this.string3 = string3;
	}

	public String getString4() {
		return string4;
	}

	public void setString4(String string4) {
		this.string4 = string4;
	}
}
