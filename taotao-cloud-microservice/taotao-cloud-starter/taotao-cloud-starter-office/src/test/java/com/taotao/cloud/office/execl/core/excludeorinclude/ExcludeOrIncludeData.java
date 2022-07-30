package com.taotao.cloud.office.execl.core.excludeorinclude;

import com.alibaba.excel.annotation.ExcelProperty;


public class ExcludeOrIncludeData {
    @ExcelProperty(order = 1)
    private String column1;
    @ExcelProperty(order = 2)
    private String column2;
    @ExcelProperty(order = 3)
    private String column3;
    @ExcelProperty(order = 4)
    private String column4;

	public String getColumn1() {
		return column1;
	}

	public void setColumn1(String column1) {
		this.column1 = column1;
	}

	public String getColumn2() {
		return column2;
	}

	public void setColumn2(String column2) {
		this.column2 = column2;
	}

	public String getColumn3() {
		return column3;
	}

	public void setColumn3(String column3) {
		this.column3 = column3;
	}

	public String getColumn4() {
		return column4;
	}

	public void setColumn4(String column4) {
		this.column4 = column4;
	}
}
