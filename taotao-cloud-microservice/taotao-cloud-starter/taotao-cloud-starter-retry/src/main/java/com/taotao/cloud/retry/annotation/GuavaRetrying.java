package com.taotao.cloud.retry.annotation;

import com.taotao.cloud.retry.exception.GuavaRetryException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 番石榴重试
 *
 * <pre class="code">
 *     &#064;Service
 * public class GetnameserviceImpl implements Getnameservice {
 *
 *     &#064;Override
 *     &#064;GuavaRetrying(exceptionClass  = Exception.class, attemptNumber = 3,waitStrategySleepTime=5)
 *     public String getName() throws Exception {
 *         SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
 *         Date date = new Date(System.currentTimeMillis());
 *         System.out.println("执行了:          "+formatter.format(date));
 *         throw new Exception("我就想报错");
 *     }
 * }
 * </pre>
 *
 * @author shuigedeng
 * @version 2022.08
 * @since 2022-08-08 10:21:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GuavaRetrying {
	//异常
	Class<? extends Throwable>[] exceptionClass() default {GuavaRetryException.class};

	//重试次数
	int attemptNumber() default 0;

	//等待时间
	long waitStrategySleepTime() default 0;

	//持续时间; 期间
	long duration() default 0;
}

