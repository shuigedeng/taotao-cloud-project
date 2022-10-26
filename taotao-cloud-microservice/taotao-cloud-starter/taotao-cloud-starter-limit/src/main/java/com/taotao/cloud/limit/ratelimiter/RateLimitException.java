package com.taotao.cloud.limit.ratelimiter;

/**
 * 速率限制例外
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-26 08:56:35
 */
public class RateLimitException extends RuntimeException {

	private final long retryAfter;

	public RateLimitException(String message, long retryAfter) {
		super(message);
		this.retryAfter = retryAfter;
	}

	public long getRetryAfter() {
		return retryAfter;
	}
}
