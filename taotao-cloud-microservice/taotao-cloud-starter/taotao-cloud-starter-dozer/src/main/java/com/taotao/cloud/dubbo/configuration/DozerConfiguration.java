package com.taotao.cloud.dubbo.configuration;

import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.spring.DozerBeanMapperFactoryBean;
import com.taotao.cloud.dubbo.props.DozerProperties;
import com.taotao.cloud.dubbo.util.DozerUtil;
import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Dozer配置
 *
 * @link http://dozer.sourceforge.net/documentation/usage.html http://www.jianshu.com/p/bf8f0e8aee23
 */
@ConditionalOnClass({DozerBeanMapperFactoryBean.class, Mapper.class})
@ConditionalOnMissingBean(Mapper.class)
public class DozerConfiguration {

	private final DozerProperties properties;

	/**
	 * Constructor for creating auto configuration.
	 *
	 * @param properties properties
	 */
	public DozerConfiguration(DozerProperties properties) {
		this.properties = properties;
	}

	@Bean
	public DozerUtil getDozerUtil(Mapper mapper) {
		return new DozerUtil(mapper);
	}

	/**
	 * Creates default Dozer mapper
	 *
	 * @return Dozer mapper
	 * @throws IOException if there is an exception during initialization.
	 */
	@Bean
	public DozerBeanMapperFactoryBean dozerMapper() throws IOException {
		DozerBeanMapperFactoryBean factoryBean = new DozerBeanMapperFactoryBean();
		// 官方这样子写，没法用 匹配符！
		//factoryBean.setMappingFiles(properties.getMappingFiles());
		factoryBean.setMappingFiles(properties.resolveMapperLocations());
		return factoryBean;
	}
}
