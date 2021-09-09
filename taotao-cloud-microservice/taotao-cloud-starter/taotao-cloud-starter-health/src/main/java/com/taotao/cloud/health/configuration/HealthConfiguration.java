package com.taotao.cloud.health.configuration;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.configuration.CoreAutoConfiguration;
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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
@AutoConfigureAfter({CoreAutoConfiguration.class})
@ConditionalOnProperty(prefix = HealthProperties.PREFIX, name = "enabled", havingValue = "true")
public class HealthConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(HealthConfiguration.class, StarterNameConstant.HEALTH_STARTER);
	}

	@Bean(destroyMethod = "close")
	@ConditionalOnProperty(prefix = WarnProperties.PREFIX, name = "enabled", havingValue = "true")
	public WarnProvider getWarnProvider() {
		LogUtil.started(WarnProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new WarnProvider();
	}

	@Bean(destroyMethod = "close")
	@ConditionalOnProperty(prefix = CheckProperties.PREFIX, name = "enabled", havingValue = "true")
	public HealthCheckProvider getHealthCheckProvider(CollectTaskProperties properties) {
		LogUtil.started(HealthCheckProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new HealthCheckProvider(properties);
	}

	@Bean(initMethod = "start", destroyMethod = "close")
	@ConditionalOnProperty(prefix = ExportProperties.PREFIX, name = "enabled", havingValue = "true")
	public ExportProvider getExportProvider() {
		LogUtil.started(ExportProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new ExportProvider();
	}

	@Bean
	@ConditionalOnProperty(prefix = DumpProperties.PREFIX, name = "enabled", havingValue = "true")
	public DumpProvider dumpProvider() {
		LogUtil.started(DumpProvider.class, StarterNameConstant.HEALTH_STARTER);
		return new DumpProvider();
	}

	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnBean(HealthCheckProvider.class)
	@ConditionalOnProperty(prefix = ReportProperties.PREFIX, name = "enabled", havingValue = "true")
	public FilterRegistrationBean healthReportFilter(HealthCheckProvider healthProvider) {
		LogUtil.started(HealthReportFilter.class, StarterNameConstant.HEALTH_STARTER);

		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		filterRegistrationBean.setFilter(new HealthReportFilter(healthProvider));
		filterRegistrationBean.setName("taotao-cloud-report-filter");
		filterRegistrationBean.addUrlPatterns("/taotao/cloud/health/report/*");
		LogUtil.info(
			"health报表注册成功,访问:" + RequestUtil.getBaseUrl() + "/taotao/cloud/health/");
		return filterRegistrationBean;
	}

	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnBean(DumpProvider.class)
	@ConditionalOnProperty(prefix = DumpProperties.PREFIX, name = "enabled", havingValue = "true")
	public FilterRegistrationBean dumpFilter(DumpProvider dumpProvider) {
		LogUtil.started(DumpFilter.class, StarterNameConstant.HEALTH_STARTER);

		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		filterRegistrationBean.setFilter(new DumpFilter(dumpProvider));
		filterRegistrationBean.setName("taotao-cloud-dump-filter");
		filterRegistrationBean.addUrlPatterns("/taotao/cloud/health/dump/*");
		LogUtil.info(
			"health dump注册成功,访问:" + RequestUtil.getBaseUrl() + "/taotao/cloud/health/dump/");
		return filterRegistrationBean;
	}

	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnProperty(prefix = PingProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
	public FilterRegistrationBean pingFilter() {
		LogUtil.started(PingFilter.class, StarterNameConstant.HEALTH_STARTER);

		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		filterRegistrationBean.setFilter(new PingFilter());
		filterRegistrationBean.setName("taotao-cloud-ping-filter");
		filterRegistrationBean.addUrlPatterns("/taotao/cloud/health/ping/");
		LogUtil.info(
			"health ping注册成功,访问:" + RequestUtil.getBaseUrl() + "/taotao/cloud/health/ping/");
		return filterRegistrationBean;
	}

	@Configuration
	@ConditionalOnWebApplication
	@ConditionalOnProperty(prefix = DoubtApiProperties.PREFIX, name = "enabled", havingValue = "true")
	static class InterceptorProcessor implements InstantiationAwareBeanPostProcessor {

		private DoubtApiProperties properties;

		public InterceptorProcessor(DoubtApiProperties properties) {
			this.properties = properties;
		}

		@Override
		public boolean postProcessAfterInstantiation(Object bean, String beanName)
			throws BeansException {
			if (bean instanceof AbstractHandlerMapping) {
				AbstractHandlerMapping handlerMapping = (AbstractHandlerMapping) bean;
				MappedInterceptor interceptor = new MappedInterceptor(new String[]{"/**"},
					new String[]{"/actuator/**"}, new DoubtApiInterceptor(properties));
				handlerMapping.setInterceptors(interceptor);
			}

			return true;
		}
	}

	//@Bean
	//@ConditionalOnWebApplication
	//@ConditionalOnBean(WebMvcConfigurer.class)
	//public void doubtApiInterceptor(WebMvcConfigurer webMvcConfigurer,
	//	DoubtApiProperties properties) {
	//	LogUtil.started(InterceptorRegistry.class, StarterName.HEALTH_STARTER);
	//
	//	InterceptorRegistry registry = new InterceptorRegistry();
	//	registry.addInterceptor(new DoubtApiInterceptor(properties)).addPathPatterns("/**");
	//	webMvcConfigurer.addInterceptors(registry);
	//}

}
