package com.taotao.cloud.dubbo.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.dubbo.properties.DubboProperties;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Dubbo配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:21:27
 */
@AutoConfiguration
@EnableDubboConfig
@EnableConfigurationProperties({DubboProperties.class})
@ConditionalOnProperty(prefix = DubboProperties.PREFIX, name = "enabled", matchIfMissing = true)
public class DubboAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(DubboAutoConfiguration.class, StarterName.DUBBO_STARTER);
	}

    //@Bean
    //@ConditionalOnClass(ConfigurationPropertySources.class)
    //@ConditionalOnProperty(prefix = DUBBO_SCAN_PREFIX, name = BASE_PACKAGES_PROPERTY_NAME)
    //public DubboFeignProviderBeanPostProcessor dubboFeignProviderBeanPostProcessor(Environment environment) {
    //    Set<String> packagesToScan = environment.getProperty(DUBBO_SCAN_PREFIX + BASE_PACKAGES_PROPERTY_NAME, Set.class, emptySet());
    //    return new DubboFeignProviderBeanPostProcessor(packagesToScan);
    //}
	//
    //@Bean
    //@Primary
    //public Feign.Builder feignDubboBuilder() {
    //    return new DubboFeignBuilder();
    //}
}
