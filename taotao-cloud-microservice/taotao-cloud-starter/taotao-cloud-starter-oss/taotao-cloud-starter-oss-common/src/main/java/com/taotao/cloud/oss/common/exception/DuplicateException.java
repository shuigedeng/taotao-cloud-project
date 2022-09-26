package com.taotao.cloud.oss.common.exception;

import com.taotao.cloud.common.enums.ResultEnum;

/**
 * 重复例外
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:35:04
 */
public class DuplicateException extends OssException {

	public DuplicateException(String message) {
		super(message);
	}

	public DuplicateException(Integer code, String message) {
		super(code, message);
	}

	public DuplicateException(Throwable e) {
		super(e);
	}

	public DuplicateException(String message, Throwable e) {
		super(message, e);
	}

	public DuplicateException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public DuplicateException(ResultEnum result) {
		super(result);
	}

	public DuplicateException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
