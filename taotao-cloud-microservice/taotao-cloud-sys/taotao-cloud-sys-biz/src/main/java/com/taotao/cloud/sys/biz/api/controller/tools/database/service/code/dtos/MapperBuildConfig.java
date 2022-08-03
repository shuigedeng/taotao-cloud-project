package com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.dtos;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
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
    private ProjectGenerateConfig.DataSourceConfig dataSourceConfig;

    /**
     * 包路径配置
     */
    @Valid
    private ProjectGenerateConfig.PackageConfig packageConfig;
    /**
     * 项目名
     */
    @NotNull
    private String projectName;
    /**
     * 插件列表配置
     */
    private List<PluginConfig> pluginConfigs = new ArrayList<>();

    @Data
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
    }


    @Data
    public static class PluginConfig{
        /**
         * 类型, class 路径
         */
        private String type;
        /**
         * 类配置信息
         */
        private Map<String,String> properties = new HashMap<>();
    }
}
