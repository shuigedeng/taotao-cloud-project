package com.taotao.cloud.ttcrpc.registry.apiregistry.base;


import com.taotao.cloud.common.extension.StringUtils;

public class ApiRegistryHttpStateException extends ApiRegistryException {
    private String appName;
    private String url;
    private int stateCode;
    public ApiRegistryHttpStateException(String appName,String url,int stateCode){
        super("url:"+ StringUtils.nullToEmpty(url)+",返回状态码:"+stateCode);
        this.appName = appName;
        this.url=url;
        this.stateCode=stateCode;
    }

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}
}
