package com.taotao.cloud.websocket.netty.standard;

import com.taotao.cloud.websocket.netty.annotation.ServerEndpoint;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class EndpointClassPathScanner extends ClassPathBeanDefinitionScanner {

	public EndpointClassPathScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
		super(registry, useDefaultFilters);
	}

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		addIncludeFilter(new AnnotationTypeFilter(ServerEndpoint.class));
		return super.doScan(basePackages);
	}
}
