package com.taotao.cloud.health.configuration;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.health.filter.HibernateInterceptor;
import com.taotao.cloud.health.filter.SqlMybatisInterceptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatits 监控配置文件
 *
 * @author Robin.Wang
 * @date 2020-04-29
 */
@Configuration

public class HealthSqlMybatisConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(HealthSqlMybatisConfiguration.class, StarterNameConstant.HEALTH_STARTER);
	}

	@Bean
	@ConditionalOnClass(name = "org.apache.ibatis.plugin.Interceptor")
	public SqlMybatisInterceptor sqlMybatisInterceptor() {
		LogUtil.started(SqlMybatisInterceptor.class, StarterNameConstant.HEALTH_STARTER);
		return new SqlMybatisInterceptor();
	}

	@Bean
	@ConditionalOnClass(name = "org.hibernate.Interceptor")
	public HibernateInterceptor hibernateInterceptor() {
		LogUtil.started(HibernateInterceptor.class, StarterNameConstant.HEALTH_STARTER);
		return new HibernateInterceptor();
	}
}
