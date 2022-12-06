package com.taotao.cloud.security.justauth;


import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = SecurityJustAuthTest.App.class,
	webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class SecurityJustAuthTest {

	@SpringBootApplication
	public static class App {

	}


	@Test
	public void test() throws Exception {
	}
}
