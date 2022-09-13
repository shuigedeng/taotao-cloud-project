package com.taotao.cloud.data.mybatisplus.datascope.dataPermission.aop;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * Advisor通常由另两个组件组成——Advice接口和Pointcut接口，其中Advice表示实际增强的逻辑入口(通知)，Pointcut表示哪些类或者哪些方法需要被拦截(切入点)。
 * 切点切面PointcutAdvisor
 * 代表具有切点的切面，它可以通过任意Pointcut和Advice定义一个切面，这样就可以通过类、方法名以及方位等信息灵活的定义切面的连接点，提供更具实用性的切面。
 *
 * @author fxz
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class DataPermissionAnnotationAdvisor extends DefaultPointcutAdvisor {

	private final Advice advice;

	private final Pointcut pointcut;

	public DataPermissionAnnotationAdvisor() {
		// 自定义通知
		this.advice = DataPermissionCustomAdvice.of();
		// 自定义切入点
		this.pointcut = DataPermissionCustomPointcut.of();
	}

}
