package com.taotao.cloud.cache.caffeine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@SpringBootTest(classes = CaffeineTest.App.class,
	properties = {
		"spring.application.name=taotao-cloud-caffeine-test",
		"taotao.cloud.caffeine.enabled=true"
	},
	webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class CaffeineTest {

	@SpringBootApplication
	public static class App {

	}

	@Autowired
	private CacheManager cacheManager;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(cacheManager);
		Cache cache = cacheManager.getCache("test#5m");
		Assertions.assertNotNull(cache);
		cache.put("hello", "world");
		cache.get("hello");

	}

}
