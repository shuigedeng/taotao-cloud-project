package com.taotao.cloud.sys.biz.api.controller.tools.mybtis.dto;

import lombok.Data;

@Data
public class ProjectDto {
    private String project;
    private String classloaderName;

    public ProjectDto() {
    }

    public ProjectDto(String project, String classloaderName) {
        this.project = project;
        this.classloaderName = classloaderName;
    }
}
