package com.taotao.cloud.sys.biz.tools.dubbo;

import com.alibaba.dubbo.common.URL;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.codec.binary.Base64;

import java.util.List;

public class DubboProviderDto {
    private String origin;

    private String address;
    private String group;
    private String version;
    private String serviceClassName;
    private String application;
    // dubbo 版本
    private String dubbo;
    private long timestamp;
    private String methods;

    public DubboProviderDto(String origin, String address) {
        this.origin = origin;
        this.address = address;
    }

    public DubboProviderDto() {
    }

    public void config(String serviceInterface, String group, String version, String methods, String dubbo, long timestamp, String application) {
        this.serviceClassName = serviceInterface;
        this.group = group;
        this.version = version;
        this.methods = methods;
        this.dubbo = dubbo;
        this.timestamp = timestamp;
        this.application = application;
    }

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getServiceClassName() {
		return serviceClassName;
	}

	public void setServiceClassName(String serviceClassName) {
		this.serviceClassName = serviceClassName;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getDubbo() {
		return dubbo;
	}

	public void setDubbo(String dubbo) {
		this.dubbo = dubbo;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}
}
