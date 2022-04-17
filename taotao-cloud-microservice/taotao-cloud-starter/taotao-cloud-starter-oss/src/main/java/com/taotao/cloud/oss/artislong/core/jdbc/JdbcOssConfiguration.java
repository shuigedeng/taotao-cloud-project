package com.taotao.cloud.oss.artislong.core.jdbc;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.jdbc.model.JdbcOssConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@ConditionalOnClass(JdbcTemplate.class)
@EnableConfigurationProperties({JdbcOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.JDBC + CharPool.DOT + OssConstant.ENABLE,
        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class JdbcOssConfiguration {

    public static final String DEFAULT_BEAN_NAME = "jdbcOssClient";

    @Autowired
    private JdbcOssProperties jdbcOssProperties;

    @Bean
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
        if (ObjectUtil.isEmpty(jdbcOssConfig.getDriver()) && ObjectUtil.isEmpty(jdbcOssConfig.getType()) &&
                ObjectUtil.isEmpty(jdbcOssConfig.getUrl()) && ObjectUtil.isEmpty(jdbcOssConfig.getUsername()) &&
                ObjectUtil.isEmpty(jdbcOssConfig.getPassword()) && ObjectUtil.isEmpty(jdbcOssConfig.getDataSourceName())) {
            SpringUtil.registerBean(jdbcOssClientBeanName, jdbcOssClient(SpringUtil.getBean(DataSource.class), jdbcOssConfig));
        } else if (ObjectUtil.isNotEmpty(jdbcOssConfig.getDataSourceName())) {
            SpringUtil.registerBean(jdbcOssClientBeanName, jdbcOssClient(jdbcTemplate((DataSource) SpringUtil.getBean(jdbcOssConfig.getDataSourceName())), jdbcOssProperties));
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
