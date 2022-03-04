//package com.taotao.cloud.sys.biz.forest;
//
//import com.dtflys.forest.annotation.DataVariable;
//import com.dtflys.forest.annotation.Get;
//import com.dtflys.forest.annotation.Request;
//
//public interface MyClient {
//
//	@Request("http://localhost:8080/hello")
//	String helloForest();
//
//	/**
//	 * 在请求接口上加上自定义的 @MyAuth 注解
//	 * 注解的参数可以是字符串模板，通过方法调用的时候动态传入
//	 * 也可以是写死的字符串
//	 */
//	@Get("/hello/user?username={username}")
//	@MyAuth(username = "{username}", password = "bar")
//	String send(@DataVariable("username") String username);
//}
