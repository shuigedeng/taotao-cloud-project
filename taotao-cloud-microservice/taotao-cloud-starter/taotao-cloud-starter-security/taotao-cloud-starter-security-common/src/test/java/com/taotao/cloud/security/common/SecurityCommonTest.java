package com.taotao.cloud.security.common;


import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = SecurityCommonTest.App.class,
	webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class SecurityCommonTest {

	@SpringBootApplication
	public static class App {

	}


	@Test
	public void test() throws Exception {
		//NoticeData noticeData = new NoticeData();
		//noticeData.setType("template1");
		//Map<String, String> params = new HashMap<>();
		//params.put("code", "1234");
		//noticeData.setParams(params);
		//
		//boolean send = noticeService.send(noticeData, "15730445330");
	}
}
