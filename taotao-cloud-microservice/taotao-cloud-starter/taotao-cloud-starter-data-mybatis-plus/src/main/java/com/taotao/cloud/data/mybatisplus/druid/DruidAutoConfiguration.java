package com.taotao.cloud.data.mybatisplus.druid;

import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.taotao.cloud.common.support.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * Druid配置
 */
@AutoConfiguration
@ConditionalOnClass(DruidStatProperties.class)
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:tt-druid.yml")
public class DruidAutoConfiguration {
	/**
	 * 注册一个StatViewServlet
	 *
	 * @return
	 */
	@Bean
	public ServletRegistrationBean<StatViewServlet> druidStatViewServle() {

		//org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
		ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

		//添加初始化参数：initParams

		//白名单：
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1");

		//IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
		servletRegistrationBean.addInitParameter("deny", "192.168.0.114");

		//登录查看信息的账号密码.
		servletRegistrationBean.addInitParameter("loginUsername", "admin");
		servletRegistrationBean.addInitParameter("loginPassword", "123456");

		//是否能够重置数据.
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}


	@Bean
	public SqlLogFilter sqlLogFilter(){
		return new SqlLogFilter();
	}

	/**
	 * 注册一个：filterRegistrationBean
	 *
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<WebStatFilter> druidStatFilter() {
		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());

		//添加过滤规则.
		filterRegistrationBean.addUrlPatterns("/*");

		//添加不需要忽略的格式信息.
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

		return filterRegistrationBean;
	}
}
