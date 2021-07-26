package com.taotao.cloud.job.component;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.PropertyUtil;
import com.taotao.cloud.common.utils.RequestUtil;
import com.taotao.cloud.job.properties.XxlExecutorProperties;
import com.taotao.cloud.job.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * xxl-job自动装配
 *
 * @author lishangbu
 * @date 2020/9/14
 */
@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration
public class XxlJobAutoConfiguration {

	/**
	 * 服务名称 包含 XXL_JOB_ADMIN 则说明是 Admin
	 */
	private static final String TAO_TAO_CLOUD_XXL_JOB_ADMIN = "taotao-cloud-xxl-job-admin";
	private static final String XXL_JOB_ADMIN = "xxl-job-admin";

	/**
	 * 配置xxl-job 执行器，提供自动发现 xxl-job-admin 能力
	 *
	 * @param xxlJobProperties xxl 配置
	 * @param environment      环境变量
	 * @param discoveryClient  注册发现客户端
	 */
	@Bean
	@ConditionalOnProperty(prefix = "taotao.cloud.xxl.job", name = "enabled", havingValue = "true", matchIfMissing = true)
	public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties xxlJobProperties,
		Environment environment,
		DiscoveryClient discoveryClient) {
		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
		XxlExecutorProperties executor = xxlJobProperties.getExecutor();

		// 应用名默认为服务名
		String appName = executor.getAppname();
		if (!StringUtils.hasText(appName)) {
			appName = PropertyUtil.getProperty("spring.application.name");
		}
		xxlJobSpringExecutor.setAppname(appName);

		xxlJobSpringExecutor.setAddress(executor.getAddress());

		xxlJobSpringExecutor.setIp(executor.getIp());
		if (StrUtil.isEmpty(executor.getIp())) {
			executor.setIp(RequestUtil.getLocalAddr());
		}

		xxlJobSpringExecutor.setPort(executor.getPort());
		xxlJobSpringExecutor.setAccessToken(executor.getAccessToken());
		xxlJobSpringExecutor.setLogPath(executor.getLogPath());
		xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());

		// 如果配置为空则获取注册中心的服务列表 "http://pigx-xxl:9080/xxl-job-admin"
		if (!StringUtils.hasText(xxlJobProperties.getAdmin().getAddresses())) {
			String serverList = discoveryClient.getServices().stream()
				.filter(s -> s.contains(TAO_TAO_CLOUD_XXL_JOB_ADMIN))
				.flatMap(s -> discoveryClient.getInstances(s).stream()).map(instance -> String
					.format("http://%s:%s", instance.getHost(), instance.getPort()))
				.collect(Collectors.joining(","));
			xxlJobSpringExecutor.setAdminAddresses(serverList);
		} else {
			xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
		}

		LogUtil.info(
			"[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_JOB_STARTER + "]" + "job模块已启动");
		return xxlJobSpringExecutor;
	}

}
