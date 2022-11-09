package com.taotao.cloud.auth.biz.authentication.miniapp.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * The interface Channel user details service.
 */
public interface MiniAppUserDetailsService {

	/**
	 * 小程序在微信登录成功后发起后端登录用来注册的方法
	 *
	 * @param request the request
	 * @return the user details
	 */
	UserDetails register(MiniAppRequest request, String sessionKey);

	/**
	 * openid登录
	 * <p>
	 * clientId和openId决定唯一性
	 *
	 * @param clientId the client id
	 * @param openId   the open id
	 * @return the user details
	 */
	UserDetails loadByOpenId(String clientId, String openId);

}
