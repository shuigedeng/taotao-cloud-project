package com.taotao.cloud.canal.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/**
 * canal 监听器注解，继承 @Component
 *
 * @author 阿导
 * @CopyRight 萬物皆導
 * @created 2018/5/28 14:05
 * @Modified_By 阿导 2018/5/28 14:05
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CanalEventListener {

	@AliasFor(annotation = Component.class)
	String value() default "";

}
