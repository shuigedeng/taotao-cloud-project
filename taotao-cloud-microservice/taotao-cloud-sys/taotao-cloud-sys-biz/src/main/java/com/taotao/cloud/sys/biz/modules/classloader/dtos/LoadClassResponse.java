package com.taotao.cloud.sys.biz.modules.classloader.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sanri.tools.modules.classloader.ExtendClassloader;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class LoadClassResponse {
    /**
     * 加载的类列表
     */
    private List<String> classes = new ArrayList<>();
    /**
     * 加载的 jar 列表
     */
    private List<String> jars = new ArrayList<>();

    /**
     * 无效文件列表
     */
    private List<String> removeFiles = new ArrayList<>();

    @JsonIgnore
    private ExtendClassloader extendClassloader;

    private String settings;

    public LoadClassResponse() {
    }

    public LoadClassResponse(ExtendClassloader extendClassloader) {
        this.extendClassloader = extendClassloader;
    }

    public void addClass(String className){
        this.classes.add(className);
    }

    public void addJar(String jar){
        this.jars.add(jar);
    }

    public void addRemoveFile(String filename){
        removeFiles.add(filename);
    }
}
