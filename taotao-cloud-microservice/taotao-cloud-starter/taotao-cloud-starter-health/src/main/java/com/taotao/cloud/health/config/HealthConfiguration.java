package com.taotao.cloud.health.config;

import com.yh.csx.bsf.core.util.LogUtils;
import com.yh.csx.bsf.core.util.WebUtils;
import com.yh.csx.bsf.health.dump.DumpProvider;
import com.yh.csx.bsf.health.export.ExportProvider;
import com.yh.csx.bsf.health.filter.*;
import com.yh.csx.bsf.health.collect.HealthCheckProvider;
import com.yh.csx.bsf.health.warn.WarnProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: chejiangyi
 * @version: 2019-07-24 13:45
 **/
@Configuration
@ConditionalOnProperty(name = "bsf.health.enabled", havingValue = "true")
@EnableConfigurationProperties({ HealthProperties.class, WarnProperties.class, ExportProperties.class })
public class HealthConfiguration {
	@ConditionalOnProperty(name = "bsf.health.warn.enabled", havingValue = "true")
	@Bean(destroyMethod = "close")
	public WarnProvider getWarnProvider() {
		LogUtils.info(HealthConfiguration.class, HealthProperties.Project, "报警服务注册成功");
		return new WarnProvider();
	}

	@ConditionalOnProperty(name = "bsf.health.check.enabled", havingValue = "true")
	@Bean(destroyMethod = "close")
	public HealthCheckProvider getHealthCheckProvider() {
		LogUtils.info(HealthConfiguration.class, HealthProperties.Project, "自动健康检查服务注册成功");
		return new HealthCheckProvider();
	}

	@Bean
	@ConditionalOnWebApplication
	@ConditionalOnProperty(name = "bsf.health.report.enabled", havingValue = "true", matchIfMissing = true)
	public FilterRegistrationBean healthReportFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		// 优先注入
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		filterRegistrationBean.setFilter(new HealthReportFilter());
		filterRegistrationBean.setName("healthReportFilter");
		filterRegistrationBean.addUrlPatterns("/bsf/health/*");
		LogUtils.info(HealthConfiguration.class, HealthProperties.Project,
				"health报表注册成功,访问:" + WebUtils.getBaseUrl() + "/bsf/health/");
		return filterRegistrationBean;
	}

	@ConditionalOnProperty(name = "bsf.health.export.enabled", havingValue = "true")
	@Bean(initMethod = "start", destroyMethod = "close")
	public ExportProvider getExportProvider() {
		LogUtils.info(HealthConfiguration.class, HealthProperties.Project, "自动上传健康报表服务注册成功");
		return new ExportProvider();
	}

	@ConditionalOnProperty(name = "bsf.health.dump.enabled", havingValue = "true")
	@Bean
	public DumpProvider dumpProvider() {
		return new DumpProvider();
	}

	@ConditionalOnProperty(name = "bsf.health.dump.enabled", havingValue = "true")
	@Bean
	@ConditionalOnWebApplication
	public FilterRegistrationBean dumpFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		// 优先注入
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		filterRegistrationBean.setFilter(new DumpFilter());
		filterRegistrationBean.setName("bsfDumpFilter");
		filterRegistrationBean.addUrlPatterns("/bsf/health/dump/*");
		LogUtils.info(HealthConfiguration.class, HealthProperties.Project,
				"health dump注册成功,访问:" + WebUtils.getBaseUrl() + "/bsf/health/dump/");
		return filterRegistrationBean;
	}

	@ConditionalOnProperty(name = "bsf.health.ping.enabled", havingValue = "true", matchIfMissing = true)
	@Bean
	@ConditionalOnWebApplication
	public FilterRegistrationBean pingFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		// 优先注入
		filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		filterRegistrationBean.setFilter(new PingFilter());
		filterRegistrationBean.setName("bsfpingFilter");
		filterRegistrationBean.addUrlPatterns("/bsf/health/ping/");
		LogUtils.info(HealthConfiguration.class, HealthProperties.Project,
				"health ping注册成功,访问:" + WebUtils.getBaseUrl() + "/bsf/health/ping/");
		return filterRegistrationBean;
	}

	@ConditionalOnProperty(name = "bsf.health.doubtapi.enabled", havingValue =  "true")
	@Bean
	@ConditionalOnWebApplication
	public WebMvcConfigurer webMvcConfigurer() { 
		  return new  WebMvcConfigurer() {	  
			  @Override 
			  public void addInterceptors(InterceptorRegistry registry) {
				  registry.addInterceptor(new DoubtApiInterceptor()).addPathPatterns("/**");	  
				
			  }
		  
		  }; 
	  }
}
