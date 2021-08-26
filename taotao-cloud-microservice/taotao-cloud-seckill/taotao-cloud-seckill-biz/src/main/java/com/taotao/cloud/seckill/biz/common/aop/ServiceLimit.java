package com.taotao.cloud.seckill.biz.common.aop;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 自定义注解  限流
 * 创建者	张志朋
 * 创建时间	2015年6月3日
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public  @interface ServiceLimit {
	/**
	 * 描述
	 */
	String description()  default "";

	/**
	 * key
	 */
	String key() default "";

	/**
	 * 类型
	 */
	LimitType limitType() default LimitType.CUSTOMER;

	enum LimitType {
		/**
		 * 自定义key
		 */
		CUSTOMER,
		/**
		 * 根据请求者IP
		 */
		IP
	}
}
