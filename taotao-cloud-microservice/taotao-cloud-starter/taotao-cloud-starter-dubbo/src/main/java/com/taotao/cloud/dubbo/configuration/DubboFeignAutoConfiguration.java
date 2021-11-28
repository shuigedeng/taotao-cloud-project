package com.taotao.cloud.dubbo.configuration;

import static java.util.Collections.emptySet;
import static org.apache.dubbo.spring.boot.util.DubboUtils.BASE_PACKAGES_PROPERTY_NAME;
import static org.apache.dubbo.spring.boot.util.DubboUtils.DUBBO_SCAN_PREFIX;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.dubbo.properties.DubboProperties;
import feign.Feign;
import java.util.Set;
import org.apache.dubbo.config.AbstractConfig;
import org.apache.dubbo.config.spring.beans.factory.annotation.DubboFeignBuilder;
import org.apache.dubbo.config.spring.beans.factory.annotation.DubboFeignProviderBeanPostProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

/**
 * Dubbo配置
 *
 * @author shuigedeng
 */
@Configuration
@EnableConfigurationProperties({DubboProperties.class})
@ConditionalOnProperty(prefix = DubboProperties.PREFIX, name = "enabled", matchIfMissing = true)
@ConditionalOnClass(AbstractConfig.class)
public class DubboFeignAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(DubboFeignAutoConfiguration.class, StarterNameConstant.DUBBO_STARTER);
	}

    @Bean
    @ConditionalOnClass(ConfigurationPropertySources.class)
    @ConditionalOnProperty(prefix = DUBBO_SCAN_PREFIX, name = BASE_PACKAGES_PROPERTY_NAME)
    public DubboFeignProviderBeanPostProcessor dubboFeignProviderBeanPostProcessor(Environment environment) {
	    LogUtil.started(DubboFeignProviderBeanPostProcessor.class, StarterNameConstant.DUBBO_STARTER);

        Set<String> packagesToScan = environment.getProperty(DUBBO_SCAN_PREFIX + BASE_PACKAGES_PROPERTY_NAME, Set.class, emptySet());
        return new DubboFeignProviderBeanPostProcessor(packagesToScan);
    }

    @Bean
    @Primary
    public Feign.Builder feignDubboBuilder() {
	    LogUtil.started(DubboFeignBuilder.class, StarterNameConstant.DUBBO_STARTER);

        return new DubboFeignBuilder();
    }
}
