package com.taotao.cloud.health.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yh.csx.bsf.health.filter.SqlMybatisInterceptor;

/**
 * mybatits 监控配置文件
 * @author 	Robin.Wang
 * @date	2020-04-29
 * */
@Configuration
@ConditionalOnClass(name = "org.apache.ibatis.plugin.Interceptor")
public class HealthSqlMybatisConfiguration {
	
	@Bean	
	public SqlMybatisInterceptor sqlMybatisInterceptor() {
		return new SqlMybatisInterceptor();
	}
}
