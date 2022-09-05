package com.taotao.cloud.quartz;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = QuartzTest.App.class,
	properties = {
		"spring.application.name=taotao-cloud-dozer-test",
		"taotao.cloud.dozer.enabled=true"
	},
	webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class QuartzTest {

	@SpringBootApplication
	public static class App {

	}


	@Test
	void contextLoads() {
	}

}
