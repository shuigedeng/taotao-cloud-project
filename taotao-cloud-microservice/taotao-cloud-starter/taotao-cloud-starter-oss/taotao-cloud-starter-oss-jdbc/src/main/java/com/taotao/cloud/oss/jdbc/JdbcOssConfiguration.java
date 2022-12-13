package com.taotao.cloud.oss.jdbc;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.condition.ConditionalOnOssEnabled;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * jdbc操作系统配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:04
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@ConditionalOnBean(JdbcTemplate.class)
@EnableConfigurationProperties({JdbcOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "JDBC")
public class JdbcOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(JdbcOssConfiguration.class, StarterName.OSS_JDBC_STARTER);
	}

	public static final String DEFAULT_BEAN_NAME = "jdbcOssClient";

	@Autowired
	private JdbcOssProperties jdbcOssProperties;

	@Bean
	@ConditionalOnMissingBean
	public StandardOssClient jdbcOssClient() {
		Map<String, JdbcOssConfig> ossConfigMap = jdbcOssProperties.getOssConfig();
		if (ossConfigMap.isEmpty()) {
			registerJdbcOssClient(DEFAULT_BEAN_NAME, jdbcOssProperties);
		} else {
			ossConfigMap.forEach(this::registerJdbcOssClient);
		}
		return null;
	}

	public void registerJdbcOssClient(String jdbcOssClientBeanName, JdbcOssConfig jdbcOssConfig) {
		if (ObjectUtil.isEmpty(jdbcOssConfig.getDriver()) && ObjectUtil.isEmpty(
			jdbcOssConfig.getType()) &&
			ObjectUtil.isEmpty(jdbcOssConfig.getUrl()) && ObjectUtil.isEmpty(
			jdbcOssConfig.getUsername()) &&
			ObjectUtil.isEmpty(jdbcOssConfig.getPassword()) && ObjectUtil.isEmpty(
			jdbcOssConfig.getDataSourceName())) {
			SpringUtil.registerBean(jdbcOssClientBeanName,
				jdbcOssClient(SpringUtil.getBean(DataSource.class), jdbcOssConfig));
		} else if (ObjectUtil.isNotEmpty(jdbcOssConfig.getDataSourceName())) {
			SpringUtil.registerBean(jdbcOssClientBeanName, jdbcOssClient(
				jdbcTemplate((DataSource) SpringUtil.getBean(jdbcOssConfig.getDataSourceName())),
				jdbcOssProperties));
		} else {
			SpringUtil.registerBean(jdbcOssClientBeanName, jdbcOssClient(jdbcOssConfig));
		}
	}

	public StandardOssClient jdbcOssClient(DataSource dataSource, JdbcOssConfig jdbcOssConfig) {
		return new JdbcOssClient(jdbcTemplate(dataSource), jdbcOssConfig);
	}

	public StandardOssClient jdbcOssClient(JdbcTemplate jdbcTemplate, JdbcOssConfig jdbcOssConfig) {
		return new JdbcOssClient(jdbcTemplate, jdbcOssConfig);
	}

	public StandardOssClient jdbcOssClient(JdbcOssConfig jdbcOssConfig) {
		return new JdbcOssClient(jdbcTemplate(jdbcOssConfig), jdbcOssConfig);
	}

	public JdbcTemplate jdbcTemplate(JdbcOssConfig jdbcOssConfig) {
		return new JdbcTemplate(dataSource(jdbcOssConfig));
	}

	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	public DataSource dataSource(JdbcOssConfig jdbcOssConfig) {
		Class<? extends DataSource> type = jdbcOssConfig.getType();
		if (ObjectUtil.isEmpty(type)) {
			type = HikariDataSource.class;
		}
		return DataSourceBuilder.create()
			.type(type)
			.driverClassName(jdbcOssConfig.getDriver())
			.url(jdbcOssConfig.getUrl())
			.username(jdbcOssConfig.getUsername())
			.password(jdbcOssConfig.getPassword())
			.build();
	}
}
