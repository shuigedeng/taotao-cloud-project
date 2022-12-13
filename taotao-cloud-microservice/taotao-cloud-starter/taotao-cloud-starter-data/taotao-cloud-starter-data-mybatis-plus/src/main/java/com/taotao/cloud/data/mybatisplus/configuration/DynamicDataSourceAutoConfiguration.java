package com.taotao.cloud.data.mybatisplus.configuration;

import com.baomidou.dynamic.datasource.processor.DsHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.data.mybatisplus.dynamic.config.DsLastParamProcessor;
import com.taotao.cloud.data.mybatisplus.dynamic.config.JdbcDynamicDataSourceProvider;
import com.taotao.cloud.data.mybatisplus.dynamic.properties.MybatisPlusDynamicDataSourceProperties;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 动态数据源切换配置
 */
@AutoConfiguration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(MybatisPlusDynamicDataSourceProperties.class)
@ConditionalOnProperty(prefix = MybatisPlusDynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true")
public class DynamicDataSourceAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(DynamicDataSourceAutoConfiguration.class,
			StarterName.DATA_MYBATIS_PLUS_STARTER);
	}

	/**
	 * 从数据库获取数据源信息
	 */
	@Bean
	public DynamicDataSourceProvider dynamicDataSourceProvider(StringEncryptor stringEncryptor,
		DataSourceProperties properties,
		MybatisPlusDynamicDataSourceProperties dynamicDataSourceProperties) {
		return new JdbcDynamicDataSourceProvider(stringEncryptor, properties,
			dynamicDataSourceProperties);
	}

	/**
	 * 动态解析数据源
	 */
	@Bean
	public DsProcessor dsProcessor() {
		DsLastParamProcessor lastParamProcessor = new DsLastParamProcessor();
		DsHeaderProcessor headerProcessor = new DsHeaderProcessor();
		DsSessionProcessor sessionProcessor = new DsSessionProcessor();
		DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();

		lastParamProcessor.setNextProcessor(headerProcessor);
		headerProcessor.setNextProcessor(sessionProcessor);
		sessionProcessor.setNextProcessor(spelExpressionProcessor);
		return lastParamProcessor;
	}

}
