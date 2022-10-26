package com.taotao.cloud.limit.ratelimiter;

/**
 * 执行功能异常
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-26 08:57:00
 */
public class ExecuteFunctionException extends RuntimeException {

	public ExecuteFunctionException(String message, Throwable cause) {
		super(message, cause);
	}
}
