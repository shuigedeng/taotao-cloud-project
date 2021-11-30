package com.taotao.cloud.demo.http;


import com.taotao.cloud.common.http.HttpRequest;
import com.taotao.cloud.common.http.LogLevel;
import com.taotao.cloud.common.http.ResponseSpec;
import java.net.InetSocketAddress;
import org.junit.jupiter.api.Test;

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


	@Test
	public void http() {
		// 同步请求 url，方法支持 get、post、patch、put、delete
		HttpRequest.get("https://www.baidu.com")
			.useSlf4jLog() // 使用 Slf4j 日志，同类的有 .useConsoleLog(),日志级别为 BODY
			.addHeader("x-account-id", "mica001") // 添加 header
			.addCookie(builder -> builder.domain("www.baidu.com").name("name")
				.value("value"))  // 添加 cookie
			.query("q", "mica") // 设置 url 参数，默认进行 url encode
			.queryEncoded("name", "encodedValue")
			.retryOn(responseSpec -> !responseSpec.isOk()) // 对结果集进行断言重试
			.proxy(InetSocketAddress.createUnresolved("127.0.0.1", 8080)) // 设置代理
			.formBuilder()                  // 表单构造器，同类 multipartFormBuilder 文件上传表单
			.add("id", 123123)              // 表单参数
			.execute()                      // 发起请求
			.asJsonNode();                  // 结果集转换，注：如果网络异常等会直接抛出异常。
			// 同类的方法有 asString、asBytes
			// json 类响应：asJsonNode、asValue、asList、asMap、atJsonPath、，采用 jackson 处理
			// file 文件：toFile
	}

}
