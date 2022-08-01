package com.taotao.cloud.sys.biz.modules.compiler.dtos;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目编译配置
 */
@Data
public class ModuleCompileConfig {
    /**
     * 模块路径
     */
    private File module;
    /**
     * 源码目录 相对路径
     */
    private String sourceRoot;
    /**
     * 类路径信息
     */
    private List<File> classpaths = new ArrayList<>();
    /**
     * 输出位置 相对路径
     */
    private String output;
    /**
     * 要编译的文件列表 相对路径
     */
    private List<String> compileFiles = new ArrayList<>();

    /**
     * 获取源码路径文件
     * @return
     */
    public File getSourcePath(){
        return new File(module,sourceRoot);
    }

    /**
     * 获取编译文件列表
     * @return
     */
    public List<File> getCompileFiles(){
        return compileFiles.stream().map(compileFile -> new File(module,compileFile)).collect(Collectors.toList());
    }

    /**
     * 获取输出目录
     * @return
     */
    public File getOutput(){
        return new File(module,output);
    }
}
