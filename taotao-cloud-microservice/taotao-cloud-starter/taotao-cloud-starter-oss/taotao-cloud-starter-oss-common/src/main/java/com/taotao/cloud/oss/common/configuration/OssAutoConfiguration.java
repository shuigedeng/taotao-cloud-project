package com.taotao.cloud.oss.common.configuration;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:33:58
 */
@EnableSpringUtil
@AutoConfiguration
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

}
