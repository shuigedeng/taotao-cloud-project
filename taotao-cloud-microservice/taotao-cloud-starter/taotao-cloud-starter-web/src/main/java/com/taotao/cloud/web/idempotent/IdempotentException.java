package com.taotao.cloud.web.idempotent;

import com.taotao.cloud.common.exception.BaseException;

/**
 * 幂等校验异常
 *
 * @author shuigedeng
 */
public class IdempotentException extends BaseException {

	private static final long serialVersionUID = -851115183208290929L;

	public IdempotentException(String message) {
		super(message);
	}
}
