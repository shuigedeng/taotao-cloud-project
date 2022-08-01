package com.taotao.cloud.sys.biz.modules.versioncontrol.project.dtos;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目元数据信息, 需要存储各模块的上次编译时间
 */
@Data
public class ProjectMeta {
    /**
     * 项目名
     */
    private String projectName;

    /**
     * 相对于仓库路径
     */
    private String path;

    public ProjectMeta(String projectName, String path) {
        this.projectName = projectName;
        this.path = path;
    }

    /**
     * 模块编译信息元数据 模块名 => 模块编译元数据信息
     */
    private Map<String,ModuleCompileMeta> moduleCompileMetas = new HashMap<>();

    /**
     * 添加一个模块元数据, 如果模块存在, 则覆盖
     * @param moduleCompileMeta
     */
    public void addModuleCompileMeta(ModuleCompileMeta moduleCompileMeta){
        moduleCompileMetas.put(moduleCompileMeta.getModuleName(),moduleCompileMeta);
    }

    /**
     * 模块的编译信息
     */
    @Data
    public static final class ModuleCompileMeta{
        /**
         * 模块名称
         */
        private String moduleName;
        /**
         * 上次编译成功时间
         */
        private long lastCompileTime;
        /**
         * pom 文件相对路径, 相对于项目
         */
        private String pomFileRelativePath;

        /**
         * 类路径信息, 这里不会放具体的 classpath 信息, 只是存一个文件地址, 不然所有 module 的类路径放一个文件太大了
         */
        private String classpath;

        public ModuleCompileMeta(String moduleName, String pomFileRelativePath) {
            this.moduleName = moduleName;
            this.pomFileRelativePath = pomFileRelativePath;
        }
    }
}
