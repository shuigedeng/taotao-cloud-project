package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.dtos;

import com.taotao.cloud.sys.biz.api.controller.tools.core.utils.OnlyPath;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;

@Data
public class ProjectLocation {
    @NotBlank
    private String group;
    @NotBlank
    private String repository;
    /**
     * 项目路径, 默认就是仓库地址
     */
    private String path = "";

    public ProjectLocation() {
    }

    public ProjectLocation(@NotBlank String group, @NotBlank String repository, String path) {
        this.group = group;
        this.repository = repository;
        this.path = path;
    }

    public String getProjectName(){
        if (StringUtils.isBlank(path)){
            return repository;
        }
        return new OnlyPath(path).getFileName();
    }
}
