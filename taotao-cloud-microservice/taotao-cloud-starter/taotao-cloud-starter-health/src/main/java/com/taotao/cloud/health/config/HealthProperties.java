package com.taotao.cloud.health.config;

import com.taotao.cloud.common.utils.ContextUtil;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author: chejiangyi
 * @version: 2019-07-24 21:03
 **/
public class HealthProperties {

	public static String Project = "Health";
	public static String SpringApplictionName = "spring.application.name";
	public static String BsfHealthEnabled = "bsf.health.enabled";
	public static String BsfEnv = "bsf.env";

	public static HealthProperties Default() {
		return ContextUtil.getApplicationContext().getBean(HealthProperties.class);
	}

	/**
	 * 是否开启健康检查
	 *
	 * @return
	 */
	@Value("${bsf.health.enabled:false}")
	private boolean bsfHealthEnabled;

	/**
	 * 健康检查时间间隔 秒
	 *
	 * @return
	 */
	@Value("${bsf.health.timespan:10}")
	private int bsfHealthTimeSpan;

	//public static int getHealthCheckTimeSpan(){return PropertyUtils.getProperty("bsf.health.timespan",10);}

	public HealthProperties() {
	}

	public HealthProperties(boolean bsfHealthEnabled, int bsfHealthTimeSpan) {
		this.bsfHealthEnabled = bsfHealthEnabled;
		this.bsfHealthTimeSpan = bsfHealthTimeSpan;
	}

	public static String getProject() {
		return Project;
	}

	public static void setProject(String project) {
		Project = project;
	}

	public static String getSpringApplictionName() {
		return SpringApplictionName;
	}

	public static void setSpringApplictionName(String springApplictionName) {
		SpringApplictionName = springApplictionName;
	}

	public static String getBsfHealthEnabled() {
		return BsfHealthEnabled;
	}

	public static void setBsfHealthEnabled(String bsfHealthEnabled) {
		BsfHealthEnabled = bsfHealthEnabled;
	}

	public static String getBsfEnv() {
		return BsfEnv;
	}

	public static void setBsfEnv(String bsfEnv) {
		BsfEnv = bsfEnv;
	}

	public boolean isBsfHealthEnabled() {
		return bsfHealthEnabled;
	}

	public void setBsfHealthEnabled(boolean bsfHealthEnabled) {
		this.bsfHealthEnabled = bsfHealthEnabled;
	}

	public int getBsfHealthTimeSpan() {
		return bsfHealthTimeSpan;
	}

	public void setBsfHealthTimeSpan(int bsfHealthTimeSpan) {
		this.bsfHealthTimeSpan = bsfHealthTimeSpan;
	}
}
