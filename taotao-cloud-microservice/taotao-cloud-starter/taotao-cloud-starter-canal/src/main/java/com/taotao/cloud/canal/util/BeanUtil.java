package com.taotao.cloud.canal.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * bean 工具类
 *
 * @author 阿导
 * @CopyRight 万物皆导
 * @created 2018年05月28日 13:58:00
 * @Modified_By 阿导 2018/5/28 13:58
 */
@Component
public class BeanUtil implements ApplicationContextAware {
	/**
	 * 上下文
	 */
	private static ApplicationContext applicationContext;
	
	/**
	 * 默认构造方法，注入上下文
	 *
	 * @author 阿导
	 * @time 2018/5/28 14:01
	 * @CopyRight 万物皆导
	 * @param applicationContext
	 * @return
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BeanUtil.applicationContext=applicationContext;
	}
	
	/**
	 * 获取 bean
	 *
	 * @author 阿导
	 * @time 2018/5/28 14:02
	 * @CopyRight 万物皆导
	 * @param clazz
	 * @return
	 */
	
	public static <T> T getBean(Class<T> clazz) {
		T obj;
		try {
			//从上下文获取 bean
			obj = applicationContext.getBean(clazz);
		} catch (Exception e) {
			obj = null;
		}
		//返回 bean
		return obj;
	}
	
	/**
	 * 获取 bean 的类型
	 *
	 * @author 阿导
	 * @time 2018/5/28 14:03
	 * @CopyRight 万物皆导
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getBeansOfType(Class<T> clazz) {
		//声明一个结果
		Map<String, T> map;
		try {
			//获取类型
			map = applicationContext.getBeansOfType(clazz);
		} catch (Exception e) {
			map = null;
		}
		//返回 bean 的类型
		return map == null ? null : new ArrayList<>(map.values());
	}
	
	
	/**
	 * 获取所有被注解的 bean
	 *
	 * @author 阿导
	 * @time 2018/5/28 14:04
	 * @CopyRight 万物皆导
	 * @param anno
	 * @return
	 */
	public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> anno) {
		Map<String, Object> map;
		try {
			//获取注解的 bean
			map = applicationContext.getBeansWithAnnotation(anno);
		} catch (Exception e) {
			map = null;
		}
		return map;
	}
}
