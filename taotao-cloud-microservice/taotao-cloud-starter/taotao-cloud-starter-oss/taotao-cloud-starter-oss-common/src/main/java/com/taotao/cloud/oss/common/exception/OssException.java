package com.taotao.cloud.oss.common.exception;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;

/**
 * oss例外
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:35:06
 */
public class OssException extends BaseException {

	public OssException(String message) {
		super(message);
	}

	public OssException(Integer code, String message) {
		super(code, message);
	}

	public OssException(Throwable e) {
		super(e);
	}

	public OssException(String message, Throwable e) {
		super(message, e);
	}

	public OssException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public OssException(ResultEnum result) {
		super(result);
	}

	public OssException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
