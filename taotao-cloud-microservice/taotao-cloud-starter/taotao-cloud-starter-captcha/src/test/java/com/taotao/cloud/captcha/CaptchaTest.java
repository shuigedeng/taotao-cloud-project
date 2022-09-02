package com.taotao.cloud.captcha;

import com.taotao.cloud.captcha.service.CaptchaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@SpringBootTest(classes = CaptchaTest.App.class,
	properties = {
		"spring.application.name=taotao-cloud-captcha-test",
		"taotao.cloud.captcha.enabled=true"
	},
	webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class CaptchaTest {

	@SpringBootApplication
	public static class App {

	}

	@Autowired
	private CaptchaService captchaService;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(captchaService);
		String s = captchaService.captchaType();
		Assertions.assertEquals("sdfl", s);
	}

}
