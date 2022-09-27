package com.taotao.cloud.sms.aliyun;


import com.taotao.cloud.sms.common.model.NoticeData;
import com.taotao.cloud.sms.common.service.NoticeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;


@SpringBootTest(classes = AliyunSmsClientTest.App.class,
	webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class AliyunSmsClientTest {
	@SpringBootApplication
	public static class App {

	}

	@Autowired
	private NoticeService noticeService;

	@Test
	public void test() throws Exception {
		NoticeData noticeData = new NoticeData();
		noticeData.setType("template1");
		Map<String, String> params = new HashMap<>();
		params.put("code", "1234");
		noticeData.setParams(params);

		boolean send = noticeService.send(noticeData, "15730445330");
	}
}
