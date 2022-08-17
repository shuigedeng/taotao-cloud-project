package com.taotao.cloud.p6spy.properties.p6spy;

import com.p6spy.engine.spy.P6ModuleManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;

/**
 * Before the DataSource is initialized, set the P6spy configuration
 *
 */
public class CusP6spyConfigLoaderBeanPostProcessor implements BeanPostProcessor, Ordered, ApplicationContextAware {

	boolean isLoad = false;

	private P6spyProperties p6spyProperties;

	private ApplicationContext applicationContext;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		try {
			if (bean instanceof DataSource && !isLoad) {
				p6spyProperties = getP6spyProperties();
				Field[] fields = P6spyProperties.class.getDeclaredFields();
				for (Field field : fields) {
					ReflectionUtils.makeAccessible(field);
					String val = (String) field.get(this.p6spyProperties);
					if (val != null) {
						String propertyKey = String.format("%s.%s", "p6spy.config", field.getName());
						System.setProperty(propertyKey, val);
					}
				}

				P6ModuleManager.getInstance().reload();
				isLoad = true;
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public P6spyProperties getP6spyProperties() {
		if (p6spyProperties == null) {
			p6spyProperties = applicationContext.getBean(P6spyProperties.class);
		}
		return p6spyProperties;
	}

	@Override
	public int getOrder() {
		return 30;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
