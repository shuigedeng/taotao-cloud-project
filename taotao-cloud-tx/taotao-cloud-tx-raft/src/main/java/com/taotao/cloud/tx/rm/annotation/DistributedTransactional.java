package com.taotao.cloud.tx.rm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 自定义的分布式事务注解
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedTransactional {


	// 标识当前是全局事务的开启者
	boolean isStart() default false;

	// 标识当前是全局事务的结束者
	boolean isEnd() default false;
}
