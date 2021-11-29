package com.taotao.cloud.demo.http;


import com.taotao.cloud.common.http.HttpRequest;
import com.taotao.cloud.common.http.LogLevel;
import com.taotao.cloud.common.http.ResponseSpec;

public class HttpTest {

	public static void main(String[] args) {
		String html = HttpRequest.get("https://wwww.baiduxxx.com/123123")
			.useSlf4jLog(LogLevel.NONE)
			.execute()
			.onFailed((request, e) -> {
				e.printStackTrace();
				ResponseSpec response = e.getResponse();
				System.out.println(response.asString());
			})
			.onSuccessful(ResponseSpec::asString);
		System.out.println(html);
	}

}
