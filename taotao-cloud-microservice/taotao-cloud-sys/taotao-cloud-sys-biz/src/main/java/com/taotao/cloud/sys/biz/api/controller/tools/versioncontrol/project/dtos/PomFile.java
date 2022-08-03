package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.project.dtos;

import java.io.File;
import java.util.Date;

import com.taotao.cloud.sys.biz.api.controller.tools.core.utils.OnlyPath;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PomFile implements Comparable<PomFile>{
    private File project;
    private OnlyPath relativePath;
    private String moduleName;
    /**
     * 模块上次编译时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastCompileTime;

    public PomFile() {
    }

    public PomFile(File project, File pomFile){
        this.project = project;
        this.moduleName = pomFile.getParentFile().getName();
//        final Path relativize = project.toPath().relativize(pomFile.toPath());
        final OnlyPath relativize = new OnlyPath(project).relativize(new OnlyPath(pomFile));
        this.relativePath = OnlyPath.ROOT.resolve(relativize);
    }

    public PomFile(File project, OnlyPath relativePath) {
        this.project = project;
        this.relativePath = OnlyPath.ROOT.resolve(relativePath);
    }
    public PomFile(File project, String relativePath) {
        this.project = project;
        this.relativePath = OnlyPath.ROOT.resolve(new OnlyPath(relativePath));
    }

    @JsonIgnore
    public File getPomFile(){
        return new File(project,relativePath.toString());
    }

    public String getRelativePath(){
        return relativePath.toString();
    }

    public String getModuleName() {
        return moduleName;
    }

    public Date getLastCompileTime() {
        return lastCompileTime;
    }

    public void setLastCompileTime(Date lastCompileTime) {
        this.lastCompileTime = lastCompileTime;
    }

    @Override
    public int compareTo(PomFile o) {
        return o.relativePath.compareTo(this.relativePath);
    }
}
