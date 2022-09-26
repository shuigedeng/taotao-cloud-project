package com.taotao.cloud.oss.common.exception;

import com.taotao.cloud.common.enums.ResultEnum;

/**
 * 不支持例外
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:35:08
 */
public class NotSupportException extends OssException {
	public NotSupportException(String message) {
		super(message);
	}

	public NotSupportException(Integer code, String message) {
		super(code, message);
	}

	public NotSupportException(Throwable e) {
		super(e);
	}

	public NotSupportException(String message, Throwable e) {
		super(message, e);
	}

	public NotSupportException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public NotSupportException(ResultEnum result) {
		super(result);
	}

	public NotSupportException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
