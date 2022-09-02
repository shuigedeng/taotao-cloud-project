package com.taotao.cloud.core.async.exception;

/**
 * 如果任务在执行之前，自己后面的任务已经执行完或正在被执行，则抛该exception
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-30 13:24:16
 */
public class SkippedException extends RuntimeException {

	/**
	 * 跳过例外
	 *
	 * @since 2022-05-30 13:24:17
	 */
	public SkippedException() {
        super();
    }

	/**
	 * 跳过例外
	 *
	 * @param message 消息
	 * @since 2022-05-30 13:24:18
	 */
	public SkippedException(String message) {
        super(message);
    }
}
