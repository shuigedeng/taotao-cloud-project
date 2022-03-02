package com.taotao.cloud.sys.biz.forest;

import com.dtflys.forest.annotation.DataFile;
import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.XMLBody;
import com.dtflys.forest.callback.OnProgress;
import com.dtflys.forest.extensions.BasicAuth;
import com.dtflys.forest.extensions.DownloadFile;
import com.dtflys.forest.extensions.OAuth2;
import com.dtflys.forest.http.ForestRequest;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface AmapClient {

	/**
	 * 聪明的你一定看出来了@Get注解代表该方法专做GET请求 在url中的{0}代表引用第一个参数，{1}引用第二个参数
	 */
	@Get("http://ditu.amap.com/service/regeo?longitude={0}&latitude={1}")
	Map getLocation(String longitude, String latitude);

	/**
	 * 将对象参数解析为JSON字符串，并放在请求的Body进行传输
	 */
	//@Post("/register")
	//String registerUser(@JSONBody MyUser user);

	/**
	 * 将Map类型参数解析为JSON字符串，并放在请求的Body进行传输
	 */
	@Post("/test/json")
	String postJsonMap(@JSONBody Map mapObj);

	/**
	 * 直接传入一个JSON字符串，并放在请求的Body进行传输
	 */
	@Post("/test/json")
	String postJsonText(@JSONBody String jsonText);

	/**
	 * 将一个通过JAXB注解修饰过的类型对象解析为XML字符串
	 * 并放在请求的Body进行传输
	 */
	//@Post("/message")
	//String sendXmlMessage(@XMLBody MyMessage message);

	/**
	 * 直接传入一个XML字符串，并放在请求的Body进行传输
	 */
	@Post("/test/xml")
	String postXmlBodyString(@XMLBody String xml);

	/**
	 * ProtobufProto.MyMessage 为 Protobuf 生成的数据类
	 * 将 Protobuf 生成的数据对象转换为 Protobuf 格式的字节流
	 * 并放在请求的Body进行传输
	 *
	 * 注: 需要引入 google protobuf 依赖
	 */
	//@Post(url = "/message", contentType = "application/octet-stream")
	//String sendProtobufMessage(@ProtobufBody ProtobufProto.MyMessage message);

	/**
	 * 用@DataFile注解修饰要上传的参数对象 OnProgress参数为监听上传进度的回调函数
	 */
	@Post("/upload")
	Map upload(@DataFile("file") String filePath, OnProgress onProgress);

	//Map result = myClient.upload("D:\\TestUpload\\xxx.jpg", progress -> {
	//	System.out.println("progress: " + Math.round(progress.getRate() * 100) + "%");  // 已上传百分比
	//	if (progress.isDone()) {   // 是否上传完成
	//		System.out.println("--------   Upload Completed!   --------");
	//	}
	//});

	/**
	 * 上传Map包装的文件列表，其中 {_key} 代表Map中每一次迭代中的键值
	 */
	@Post("/upload")
	ForestRequest<Map> uploadByteArrayMap(
		@DataFile(value = "file", fileName = "{_key}") Map<String, byte[]> byteArrayMap);

	/**
	 * 上传List包装的文件列表，其中 {_index} 代表每次迭代List的循环计数（从零开始计）
	 */
	@Post("/upload")
	ForestRequest<Map> uploadByteArrayList(
		@DataFile(value = "file", fileName = "test-img-{_index}.jpg") List<byte[]> byteArrayList);

	/**
	 * 在方法上加上@DownloadFile注解 dir属性表示文件下载到哪个目录 OnProgress参数为监听上传进度的回调函数 {0}代表引用第一个参数
	 */
	@Get("http://localhost:8080/images/xxx.jpg")
	@DownloadFile(dir = "{0}")
	File downloadFile(String dir, OnProgress onProgress);

	//File file = myClient.downloadFile("D:\\TestDownload", progress -> {
	//	System.out.println("progress: " + Math.round(progress.getRate() * 100) + "%");  // 已下载百分比
	//	if (progress.isDone()) {   // 是否下载完成
	//		System.out.println("--------   Download Completed!   --------");
	//	}
	//});

	@Post("/hello/user?username={username}")
	@BasicAuth(username = "{username}", password = "bar")
	String send(@DataVariable("username") String username);

	@OAuth2(
		tokenUri = "/auth/oauth/token",
		clientId = "password",
		clientSecret = "xxxxx-yyyyy-zzzzz",
		grantType = OAuth2.GrantType.PASSWORD,
		scope = "any",
		username = "root",
		password = "xxxxxx"
	)
	@Get("/test/data")
	String getData();
}
