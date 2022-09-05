package com.taotao.cloud.schedule.gloriaapi.common.exception;

import com.gloria.schedule.common.vo.CommonResult;

public class GlobalException extends RuntimeException {
	
	private final CommonResult<String> commonResult;
	
	public GlobalException(CommonResult<String> commonResult){
		super(commonResult.getMessage());
		this.commonResult = commonResult;
	}

	public CommonResult<String> getCommonResult() {
		return commonResult;
	}

	public GlobalException(String msg) {
		super(msg);
		this.commonResult = CommonResult.failed(msg);
	}

	public GlobalException(String msg, Throwable e) {
		super(msg, e);
		this.commonResult = CommonResult.failed(e.getMessage());;
	}

	public GlobalException(String msg, CommonResult<String> commonResult) {
		super(msg);
		this.commonResult = commonResult;
	}

	public GlobalException(String msg, Throwable e, CommonResult<String> commonResult) {
		super(msg, e);
		this.commonResult = commonResult;
	}
}
