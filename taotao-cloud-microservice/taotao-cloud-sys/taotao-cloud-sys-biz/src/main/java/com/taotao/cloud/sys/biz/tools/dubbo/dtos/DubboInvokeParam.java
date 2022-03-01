
package com.taotao.cloud.sys.biz.tools.dubbo.dtos;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONArray;


public class DubboInvokeParam  {
    @NotNull
    private String connName;
    @NotNull
    private String serviceName;
    private String classloaderName;
    private String methodName;
    private JSONArray args;
    @NotNull
    private String providerURL;

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getClassloaderName() {
		return classloaderName;
	}

	public void setClassloaderName(String classloaderName) {
		this.classloaderName = classloaderName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public JSONArray getArgs() {
		return args;
	}

	public void setArgs(JSONArray args) {
		this.args = args;
	}

	public String getProviderURL() {
		return providerURL;
	}

	public void setProviderURL(String providerURL) {
		this.providerURL = providerURL;
	}
}
