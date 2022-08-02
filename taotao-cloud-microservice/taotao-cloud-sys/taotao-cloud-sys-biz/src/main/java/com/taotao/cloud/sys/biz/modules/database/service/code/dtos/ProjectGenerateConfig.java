package com.taotao.cloud.sys.biz.modules.database.service.code.dtos;

import com.taotao.cloud.sys.biz.modules.database.service.meta.dtos.Namespace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目代码生成配置
 */
@Data
public class ProjectGenerateConfig {
    /**
     * 生成路径
     */
    private String filePath;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 作者
     */
    private String author;

    /**
     * maven 配置
     */
    @Valid
    private MavenConfig mavenConfig;
    /**
     * 数据源配置
     */
    @Valid
    private DataSourceConfig dataSourceConfig;
    /**
     * 包路径配置
     */
    @Valid
    private PackageConfig packageConfig;

    @Data
    public static class MavenConfig{
        /**
         * maven 坐标
         */
        @Valid
        private Gav gav;

        /**
         * springBootVersion
         */
        private String springBootVersion = "2.0.5.RELEASE";

        /**
         * 依赖项
         */
        private List<Dependency> dependencies = new ArrayList<>();

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class Gav {
        /**
         * groupId
         */
        @NotNull
        private String groupId;
        /**
         * artifactId
         */
        @NotNull
        private String artifactId;
        /**
         * version
         */
        @NotNull
        private String version;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Dependency{
        /**
         * 组件分组
         */
        private String group;
        /**
         * 组件名称
         */
        private String name;
        /**
         * 是否启用
         */
        private boolean enable = true;

        /**
         * maven 坐标
         */
        private Gav gav;

        public Dependency(String group,String name,String gavInfo) {
            this.group = group;
            this.name = name;
            if (StringUtils.isNotBlank(gavInfo)){
                final String[] split = StringUtils.split(gavInfo, ":");
                this.gav = new Gav(split[0],split[1],split[2]);
            }
        }
    }

    @Data
    public static class DataSourceConfig {
        /**
         * 连接名
         */
        @NotNull
        private String connName;

        /**
         * 名称空间
         */
        private Namespace namespace;

        /**
         * 需要处理的数据表名
         */
        private List<String> tableNames = new ArrayList<>();

        public DataSourceConfig() {
        }

        public DataSourceConfig(@NotNull String connName, Namespace namespace, List<String> tableNames) {
            this.connName = connName;
            this.namespace = namespace;
            this.tableNames = tableNames;
        }
    }

    @Data
    public static class PackageConfig{
        /**
         * 基础包
         */
        private String parent;
        /**
         * mapper 包路径
         */
        private String mapper;
        /**
         * service 包路径
         */
        private String service;
        /**
         * controller 包路径
         */
        private String controller;

        /**
         * entity 包路径
         */
        private String entity;
        /**
         * vo 包路径
         */
        private String vo;
        /**
         * dto 包路径
         */
        private String dto;
        /**
         * param 包路径
         */
        private String param;

        /**
         * 根据包名, 找到目标目录
         * @param sourceDir
         * @param packageName
         * @return
         */
        public static File targetDir(File sourceDir,String packageName){
            final String replaceAll = RegExUtils.replaceAll(packageName, "\\.", "/");
            final File file = new File(sourceDir, replaceAll);
            if (!file.exists()){
                file.mkdirs();
            }
            return file;
        }

    }

}
