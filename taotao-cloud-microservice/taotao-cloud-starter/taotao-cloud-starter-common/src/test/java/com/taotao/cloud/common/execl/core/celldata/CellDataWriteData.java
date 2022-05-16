package com.taotao.cloud.common.execl.core.celldata;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.metadata.data.WriteCellData;

import java.util.Date;


public class CellDataWriteData {
    @DateTimeFormat("yyyy年MM月dd日")
    private WriteCellData<Date> date;
    private WriteCellData<Integer> integer1;
    private Integer integer2;
    private WriteCellData<?> formulaValue;

	public WriteCellData<Date> getDate() {
		return date;
	}

	public void setDate(WriteCellData<Date> date) {
		this.date = date;
	}

	public WriteCellData<Integer> getInteger1() {
		return integer1;
	}

	public void setInteger1(WriteCellData<Integer> integer1) {
		this.integer1 = integer1;
	}

	public Integer getInteger2() {
		return integer2;
	}

	public void setInteger2(Integer integer2) {
		this.integer2 = integer2;
	}

	public WriteCellData<?> getFormulaValue() {
		return formulaValue;
	}

	public void setFormulaValue(WriteCellData<?> formulaValue) {
		this.formulaValue = formulaValue;
	}
}
