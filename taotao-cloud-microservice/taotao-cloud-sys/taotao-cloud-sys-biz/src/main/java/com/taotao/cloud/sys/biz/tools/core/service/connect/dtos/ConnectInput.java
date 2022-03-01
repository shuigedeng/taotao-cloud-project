package com.taotao.cloud.sys.biz.tools.core.service.connect.dtos;


import javax.validation.constraints.NotBlank;

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
    // yaml json
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

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public String getConfigTypeName() {
		return configTypeName;
	}

	public void setConfigTypeName(String configTypeName) {
		this.configTypeName = configTypeName;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
