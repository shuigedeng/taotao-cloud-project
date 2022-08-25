package com.taotao.cloud.openapi.client.proxy;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.openapi.client.annotation.OpenApiRef;
import com.taotao.cloud.openapi.client.constant.ClientConstant;
import com.taotao.cloud.openapi.common.exception.OpenApiClientException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * OpenApiRef代理对象注册器，用于动态注册OpenApiRef代理对象，当存在配置openapi.config.openApiRefPath时生效
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:05:21
 */
@Component
@ConditionalOnProperty(ClientConstant.OPENAPI_REF_PATH)
public class OpenApiRefProxyRegistry implements BeanDefinitionRegistryPostProcessor,
	ResourceLoaderAware, EnvironmentAware {

	/**
	 * 环境对象，用来获取各种配置
	 */
	private Environment environment;

	/**
	 * 资源加载对象，用来获取各种资源
	 */
	private ResourcePatternResolver resolver;
	private MetadataReaderFactory metadataReaderFactory;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
		throws BeansException {
		// 扫描自定义注解，获取Class
		Set<Class<?>> interClazzSet = this.getInterfacesAnnotatedWith(OpenApiRef.class);

		//将 class包装为BeanDefinition ，注册到Spring的Ioc容器中
		registerOpenApiRefProxies(registry, interClazzSet);
	}


	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
		throws BeansException {

	}

	/**
	 * 获取指定注解的接口类
	 *
	 * @param aClass 注解类
	 * @return 接口类
	 */
	private Set<Class<?>> getInterfacesAnnotatedWith(Class<?> aClass) {
		String scanPath = environment.getProperty(ClientConstant.OPENAPI_REF_PATH);
		if (StrUtil.isBlank(scanPath)) {
			throw new OpenApiClientException("OpenApiRef接口所在路径为空");
		}
		Set<Class<?>> classes = new HashSet<>();
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				.concat(ClassUtils.convertClassNameToResourcePath(
						SystemPropertyUtils.resolvePlaceholders(scanPath))
					.concat("/**/*.class"));
			Resource[] resources = resolver.getResources(packageSearchPath);
			MetadataReader metadataReader;
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					metadataReader = metadataReaderFactory.getMetadataReader(resource);
					// 当类型是接口再添加到集合
					if (metadataReader.getClassMetadata().isInterface()) {
						Class interClass = Class.forName(
							metadataReader.getClassMetadata().getClassName());
						if (interClass.isAnnotationPresent(aClass)) {
							classes.add(interClass);
						}
					}
				}
			}
			return classes;
		} catch (Exception ex) {
			LogUtil.error(String.format("扫描%s下的OpenApiRef接口信息异常", scanPath), ex);
			return classes;
		}
	}

	/**
	 * 注册OpenApiRef代理对象到spring容器
	 *
	 * @param registry      bean注册器
	 * @param interClazzSet OpenApiRef接口类集合
	 */
	private void registerOpenApiRefProxies(BeanDefinitionRegistry registry,
		Set<Class<?>> interClazzSet) {
		for (Class<?> interClazz : interClazzSet) {
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(
				interClazz);
			GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionBuilder.getRawBeanDefinition();

			//设置构造方法的参数  对于Class<?>,既可以设置为Class,也可以传Class的完全类名
			definition.getConstructorArgumentValues().addGenericArgumentValue(interClazz);

			//Bean的类型，指定为某个代理接口的类型
			definition.setBeanClass(OpenApiRefProxyFactoryBean.class);

			//表示 根据代理接口的类型来自动装配
			definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

			//注册bean
			registry.registerBeanDefinition(interClazz.getName(), definition);
		}
	}
}
