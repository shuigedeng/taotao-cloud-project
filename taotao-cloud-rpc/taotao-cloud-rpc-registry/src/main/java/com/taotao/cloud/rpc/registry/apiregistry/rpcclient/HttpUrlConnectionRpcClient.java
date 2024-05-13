package com.taotao.cloud.rpc.registry.apiregistry.rpcclient;

import com.taotao.cloud.rpc.registry.apiregistry.RequestInfo;
import java.lang.reflect.Type;

/**
 * HttpUrlConnection实现
 */
public class HttpUrlConnectionRpcClient implements IRpcClient {
	public <T> T execute(RequestInfo requestInfo, Type cls) {
		// val response = HttpUtils.request(new HttpUtils.HttpRequest(
		// 	requestInfo.getUrl(),
		// 	requestInfo.getMethod(),
		// 	requestInfo.getHeader(),
		// 	requestInfo.getBody(),
		// 	ApiRegistryProperties.getHttpUrlConnectionConnectTimeOut(),
		// 	ApiRegistryProperties.getHttpUrlConnectionReadTimeOut(),
		// 	ApiRegistryProperties.getHttpUrlConnectionPoolEnabled()));
		// if (!response.isSuccess()) {
		// 	throw new ApiRegistryHttpStateException(requestInfo.getAppName(), StringUtils.nullToEmpty(requestInfo.getUrl()), response.getCode());
		// }
		// val code = CodeFactory.create(response.getHeader().get("Content-Type"));
		// return code.decode(response.getBody(), cls);
		return null;
	}
}
