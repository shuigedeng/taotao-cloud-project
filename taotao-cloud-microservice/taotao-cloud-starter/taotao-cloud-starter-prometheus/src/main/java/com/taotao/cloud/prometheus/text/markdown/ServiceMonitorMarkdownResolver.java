package com.taotao.cloud.prometheus.text.markdown;

import com.taotao.cloud.prometheus.pojos.servicemonitor.ServiceCheckNotice;
import com.taotao.cloud.prometheus.pojos.servicemonitor.ServiceHealthProblem;
import com.taotao.cloud.prometheus.pojos.servicemonitor.ServiceInstanceLackProblem;
import com.taotao.cloud.prometheus.text.ServiceMonitorResolver;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;


public class ServiceMonitorMarkdownResolver implements ServiceMonitorResolver {

	@Override
	public String resolve(ServiceCheckNotice notice) {
		SimpleMarkdownBuilder builder = SimpleMarkdownBuilder.create()
				.title(String.format("%s(%s)", "服务监控通知", notice.getProjectEnviroment().getName()), 1)
				.text("有问题的服务数量：", false).text(Integer.toString(notice.getProblemServiceCount()), true);
		Set<String> lackServices = notice.getServicesReport().getLackServices();
		if (lackServices.size() > 0)
			builder.title("缺少服务：", 2).orderPoint(lackServices.toArray());
		Map<String, ServiceInstanceLackProblem> instanceLackProblems = notice.getServicesReport()
				.getInstanceLackProblems();
		if (instanceLackProblems.size() > 0) {
			builder.title("有服务缺少实例：", 2);
			instanceLackProblems.forEach((x, y) -> {
				builder.title(x, 3);
				builder.text("缺失服务数量：", false).text(Integer.toString(y.getLackCount()), true);
				builder.title("已存在服务：", 4).orderPoint(y.getInstanceIds().toArray());
			});
		}
		Map<String, ServiceHealthProblem> healthProbleam = notice.getServicesReport().getHealthProblems();
		if (healthProbleam.size() > 0) {
			builder.title("服务健康检查有问题：", 2);
			healthProbleam.forEach((x, y) -> {
				builder.text(SimpleMarkdownBuilder.bold(x), true);
				builder.point(y.getUnhealthyInstances().toArray());
			});
		}
		builder.text("通知时间：", false).text(notice.getCreateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), true);
		return builder.build();
	}

}
