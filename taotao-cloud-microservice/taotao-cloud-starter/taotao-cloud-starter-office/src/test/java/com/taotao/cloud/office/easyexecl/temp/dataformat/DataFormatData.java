package com.taotao.cloud.office.easyexecl.temp.dataformat;

import com.alibaba.excel.metadata.data.ReadCellData;



/**
 * TODO
 *
 */
public class DataFormatData {
    private ReadCellData<String> date;
    private ReadCellData<String> num;

	public ReadCellData<String> getDate() {
		return date;
	}

	public void setDate(ReadCellData<String> date) {
		this.date = date;
	}

	public ReadCellData<String> getNum() {
		return num;
	}

	public void setNum(ReadCellData<String> num) {
		this.num = num;
	}
}
