package com.taotao.cloud.sys.biz.api.controller.tools.core.service.connect.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.core.io.Resource;

/**
 * 连接模板
 */
@Data
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
}
