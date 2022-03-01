package com.taotao.cloud.sys.biz.tools.mybatis.dtos;


public class ProjectDto {
    private String project;
    private String classloaderName;

    public ProjectDto() {
    }

    public ProjectDto(String project, String classloaderName) {
        this.project = project;
        this.classloaderName = classloaderName;
    }

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getClassloaderName() {
		return classloaderName;
	}

	public void setClassloaderName(String classloaderName) {
		this.classloaderName = classloaderName;
	}
}
