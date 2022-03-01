package com.taotao.cloud.sys.biz.tools.core.service.connect.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.io.Resource;

/**
 * 连接模板
 */
public class ConnectTemplate {
    private String module;
    @JsonIgnore
    private Resource resource;
    private String configTypeName;
    private String content;

    public ConnectTemplate() {
    }

    public ConnectTemplate(String module, Resource resource, String configTypeName) {
        this.module = module;
        this.resource = resource;
        this.configTypeName = configTypeName;
    }

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getConfigTypeName() {
		return configTypeName;
	}

	public void setConfigTypeName(String configTypeName) {
		this.configTypeName = configTypeName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
