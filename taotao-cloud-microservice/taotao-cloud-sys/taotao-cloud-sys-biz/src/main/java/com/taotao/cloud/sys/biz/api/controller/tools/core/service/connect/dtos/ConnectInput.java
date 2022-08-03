package com.taotao.cloud.sys.biz.api.controller.tools.core.service.connect.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ConnectInput {
    /**
     * 模块名
     */
    @NotBlank
    private String module;
    /**
     * 基础名
     */
    @NotBlank
    private String baseName;
    /**
     * 配置类型
     * @examples yaml,json
     */
    @NotBlank
    private String configTypeName;
    /**
     * 数据所在组织
     */
    @NotBlank
    private String group;
    /**
     * 数据内容
     */
    private String content;

    public ConnectInput() {
    }

    public ConnectInput(String module,String baseName,String configTypeName, String group) {
        this.baseName = baseName;
        this.module = module;
        this.group = group;
        this.configTypeName = configTypeName;
    }
}
