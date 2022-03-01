package com.taotao.cloud.sys.biz.tools.database.service;


import com.taotao.cloud.sys.biz.tools.database.dtos.CodeGeneratorConfig;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PreviewCodeParam {
    @NotNull
    private String template;
    @NotNull
    private String connName;
    @Valid
    private ActualTableName actualTableName;
    private String renameStrategyName;
    @Valid
    private CodeGeneratorConfig.PackageConfig packageConfig;

    public PreviewCodeParam() {
    }

    public PreviewCodeParam(String template, String connName, ActualTableName actualTableName, String renameStrategyName) {
        this.template = template;
        this.connName = connName;
        this.actualTableName = actualTableName;
        this.renameStrategyName = renameStrategyName;
    }

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public ActualTableName getActualTableName() {
		return actualTableName;
	}

	public void setActualTableName(ActualTableName actualTableName) {
		this.actualTableName = actualTableName;
	}

	public String getRenameStrategyName() {
		return renameStrategyName;
	}

	public void setRenameStrategyName(String renameStrategyName) {
		this.renameStrategyName = renameStrategyName;
	}

	public CodeGeneratorConfig.PackageConfig getPackageConfig() {
		return packageConfig;
	}

	public void setPackageConfig(CodeGeneratorConfig.PackageConfig packageConfig) {
		this.packageConfig = packageConfig;
	}
}
