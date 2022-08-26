package com.taotao.cloud.monitor.kuding.properties;

import com.taotao.cloud.monitor.kuding.properties.enums.ProjectEnviroment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;


@RefreshScope
@ConfigurationProperties(prefix = NoticeProperties.PREFIX)
public class NoticeProperties {

	public static final String PREFIX = "taotao.cloud.monitor.notice";

	/**
	 * 是否开启异常通知
	 */
	private boolean enabled = false;

	/**
	 * 异常工程名
	 */
	@Value("${taotao.cloud.monitor.notice.project-name:${spring.application.name:project}}")
	private String projectName;

	/**
	 * 工程的发布环境，主要分为5个：开发环境、测试环境、预发环境、正式环境与回滚环境
	 */
	private ProjectEnviroment projectEnviroment = ProjectEnviroment.DEVELOP;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public ProjectEnviroment getProjectEnviroment() {
		return projectEnviroment;
	}

	public void setProjectEnviroment(ProjectEnviroment projectEnviroment) {
		this.projectEnviroment = projectEnviroment;
	}

}
