package com.taotao.cloud.prometheus.model;

import com.taotao.cloud.prometheus.enums.ProjectEnviroment;
import java.time.LocalDateTime;


public class PromethuesNotice {

	/**
	 * 通知标题
	 */
	protected String title;

	/**
	 * 工程环境
	 */
	protected ProjectEnviroment projectEnviroment;

	/**
	 * 通知时间
	 */
	protected LocalDateTime createTime = LocalDateTime.now();

	/**
	 * @param title
	 * @param projectEnviroment
	 */
	public PromethuesNotice(String title, ProjectEnviroment projectEnviroment) {
		this.title = title;
		this.projectEnviroment = projectEnviroment;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the projectEnviroment
	 */
	public ProjectEnviroment getProjectEnviroment() {
		return projectEnviroment;
	}

	/**
	 * @param projectEnviroment the projectEnviroment to set
	 */
	public void setProjectEnviroment(ProjectEnviroment projectEnviroment) {
		this.projectEnviroment = projectEnviroment;
	}

	/**
	 * @return the createTime
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

}
