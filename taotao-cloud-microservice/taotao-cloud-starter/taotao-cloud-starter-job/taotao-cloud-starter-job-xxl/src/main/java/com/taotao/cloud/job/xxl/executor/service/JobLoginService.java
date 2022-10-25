package com.taotao.cloud.job.xxl.executor.service;

/**
 * 登录服务工作
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-25 09:45:16
 */
public interface JobLoginService {

	/**
	 * 登录
	 *
	 * @since 2022-10-25 09:45:16
	 */
	void login();

	/**
	 * 把饼干
	 *
	 * @return {@link String }
	 * @since 2022-10-25 09:45:16
	 */
	String getCookie();

}
