package com.taotao.cloud.auth.biz.authentication.miniapp.service;

/**
 * 缓存sessionKey
 */
public interface MiniAppSessionKeyCacheService {

	/**
	 * Put sessionKey.
	 *
	 * @param cacheKey   {@code clientId::openId}
	 * @param sessionKey the session key
	 * @return sessionKey
	 */
	String put(String cacheKey, String sessionKey);

	/**
	 * Get sessionKey.
	 *
	 * @param cacheKey {@code clientId::openId}
	 * @return sessionKey
	 */
	String get(String cacheKey);
}