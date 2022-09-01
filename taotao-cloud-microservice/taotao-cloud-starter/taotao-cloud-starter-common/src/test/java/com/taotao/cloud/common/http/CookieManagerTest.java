package com.taotao.cloud.common.http;


/**
 * cookie 管理测试
 *
 */
public class CookieManagerTest {

	public static void main(String[] args) {
		InMemoryCookieManager cookieManager = new InMemoryCookieManager();

		HttpRequest.get("https://demo.dreamlu.net/captcha.jpg")
			.cookieManager(cookieManager)
			.execute()
			.asString();

		HttpRequest.get("https://demo.dreamlu.net")
			.cookieManager(cookieManager)
			.execute()
			.asString();
	}

}
