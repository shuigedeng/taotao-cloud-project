package com.taotao.cloud.java.javase.day17.chap17_5;
/**
 * 创建注解类型  @interface
 * @author shuigedeng
 *
 */
public @interface MyAnnotation {
	//属性(类似方法)
	String name() default "张三";
	int age() default 20;
	
}
