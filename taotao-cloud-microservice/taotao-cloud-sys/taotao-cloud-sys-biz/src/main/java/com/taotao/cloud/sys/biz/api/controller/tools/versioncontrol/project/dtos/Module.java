package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.project.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块信息
 */
@Data
public class Module {
    private PomFile pomFile;
    private List<Module> children = new ArrayList<>();

    public String getModuleName(){
        return pomFile.getModuleName();
    }

    public Module() {
    }

    public Module(PomFile pomFile) {
        this.pomFile = pomFile;
    }

    public Long getLastCompileTime(){
        if (pomFile == null){
            return null;
        }
        if (pomFile.getLastCompileTime() != null){
            return pomFile.getLastCompileTime().getTime();
        }
        return null;
    }

    public String getRelativePath(){
        return pomFile.getRelativePath();
    }
}
