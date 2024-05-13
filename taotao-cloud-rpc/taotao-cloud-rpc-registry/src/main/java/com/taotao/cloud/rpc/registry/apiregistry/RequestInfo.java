package com.taotao.cloud.rpc.registry.apiregistry;


import java.util.HashMap;
import java.util.Map;

public class RequestInfo {
     String appName;
     Map<String,String> header=new HashMap<>();
     String url;
     byte[] body;
     String method;
	public RequestInfo(){}
	public RequestInfo(String appName, Map<String, String> header, String url, byte[] body, String method) {
		this.appName = appName;
		this.header = header;
		this.url = url;
		this.body = body;
		this.method = method;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
