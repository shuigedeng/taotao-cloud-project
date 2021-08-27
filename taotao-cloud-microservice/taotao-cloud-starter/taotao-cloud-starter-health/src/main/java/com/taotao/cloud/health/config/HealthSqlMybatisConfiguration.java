package com.taotao.cloud.health.config;

import com.taotao.cloud.health.filter.SqlMybatisInterceptor;
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
public class HealthSqlMybatisConfiguration {

	@Bean
	public SqlMybatisInterceptor sqlMybatisInterceptor() {
		return new SqlMybatisInterceptor();
	}
}
