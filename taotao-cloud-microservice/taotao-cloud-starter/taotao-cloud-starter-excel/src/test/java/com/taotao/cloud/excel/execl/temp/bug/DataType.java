package com.taotao.cloud.excel.execl.temp.bug;

import com.alibaba.excel.annotation.ExcelProperty;



public class DataType {
    /**
     * 任务id
     */
    @ExcelProperty("任务ID")
    private Integer id;

    @ExcelProperty("多余字段1")
    private String firstSurplus;

    @ExcelProperty("多余字段2")
    private String secSurplus;

    @ExcelProperty("多余字段3")
    private String thirdSurplus;

    @ExcelProperty(value = "备注1")
    private String firstRemark;

    @ExcelProperty(value = "备注2")
    private String secRemark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstSurplus() {
		return firstSurplus;
	}

	public void setFirstSurplus(String firstSurplus) {
		this.firstSurplus = firstSurplus;
	}

	public String getSecSurplus() {
		return secSurplus;
	}

	public void setSecSurplus(String secSurplus) {
		this.secSurplus = secSurplus;
	}

	public String getThirdSurplus() {
		return thirdSurplus;
	}

	public void setThirdSurplus(String thirdSurplus) {
		this.thirdSurplus = thirdSurplus;
	}

	public String getFirstRemark() {
		return firstRemark;
	}

	public void setFirstRemark(String firstRemark) {
		this.firstRemark = firstRemark;
	}

	public String getSecRemark() {
		return secRemark;
	}

	public void setSecRemark(String secRemark) {
		this.secRemark = secRemark;
	}
}
