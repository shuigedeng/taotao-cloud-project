package com.taotao.cloud.canal.annotation;

import com.alibaba.otter.canal.protocol.CanalEntry;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 监听数据库的操作
 *
 * @author 阿导
 * @CopyRight 萬物皆導
 * @created 2018/5/28 15:39
 * @Modified_By 阿导 2018/5/28 15:39
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ListenPoint {

	/**
	 * canal 指令 default for all
	 *
	 * @return canal destination
	 * @author 阿导
	 * @time 2018/5/28 15:49
	 * @CopyRight 万物皆导
	 */
	String destination() default "";

	/**
	 * 数据库实例
	 *
	 * @return canal destination
	 * @author 阿导
	 * @time 2018/5/28 15:49
	 * @CopyRight 万物皆导
	 */
	String[] schema() default {};

	/**
	 * 监听的表 default for all
	 *
	 * @return canal destination
	 * @author 阿导
	 * @time 2018/5/28 15:50
	 * @CopyRight 万物皆导
	 */
	String[] table() default {};

	/**
	 * 监听操作的类型
	 * <p>
	 * default for all
	 *
	 * @return canal event type
	 * @author 阿导
	 * @time 2018/5/28 15:50
	 * @CopyRight 万物皆导
	 */
	CanalEntry.EventType[] eventType() default {};

}
