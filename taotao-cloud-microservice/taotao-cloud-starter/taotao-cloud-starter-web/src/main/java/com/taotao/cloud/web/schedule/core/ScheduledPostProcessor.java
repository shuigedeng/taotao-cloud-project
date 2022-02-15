package com.taotao.cloud.web.schedule.core;


import static com.taotao.cloud.web.schedule.common.utils.AnnotationUtils.changeAnnotationValue;

import com.taotao.cloud.web.schedule.ScheduledException;
import com.taotao.cloud.web.schedule.model.ScheduledJobModel;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * SuperScheduledPostProcessor
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:02:03
 */
public class ScheduledPostProcessor implements BeanPostProcessor, ApplicationContextAware {

	protected final Log logger = LogFactory.getLog(getClass());

	private ApplicationContext applicationContext;

	/**
	 * 实例化bean之前的操作
	 *
	 * @param bean     bean实例
	 * @param beanName bean的Name
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
		throws BeansException {
		return bean;
	}

	/**
	 * 实例化bean之后的操作
	 *
	 * @param bean     bean实例
	 * @param beanName bean的Name
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean,
		String beanName) throws BeansException {
		ScheduledConfig scheduledConfig = applicationContext.getBean(
			ScheduledConfig.class);

		//获取bean的方法
		Method[] methods = bean.getClass().getDeclaredMethods();
		if (methods.length > 0) {
			for (Method method : methods) {
				Scheduled annotation = method.getAnnotation(Scheduled.class);
				if (annotation == null) {
					continue;
				}

				ScheduledJobModel scheduledJobModel = new ScheduledJobModel(annotation, method,
					bean);
				if (!scheduledJobModel.check()) {
					throw new ScheduledException(
						"在" + beanName + "Bean中" + method.getName() + "方法的注解参数错误");
				}
				String name = beanName + "." + method.getName();
				scheduledConfig.addScheduledSource(name, scheduledJobModel);
				try {
					clearOriginalScheduled(annotation);
				} catch (Exception e) {
					throw new ScheduledException("在关闭原始方法" + beanName + method.getName() + "时出现错误");
				}
			}
		}
		return bean;
	}

	/**
	 * 修改注解原先的属性
	 *
	 * @param annotation 注解实例对象
	 */
	private void clearOriginalScheduled(Scheduled annotation) throws Exception {
		changeAnnotationValue(annotation, "cron", Scheduled.CRON_DISABLED);
		changeAnnotationValue(annotation, "fixedDelay", -1L);
		changeAnnotationValue(annotation, "fixedDelayString", "");
		changeAnnotationValue(annotation, "fixedRate", -1L);
		changeAnnotationValue(annotation, "fixedRateString", "");
		changeAnnotationValue(annotation, "initialDelay", -1L);
		changeAnnotationValue(annotation, "initialDelayString", "");
	}


	/**
	 * 获取SpringBoot的上下文
	 *
	 * @param applicationContext SpringBoot的上下文
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
