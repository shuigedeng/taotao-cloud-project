package com.taotao.cloud.monitor.kuding.pojos.notice;

import com.taotao.cloud.monitor.kuding.pojos.servicemonitor.MicroServiceReport;
import com.taotao.cloud.monitor.kuding.properties.enums.ProjectEnviroment;

import java.time.format.DateTimeFormatter;


public class ServiceCheckNotice extends Notice {

	private MicroServiceReport servicesReport;

	private int problemServiceCount = 0;

	public ServiceCheckNotice(MicroServiceReport microServiceNotice, ProjectEnviroment enviroment, String title) {
		super(title, enviroment);
		this.servicesReport = microServiceNotice;
	}

	public MicroServiceReport getServicesReport() {
		return servicesReport;
	}

	public void setServicesReport(MicroServiceReport servicesReport) {
		this.servicesReport = servicesReport;
	}

	public int getProblemServiceCount() {
		return problemServiceCount;
	}

	public void setProblemServiceCount(int problemServiceCount) {
		this.problemServiceCount = problemServiceCount;
	}

	public void calculateProblemCount() {
		this.problemServiceCount = servicesReport.getHealthProblems().size()
				+ servicesReport.getInstanceLackProblems().size() + servicesReport.getLackServices().size();
	}

	public String generateText() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("服务监控通知：\n");
		if (servicesReport.getLackServices().size() > 0) {
			stringBuilder.append("缺少微服务：\n");
			servicesReport.getLackServices().forEach(x -> stringBuilder.append(x).append("\n"));
			stringBuilder.append("\n");
		}
		if (servicesReport.getInstanceLackProblems().size() > 0) {
			stringBuilder.append("缺少微服务的实例：\n");
			servicesReport.getInstanceLackProblems().forEach((x, y) -> {
				stringBuilder.append(x).append(":").append(y.getLackCount()).append("\n");
				stringBuilder.append("   已有实例：").append("\n");
				y.getInstanceIds().forEach(z -> stringBuilder.append(z).append("\n"));
			});
		}
		if (servicesReport.getHealthProblems().size() > 0) {
			stringBuilder.append("实例健康检查不通过：\n");
			servicesReport.getHealthProblems().forEach((x, y) -> {
				stringBuilder.append(x).append(":\n");
				y.getHealthyInstances().forEach(z -> {
					stringBuilder.append(z).append("\n");
				});
			});
		}
		stringBuilder.append(createTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		return stringBuilder.toString();
	}
}
