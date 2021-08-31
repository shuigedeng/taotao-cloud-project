package com.taotao.cloud.health.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
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
@ConditionalOnClass(name = "org.apache.ibatis.plugin.Interceptor")
public class HealthSqlMybatisConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(HealthSqlMybatisConfiguration.class, StarterName.HEALTH_STARTER);
	}

	@Bean
	public SqlMybatisInterceptor sqlMybatisInterceptor() {
		LogUtil.started(SqlMybatisInterceptor.class, StarterName.HEALTH_STARTER);

		return new SqlMybatisInterceptor();
	}
}
