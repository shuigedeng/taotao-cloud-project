package com.taotao.cloud.health.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.health.collect.HealthCheckProvider;
import com.taotao.cloud.health.dump.DumpProvider;
import com.taotao.cloud.health.export.ExportProvider;
import com.taotao.cloud.health.filter.DoubtApiInterceptor;
import com.taotao.cloud.health.filter.DumpFilter;
import com.taotao.cloud.health.filter.HealthReportFilter;
import com.taotao.cloud.health.filter.PingFilter;
import com.taotao.cloud.health.properties.CheckProperties;
import com.taotao.cloud.health.properties.CollectTaskProperties;
import com.taotao.cloud.health.properties.DoubtApiProperties;
import com.taotao.cloud.health.properties.DumpProperties;
import com.taotao.cloud.health.properties.ExportProperties;
import com.taotao.cloud.health.properties.HealthProperties;
import com.taotao.cloud.health.properties.PingProperties;
import com.taotao.cloud.health.properties.ReportProperties;
import com.taotao.cloud.health.properties.WarnProperties;
import com.taotao.cloud.health.warn.WarnProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnProperty(prefix = HealthProperties.PREFIX, name = "enabled", havingValue = "true")
public class HealthConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.info(HealthConfiguration.class, StarterName.HEALTH_STARTER,
			" HealthConfiguration 模块已启动");
	}

	@ConditionalOnProperty(prefix = WarnProperties.PREFIX, name = "enabled", havingValue = "true")
	@Bean(destroyMethod = "close")
	public WarnProvider getWarnProvider() {
		LogUtil.info(WarnProvider.class, StarterName.HEALTH_STARTER, " WarnProvider 报警服务注册成功");
		return new WarnProvider();
	}

	@ConditionalOnProperty(prefix = CheckProperties.PREFIX, name = "enabled", havingValue = "true")
	@Bean(destroyMethod = "close")
	public HealthCheckProvider getHealthCheckProvider(CollectTaskProperties properties) {
		LogUtil.info(HealthCheckProvider.class, StarterName.HEALTH_STARTER,
			" HealthCheckProvider 自动健康检查服务注册成功");
		return new HealthCheckProvider(properties);
	}

	@ConditionalOnProperty(prefix = ExportProperties.PREFIX, name = "enabled", havingValue = "true")
	@Bean(initMethod = "start", destroyMethod = "close")
	public ExportProvider getExportProvider() {
		LogUtil.info(ExportProvider.class, StarterName.HEALTH_STARTER,
			" ExportProvider 自动上传健康报表服务注册成功");
		return new ExportProvider();
	}

	@ConditionalOnProperty(prefix = DumpProperties.PREFIX, name = "enabled", havingValue = "true")
	@Bean
	public DumpProvider dumpProvider() {
		LogUtil.info(DumpProvider.class, StarterName.HEALTH_STARTER, " DumpProvider 自动下载数据服务注册成功");
		return new DumpProvider();
	}

	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnProperty(prefix = ReportProperties.PREFIX, name = "enabled", havingValue = "true")
	public FilterRegistrationBean healthReportFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		filterRegistrationBean.setFilter(new HealthReportFilter());
		filterRegistrationBean.setName("taotao-cloud-report-filter");
		filterRegistrationBean.addUrlPatterns("/taotao/cloud/health/*");
		LogUtil.info(HealthReportFilter.class, StarterName.HEALTH_STARTER,
			"health报表注册成功,访问:" + RequestUtil.getBaseUrl() + "/taotao/cloud/health/");
		return filterRegistrationBean;
	}

	@ConditionalOnProperty(prefix = DumpProperties.PREFIX, name = "enabled", havingValue = "true")
	@Bean
	@ConditionalOnWebApplication
	public FilterRegistrationBean dumpFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		filterRegistrationBean.setFilter(new DumpFilter());
		filterRegistrationBean.setName("taotao-cloud-dump-filter");
		filterRegistrationBean.addUrlPatterns("/taotao/cloud/health/dump/*");
		LogUtil.info(DumpFilter.class, StarterName.HEALTH_STARTER,
			"health dump注册成功,访问:" + RequestUtil.getBaseUrl() + "/taotao/cloud/health/dump/");
		return filterRegistrationBean;
	}

	@ConditionalOnProperty(prefix = PingProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
	@Bean
	@ConditionalOnWebApplication
	public FilterRegistrationBean pingFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		filterRegistrationBean.setFilter(new PingFilter());
		filterRegistrationBean.setName("taotao-cloud-ping-filter");
		filterRegistrationBean.addUrlPatterns("/taotao/cloud/health/ping/");
		LogUtil.info(PingFilter.class, StarterName.HEALTH_STARTER,
			"health ping注册成功,访问:" + RequestUtil.getBaseUrl() + "/taotao/cloud/health/ping/");
		return filterRegistrationBean;
	}

	@ConditionalOnProperty(prefix = DoubtApiProperties.PREFIX, name = "enabled", havingValue = "true")
	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnBean(WebMvcConfigurer.class)
	public void doubtApiInterceptor(WebMvcConfigurer webMvcConfigurer,
		DoubtApiProperties properties) {
		InterceptorRegistry registry = new InterceptorRegistry();
		registry.addInterceptor(new DoubtApiInterceptor(properties)).addPathPatterns("/**");
		webMvcConfigurer.addInterceptors(registry);
	}

}
