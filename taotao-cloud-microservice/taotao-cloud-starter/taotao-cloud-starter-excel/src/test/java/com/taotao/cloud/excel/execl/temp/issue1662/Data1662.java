package com.taotao.cloud.excel.execl.temp.issue1662;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;

public class Data1662 {
    @ExcelProperty(index = 0)
    private String str;
    @ExcelProperty(index = 1)
    private Date date;
    @ExcelProperty(index = 2)
    private double r;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}
}
