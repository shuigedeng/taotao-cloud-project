package com.taotao.cloud.tracing.micrometer.autoconfigure;

import com.p6spy.engine.spy.P6ModuleManager;
import com.taotao.cloud.data.p6spy.properties.P6spyProperties;
import java.lang.reflect.Field;
import javax.sql.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils;

/**
 * Sleuth p6spy 自动装配
 */
@AutoConfiguration
public class SleuthP6spyAutoConfiguration implements BeanPostProcessor, Ordered, ApplicationContextAware {

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
					Object val = field.get(this.p6spyProperties);

					String name = field.getName();
					if (val instanceof String valStr) {
						String propertyKey = String.format("%s.%s", "p6spy.config", name);

						if ("modulelist".equals(name)) {
							valStr = valStr + ",brave.p6spy.TracingP6Factory";
						}

						System.setProperty(propertyKey, valStr);
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
