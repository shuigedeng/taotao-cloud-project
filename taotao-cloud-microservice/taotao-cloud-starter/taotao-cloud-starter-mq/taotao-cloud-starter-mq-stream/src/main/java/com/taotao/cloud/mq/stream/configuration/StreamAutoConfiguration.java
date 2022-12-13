package com.taotao.cloud.mq.stream.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.support.factory.YamlPropertySourceFactory;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.mq.stream.properties.RocketmqCustomProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * StreamAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-25 09:41:50
 */
@AutoConfiguration
@EnableConfigurationProperties({RocketmqCustomProperties.class})
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:stream.yml")
public class StreamAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(StreamAutoConfiguration.class, StarterName.MQ_STREAM_STARTER);
	}

}
