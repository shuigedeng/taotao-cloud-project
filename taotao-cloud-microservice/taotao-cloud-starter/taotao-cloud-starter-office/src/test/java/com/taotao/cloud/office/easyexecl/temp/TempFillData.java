package com.taotao.cloud.office.easyexecl.temp;

import com.alibaba.excel.annotation.write.style.ContentRowHeight;



/**

 */
@ContentRowHeight(30)
public class TempFillData {
    private String name;
    private double number;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNumber() {
		return number;
	}

	public void setNumber(double number) {
		this.number = number;
	}
}
