package com.taotao.cloud.retry.exception;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;

/**
 * 当抛出这个异常的时候，表示需要重试
 */
public class GuavaRetryException extends BaseException {

	public GuavaRetryException(String message) {
		super(message);
	}

	public GuavaRetryException(Integer code, String message) {
		super(code, message);
	}

	public GuavaRetryException(Throwable e) {
		super(e);
	}

	public GuavaRetryException(String message, Throwable e) {
		super(message, e);
	}

	public GuavaRetryException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public GuavaRetryException(ResultEnum result) {
		super(result);
	}

	public GuavaRetryException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
