package com.taotao.cloud.schedule.gloriaapi.common.vo;

/**
 * 错误码
 * @author Carlos  carlos_love_gloria@163.com
 * @since 2022/3/28
 * @version 1.0.0
 */
public interface IErrorCode {
	/**
	 * description: 错误码
	 * @return 错误码
	 * @since 2021/11/17 20:28
	 */
	long getCode();
	
	/**
	 * description:
	 * @return 错误信息
	 * @since 2021/11/17 20:28
	 */
	String getMessage();
}
