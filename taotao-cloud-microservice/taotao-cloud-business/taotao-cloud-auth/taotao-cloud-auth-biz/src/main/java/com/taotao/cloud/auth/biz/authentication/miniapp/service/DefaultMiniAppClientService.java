package com.taotao.cloud.auth.biz.authentication.miniapp.service;

import org.springframework.stereotype.Service;

/**
 * // 根据请求携带的clientid 查询小程序的appid和secret 1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
 */
@Service
public class DefaultMiniAppClientService implements MiniAppClientService {

	@Override
	public MiniAppClient get(String clientId) {
		MiniAppClient miniAppClient = new MiniAppClient();
		miniAppClient.setClientId(clientId);
		miniAppClient.setAppId("wxcd395c35c45eb823");
		miniAppClient.setSecret("75f9a12c82bd24ecac0d37bf1156c749");
		return miniAppClient;
	}
}

