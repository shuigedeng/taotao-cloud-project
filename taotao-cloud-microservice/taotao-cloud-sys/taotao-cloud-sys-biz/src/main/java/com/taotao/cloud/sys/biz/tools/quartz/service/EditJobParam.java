package com.taotao.cloud.sys.biz.tools.quartz.service;

import org.quartz.JobKey;

import javax.validation.constraints.NotNull;

public class EditJobParam {
//    private JobKey jobKey;
    /**
     * 任务名
     */
    @NotNull
    private String name;
    /**
     * 任务分组
     */
    @NotNull
    private String group;

    /**
     * 描述
     */
    private String description;
    /**
     * 类名
     */
    @NotNull
    private String className;
    /**
     * 类加载器名称
     */
    private String classloaderName;
    /**
     * 任务方法名称
     */
    @NotNull
    private String jobMethodName;
    /**
     * cron 表达式
     */
    @NotNull
    private String cron;

    public JobKey getJobKey(){
        return JobKey.jobKey(name,group);
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassloaderName() {
		return classloaderName;
	}

	public void setClassloaderName(String classloaderName) {
		this.classloaderName = classloaderName;
	}

	public String getJobMethodName() {
		return jobMethodName;
	}

	public void setJobMethodName(String jobMethodName) {
		this.jobMethodName = jobMethodName;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}
}
