package com.taotao.cloud.sys.biz.tools.database.dtos;

import com.taotao.cloud.sys.biz.tools.database.dtos.CodeGeneratorConfig.DataSourceConfig;
import com.taotao.cloud.sys.biz.tools.database.dtos.CodeGeneratorConfig.PackageConfig;

import java.util.List;

public class CodeGeneratorParam {
   private List<String> templates;
   private CodeGeneratorConfig.DataSourceConfig dataSourceConfig;
   private CodeGeneratorConfig.PackageConfig packageConfig;
   private String renameStrategyName;
   // 单一文件 , 这时只会使用 dataSourceConfig
   private boolean single;

	public List<String> getTemplates() {
		return templates;
	}

	public void setTemplates(List<String> templates) {
		this.templates = templates;
	}

	public DataSourceConfig getDataSourceConfig() {
		return dataSourceConfig;
	}

	public void setDataSourceConfig(
		DataSourceConfig dataSourceConfig) {
		this.dataSourceConfig = dataSourceConfig;
	}

	public PackageConfig getPackageConfig() {
		return packageConfig;
	}

	public void setPackageConfig(
		PackageConfig packageConfig) {
		this.packageConfig = packageConfig;
	}

	public String getRenameStrategyName() {
		return renameStrategyName;
	}

	public void setRenameStrategyName(String renameStrategyName) {
		this.renameStrategyName = renameStrategyName;
	}

	public boolean isSingle() {
		return single;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}
}
