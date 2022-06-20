package com.taotao.cloud.auth.biz.authentication.miniapp;

@FunctionalInterface
public interface MiniAppClientService {

	/**
	 * Get mini app client.
	 *
	 * @param clientId the client id
	 * @return {@link MiniAppClient}
	 * @see MiniAppClient#getAppId() MiniAppClient#getAppId()
	 * @see MiniAppClient#getSecret() MiniAppClient#getSecret()
	 */
	MiniAppClient get(String clientId);
}
