package com.taotao.cloud.sys.biz.tools.database.dtos;

import com.taotao.cloud.sys.biz.tools.database.dtos.CodeGeneratorConfig.DataSourceConfig;
import com.taotao.cloud.sys.biz.tools.database.dtos.CodeGeneratorConfig.PackageConfig;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapperBuildConfig {
    /**
     * mybatis 的 targetRunTime 配置, 可选值有 MyBatis3, MyBatis3Simple, MyBatis3DynamicSql
     */
    private String targetRunTime = "MyBatis3Simple";
    /**
     * mybatis modelType 配置 , 可选值有 conditional, flat, hierarchical
     */
    private String modelType = "FLAT";
    /**
     * mybatis javaClientType 配置
     */
    private String javaClientType = "XMLMAPPER";

    /**
     * 需要生成的内容
     */
    private FilesConfig filesConfig = new FilesConfig();

    /**
     * 数据源配置
     */
    @Valid
    private CodeGeneratorConfig.DataSourceConfig dataSourceConfig;

    /**
     * 包路径配置
     */
    @Valid
    private CodeGeneratorConfig.PackageConfig packageConfig;
    /**
     * 项目名
     */
    @NotNull
    private String projectName;
    /**
     * 插件列表配置
     */
    private List<PluginConfig> pluginConfigs = new ArrayList<>();

    public static class FilesConfig{
        /**
         * 是否构建 entity
         */
        private boolean entity = true;
        /**
         * 是否构建 xml
         */
        private boolean xml = true;
        /**
         * 是否构建 mapper
         */
        private boolean mapper = true;

	    public boolean isEntity() {
		    return entity;
	    }

	    public void setEntity(boolean entity) {
		    this.entity = entity;
	    }

	    public boolean isXml() {
		    return xml;
	    }

	    public void setXml(boolean xml) {
		    this.xml = xml;
	    }

	    public boolean isMapper() {
		    return mapper;
	    }

	    public void setMapper(boolean mapper) {
		    this.mapper = mapper;
	    }
    }


    public static class PluginConfig{
        /**
         * 类型, class 路径
         */
        private String type;
        /**
         * 类配置信息
         */
        private Map<String,String> properties = new HashMap<>();

	    public String getType() {
		    return type;
	    }

	    public void setType(String type) {
		    this.type = type;
	    }

	    public Map<String, String> getProperties() {
		    return properties;
	    }

	    public void setProperties(Map<String, String> properties) {
		    this.properties = properties;
	    }
    }

	public String getTargetRunTime() {
		return targetRunTime;
	}

	public void setTargetRunTime(String targetRunTime) {
		this.targetRunTime = targetRunTime;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getJavaClientType() {
		return javaClientType;
	}

	public void setJavaClientType(String javaClientType) {
		this.javaClientType = javaClientType;
	}

	public FilesConfig getFilesConfig() {
		return filesConfig;
	}

	public void setFilesConfig(
		FilesConfig filesConfig) {
		this.filesConfig = filesConfig;
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<PluginConfig> getPluginConfigs() {
		return pluginConfigs;
	}

	public void setPluginConfigs(
		List<PluginConfig> pluginConfigs) {
		this.pluginConfigs = pluginConfigs;
	}
}
