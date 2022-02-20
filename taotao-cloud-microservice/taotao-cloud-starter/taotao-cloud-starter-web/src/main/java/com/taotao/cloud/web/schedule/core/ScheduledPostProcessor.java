package com.taotao.cloud.web.schedule.core;


import static com.taotao.cloud.web.schedule.common.utils.AnnotationUtils.changeAnnotationValue;

import com.taotao.cloud.web.schedule.ScheduledException;
import com.taotao.cloud.web.schedule.common.annotation.ScheduledBean;
import com.taotao.cloud.web.schedule.model.ScheduledJobModel;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ReflectionUtils;

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

		//System.out.println(beanName);
		//
		//ClassUtils.getUserClass(bean.getClass()).getDeclaredMethods();
		//
		//List<Method> methods = Arrays.asList(bean.getClass().getDeclaredMethods());
		//List<Method> methods2 = Arrays.asList(bean.getClass().getSuperclass().getDeclaredMethods());
		//methods.addAll(methods2);
		//

		if (beanName.equals("testTask")) {
			System.out.println("sdfasdf");
		}

		//Method[] methods = ClassUtils.getUserClass(bean.getClass()).getDeclaredMethods();

		List<Method> methods = findAllMethod(bean.getClass());
		//获取bean的方法
		if (methods.size() > 0) {
			for (Method method : methods) {
				Scheduled scheduled = AnnotationUtils.findAnnotation(method, Scheduled.class);
				ScheduledBean scheduledBean = AnnotationUtils.findAnnotation(method,
					ScheduledBean.class);

				ScheduledJobModel scheduledJobModel = null;
				if (scheduled != null) {
					scheduledJobModel = new ScheduledJobModel(scheduled, method, bean);
				}

				if (scheduledBean != null) {
					scheduledJobModel = new ScheduledJobModel(scheduledBean, method, bean);
				}

				if(null == scheduledJobModel) {
					continue;
				}

				//Scheduled annotation = method.getAnnotation(Scheduled.class);
				//if (annotation == null) {
				//	continue;
				//}

				if (!scheduledJobModel.check()) {
					throw new ScheduledException(
						"在" + beanName + "Bean中" + method.getName() + "方法的注解参数错误");
				}
				String name = beanName + "." + method.getName();
				scheduledConfig.addScheduledSource(name, scheduledJobModel);
				try {
					clearOriginalScheduled(scheduled);
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

	private List<Method> findAllMethod(Class clazz) {
		final List<Method> methods = new LinkedList<>();
		ReflectionUtils.doWithMethods(clazz, methods::add);
		return methods;
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
