package com.taotao.cloud.data.mybatis.plus.datascope.dataPermission.aop;

import com.fxz.common.dataPermission.annotation.DataPermission;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * DataPermission 注解的 Advisor 实现类 在spring aop中Advisor是一个接口，代表被拦截方法需要增强的逻辑。
 * Advisor通常由另两个组件组成——Advice接口和Pointcut接口，其中Advice表示实际增强的逻辑入口(通知)，Pointcut表示哪些类或者哪些方法需要被拦截(切入点)
 *
 * @author fxz
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class DataPermissionAnnotationAdvisor extends AbstractPointcutAdvisor {

	private final Advice advice;

	private final Pointcut pointcut;

	public DataPermissionAnnotationAdvisor() {
		// 自定义通知 处理业务逻辑
		this.advice = new DataPermissionAnnotationInterceptor();
		// 自定义切入点
		this.pointcut = this.buildPointcut();
	}

	/**
	 * 自定义切入点
	 */
	protected Pointcut buildPointcut() {
		Pointcut classPointcut = new AnnotationMatchingPointcut(DataPermission.class, true);
		Pointcut methodPointcut = new AnnotationMatchingPointcut(null, DataPermission.class, true);
		return new ComposablePointcut(classPointcut).union(methodPointcut);
	}

}
