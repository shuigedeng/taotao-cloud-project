package com.taotao.cloud.office.utils.easyexcel.other2;

public class ExcelCheckErrDto<T> {

	private T t;

	private String errMsg;

	public ExcelCheckErrDto() {
	}

	public ExcelCheckErrDto(T t, String errMsg) {
		this.t = t;
		this.errMsg = errMsg;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
