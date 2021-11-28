package com.taotao.cloud.apollo.configuration;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.taotao.cloud.apollo.properties.ApolloProperties;
import com.taotao.cloud.common.utils.LogUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApolloProperties.class)
@ConditionalOnProperty(name = "bsf.apollo.enabled", havingValue = "true")
@EnableApolloConfig //{"bsf","application"}
public class ApolloConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() {
		LogUtil.info(ApolloConfiguration.class.getName(),
			ApolloProperties.Project, "已启动!!!" + " ");
	}

}
