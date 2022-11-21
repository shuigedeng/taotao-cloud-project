package com.taotao.cloud.office.utils.easyexcel.other2;

import java.util.ArrayList;
import java.util.List;

public class ExcelCheckResult<T> {

	private List<T> successDtos;

	private List<ExcelCheckErrDto<T>> errDtos;

	public ExcelCheckResult(List<T> successDtos, List<ExcelCheckErrDto<T>> errDtos) {
		this.successDtos = successDtos;
		this.errDtos = errDtos;
	}

	public ExcelCheckResult(List<ExcelCheckErrDto<T>> errDtos) {
		this.successDtos = new ArrayList<>();
		this.errDtos = errDtos;
	}

	public List<T> getSuccessDtos() {
		return successDtos;
	}

	public void setSuccessDtos(List<T> successDtos) {
		this.successDtos = successDtos;
	}

	public List<ExcelCheckErrDto<T>> getErrDtos() {
		return errDtos;
	}

	public void setErrDtos(
		List<ExcelCheckErrDto<T>> errDtos) {
		this.errDtos = errDtos;
	}
}
