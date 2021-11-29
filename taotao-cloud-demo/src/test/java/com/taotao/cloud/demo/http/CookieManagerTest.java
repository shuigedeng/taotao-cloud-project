package com.taotao.cloud.demo.http;


import com.taotao.cloud.common.http.HttpRequest;
import com.taotao.cloud.common.http.InMemoryCookieManager;

/**
 * cookie 管理测试
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
