package com.taotao.cloud.sys.biz.api.controller.tools.dubbo;

import com.alibaba.dubbo.common.URL;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.codec.binary.Base64;

import java.util.List;

@Data
public class DubboProviderDto {
    private String origin;

    private String address;
    private String group;
    private String version;
    private String serviceClassName;
    private String application;
    /**
     * dubbo 版本
     */
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
}
