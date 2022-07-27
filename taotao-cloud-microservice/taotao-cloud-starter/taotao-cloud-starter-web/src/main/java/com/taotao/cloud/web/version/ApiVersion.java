package com.taotao.cloud.web.version;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RESTful API接口版本定义
 * <p>为接口提供优雅的版本路径，效果如下：
 * <blockquote>
 * <p>&#064;ApiVersion(1)
 * <p>&#064;RequestMapping("/{version}/user")
 * </blockquote>
 * 实际请求路径值：/v1/user
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-27 20:20:55
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ApiVersion {

	/**
	 * RESTful API接口版本号
	 * <p>最近优先原则：在方法上的 {@link ApiVersion} 可覆盖在类上面的 {@link ApiVersion}，如下：
	 * <p>类上面的 {@link #value()} 值 = 1.1，
	 * <p>方法上面的 {@link #value()} 值 = 2.1，
	 * <p>最终效果：v2.1
	 */
	double value() default 1;
	
	/**
	 * 是否废弃版本接口
	 * <p>客户端请求废弃版本接口时将抛出错误提示：
	 * <p>当前版本已停用，请升级到最新版本
	 */
	boolean deprecated() default false;
	
}
