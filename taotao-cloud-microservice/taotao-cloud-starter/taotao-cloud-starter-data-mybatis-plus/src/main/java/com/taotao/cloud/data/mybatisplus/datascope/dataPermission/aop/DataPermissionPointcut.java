package com.taotao.cloud.data.mybatisplus.datascope.dataPermission.aop;

import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.annotation.DataPermission;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * 自定义Pointcut
 *
 * @version 1.0
 * @date 2022/9/10 11:09
 */
public class DataPermissionPointcut {

	/**
	 * 自定义切入点 复合切点，为创建多个切点而提供的方便操作类。它所有的方法都返回ComposablePointcut类，这样，我们就可以使用链接表达式对其进行操作。
	 */
	protected static Pointcut of() {
		Pointcut classPointcut = new AnnotationMatchingPointcut(DataPermission.class, true);
		Pointcut methodPointcut = new AnnotationMatchingPointcut(null, DataPermission.class, true);
		return new ComposablePointcut(classPointcut).union(methodPointcut);
	}

	private DataPermissionPointcut() {
	}

}
