package com.taotao.cloud.dozer;

import com.github.dozermapper.core.Mapper;
import com.taotao.cloud.dozer.helper.DozerHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DozerTest.App.class,
	properties = {
		"spring.application.name=taotao-cloud-dozer-test",
		"taotao.cloud.dozer.enabled=true"
	},
	webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class DozerTest {

	@SpringBootApplication
	public static class App {

	}

	@Autowired
	private DozerHelper dozerHelper;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(dozerHelper);
		Mapper mapper = dozerHelper.getMapper();
		System.out.println(mapper);
	}

}
