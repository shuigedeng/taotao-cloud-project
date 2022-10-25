package com.taotao.cloud.job.xxl.executor.config;

import com.taotao.cloud.job.xxl.configuration.XxlJobAutoConfiguration;
import com.taotao.cloud.job.xxl.properties.XxlJobProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * xxl工作+配置
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-25 09:44:05
 */
@AutoConfiguration(after = XxlJobAutoConfiguration.class)
@ComponentScan(basePackages = "com.taotao.cloud.job.xxl.executor")
@ConditionalOnProperty(prefix = XxlJobProperties.PREFIX, name = "enabled", havingValue = "true")
public class XxlJobAutoRegisterConfiguration {

}
