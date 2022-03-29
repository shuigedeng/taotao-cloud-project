package com.taotao.cloud.common.exception;

import com.taotao.cloud.common.enums.ResultEnum;

/**
 * 通用的运行时异常
 * @author shuigedeng
 */
public final class CommonRuntimeException extends BaseException {

	public CommonRuntimeException(String message) {
		super(message);
	}

	public CommonRuntimeException(Integer code, String message) {
		super(code, message);
	}

	public CommonRuntimeException(Throwable e) {
		super(e);
	}

	public CommonRuntimeException(String message, Throwable e) {
		super(message, e);
	}

	public CommonRuntimeException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public CommonRuntimeException(ResultEnum result) {
		super(result);
	}

	public CommonRuntimeException(ResultEnum result,
		Throwable e) {
		super(result, e);
	}
}
