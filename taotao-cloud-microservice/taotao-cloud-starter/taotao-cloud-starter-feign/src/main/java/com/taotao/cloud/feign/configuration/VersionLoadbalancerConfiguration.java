package com.taotao.cloud.feign.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.common.utils.StringUtils;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.feign.configuration.VersionLoadbalancerConfiguration.VersionLoadBalancerClients;
import com.taotao.cloud.feign.configuration.VersionLoadbalancerConfiguration.VersionLoadbalancerRegisterBeanPostProcessor;
import com.taotao.cloud.feign.loadbalancer.VersionLoadBalancer;
import com.taotao.cloud.feign.loadbalancer.chooser.IRuleChooser;
import com.taotao.cloud.feign.loadbalancer.chooser.RoundRuleChooser;
import com.taotao.cloud.feign.properties.LoadbalancerProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

/**
 * 版本隔离配置
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-08 11:10:59
 */
@AutoConfiguration
@LoadBalancerClients(defaultConfiguration = VersionLoadBalancerClients.class)
@ConditionalOnProperty(prefix = LoadbalancerProperties.PREFIX
	+ ".isolation", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({VersionLoadbalancerRegisterBeanPostProcessor.class})
public class VersionLoadbalancerConfiguration {

	/**
	 * 将版本注册到注册中心的组件
	 */
	public static class VersionLoadbalancerRegisterBeanPostProcessor implements BeanPostProcessor {

		@Autowired
		private LoadbalancerProperties properties;

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
			if (bean instanceof NacosDiscoveryProperties && StringUtils.isNotBlank(
				properties.getVersion())) {
				NacosDiscoveryProperties nacosDiscoveryProperties = (NacosDiscoveryProperties) bean;
				nacosDiscoveryProperties.getMetadata()
					.putIfAbsent(CommonConstant.METADATA_VERSION, properties.getVersion());
			}
			return bean;
		}
	}

	/**
	 * 版本控制的路由选择类配置
	 *
	 * @author shuigedeng
	 * @version 2022.06
	 * @since 2022-06-08 11:10:55
	 */
	public static class VersionLoadBalancerClients {

		@Autowired
		private LoadbalancerProperties properties;

		@Bean
		@ConditionalOnMissingBean(IRuleChooser.class)
		@ConditionalOnProperty(prefix = LoadbalancerProperties.PREFIX
			+ ".isolation", name = "enabled", havingValue = "true", matchIfMissing = true)
		public IRuleChooser customRuleChooser(ApplicationContext context) {
			IRuleChooser chooser = new RoundRuleChooser();

			if (org.apache.commons.lang3.StringUtils.isNotBlank(properties.getChooser())) {
				try {
					Class<?> ruleClass = ClassUtils.forName(properties.getChooser(),
						context.getClassLoader());
					chooser = (IRuleChooser) ruleClass.newInstance();
				} catch (ClassNotFoundException e) {
					LogUtil.error("没有找到定义的选择器，将使用内置的选择器", e);
				} catch (InstantiationException | IllegalAccessException e) {
					LogUtil.error("没法创建定义的选择器，将使用内置的选择器", e);
				}
			}
			return chooser;
		}

		@Bean
		@ConditionalOnMissingBean(value = IRuleChooser.class)
		public IRuleChooser defaultRuleChooser() {
			return new RoundRuleChooser();
		}

		@Bean
		@ConditionalOnProperty(prefix = LoadbalancerProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
		public ReactorServiceInstanceLoadBalancer versionServiceLoadBalancer(
			Environment environment,
			LoadBalancerClientFactory loadBalancerClientFactory,
			IRuleChooser ruleChooser) {
			String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);

			return new VersionLoadBalancer(
				loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
				name,
				ruleChooser);
		}
	}

}
