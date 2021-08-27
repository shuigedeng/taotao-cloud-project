package com.taotao.cloud.health.base;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: huojuncheng
 * @version: 2020-08-25 15:10
 **/
public class FlyBookProperties {
    public static String Domain="https://open.feishu.cn/open-apis/bot/hook/";
    public static String flyBookUrl="bsf.message.flybook.url";

	public static String getDomain() {
		return Domain;
	}

	public static void setDomain(String domain) {
		Domain = domain;
	}

	public static String getFlyBookUrl() {
		return flyBookUrl;
	}

	public static void setFlyBookUrl(String flyBookUrl) {
		FlyBookProperties.flyBookUrl = flyBookUrl;
	}
}
