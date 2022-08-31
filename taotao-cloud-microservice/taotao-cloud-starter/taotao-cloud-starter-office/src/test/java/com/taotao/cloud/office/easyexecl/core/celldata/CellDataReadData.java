package com.taotao.cloud.office.easyexecl.core.celldata;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.metadata.data.ReadCellData;


public class CellDataReadData {
    @DateTimeFormat("yyyy年MM月dd日")
    private ReadCellData<String> date;
    private ReadCellData<Integer> integer1;
    private Integer integer2;
    private ReadCellData<?> formulaValue;

	public ReadCellData<String> getDate() {
		return date;
	}

	public void setDate(ReadCellData<String> date) {
		this.date = date;
	}

	public ReadCellData<Integer> getInteger1() {
		return integer1;
	}

	public void setInteger1(ReadCellData<Integer> integer1) {
		this.integer1 = integer1;
	}

	public Integer getInteger2() {
		return integer2;
	}

	public void setInteger2(Integer integer2) {
		this.integer2 = integer2;
	}

	public ReadCellData<?> getFormulaValue() {
		return formulaValue;
	}

	public void setFormulaValue(ReadCellData<?> formulaValue) {
		this.formulaValue = formulaValue;
	}
}
