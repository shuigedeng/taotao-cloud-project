package com.taotao.cloud.common.execl.core.sort;

import com.alibaba.excel.annotation.ExcelProperty;



/**

 */
public class SortData {
    private String column5;
    private String column6;
    @ExcelProperty(order = 100)
    private String column4;
    @ExcelProperty(order = 99)
    private String column3;
    @ExcelProperty(value = "column2", index = 1)
    private String column2;
    @ExcelProperty(value = "column1", index = 0)
    private String column1;

	public String getColumn5() {
		return column5;
	}

	public void setColumn5(String column5) {
		this.column5 = column5;
	}

	public String getColumn6() {
		return column6;
	}

	public void setColumn6(String column6) {
		this.column6 = column6;
	}

	public String getColumn4() {
		return column4;
	}

	public void setColumn4(String column4) {
		this.column4 = column4;
	}

	public String getColumn3() {
		return column3;
	}

	public void setColumn3(String column3) {
		this.column3 = column3;
	}

	public String getColumn2() {
		return column2;
	}

	public void setColumn2(String column2) {
		this.column2 = column2;
	}

	public String getColumn1() {
		return column1;
	}

	public void setColumn1(String column1) {
		this.column1 = column1;
	}
}
