package com.taotao.cloud.data.mybatisplus.datascope.dataPermission.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.aop.DataPermissionAnnotationAdvisor;
import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.db.DataPermissionDatabaseInterceptor;
import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.factory.DataPermissionRuleFactory;
import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.factory.DataPermissionRuleFactoryImpl;
import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.rule.DataPermissionRule;
import com.taotao.cloud.data.mybatisplus.interceptor.MpInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 数据权限的自动配置类
 */
@AutoConfiguration
public class DataPermissionAutoConfiguration {

	/**
	 * 数据权限规则工厂 配置需要生效的数据权限规则
	 * @param rules 容器中的数据权限类
	 */
	@Bean
	public DataPermissionRuleFactory dataPermissionRuleFactory(List<DataPermissionRule> rules) {
		return new DataPermissionRuleFactoryImpl(rules);
	}

	/**
	 * 配置拦截器 重写sql
	 */
	@Bean
	public MpInterceptor dataPermissionDatabaseInterceptor(List<DataPermissionRule> rules) {
		// 生效的数据权限规则
		DataPermissionRuleFactory ruleFactory = dataPermissionRuleFactory(rules);

		// 创建 DataPermissionDatabaseInterceptor 拦截器
		DataPermissionDatabaseInterceptor dataPermissionDatabaseInterceptor = new DataPermissionDatabaseInterceptor(ruleFactory);

		// 需要加在首个，主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
		return new MpInterceptor(dataPermissionDatabaseInterceptor, 5);
	}

	/**
	 * 针对数据权限注解的AOP处理
	 */
	@Bean
	public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
		return new DataPermissionAnnotationAdvisor();
	}

}
