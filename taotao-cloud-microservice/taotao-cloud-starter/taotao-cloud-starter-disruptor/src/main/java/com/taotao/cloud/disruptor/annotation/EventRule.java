package com.taotao.cloud.disruptor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented	
@Inherited		
public @interface EventRule {
	
	/**
	 * Ant风格的事件分发规则表达式,格式为：/event/tags/keys，如：/Event-DC-Output/TagA-Output/**
	 * @return 规则表达式
	 */
	String value() default "*";
	
}
