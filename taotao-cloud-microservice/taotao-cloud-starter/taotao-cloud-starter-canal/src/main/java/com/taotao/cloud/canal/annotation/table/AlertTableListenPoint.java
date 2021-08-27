package com.taotao.cloud.canal.annotation.table;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.wwjd.starter.canal.annotation.ListenPoint;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 次改字段属性监听器
 *
 * @author 阿导
 * @CopyRight 万物皆导
 * @created 2018年05月28日 19:40:00
 * @Modified_By 阿导 2018/5/28 19:40
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ListenPoint(eventType = CanalEntry.EventType.ALTER)
public @interface AlertTableListenPoint {
	/**
	 * canal 指令
	 * default for all
	 *
	 * @return canal destination
	 * @author 阿导
	 * @time 2018/5/28 15:49
	 * @CopyRight 万物皆导
	 */
	@AliasFor(annotation = ListenPoint.class)
	String destination() default "";
	
	/**
	 * 数据库实例
	 *
	 * @return canal destination
	 * @author 阿导
	 * @time 2018/5/28 15:49
	 * @CopyRight 万物皆导
	 */
	@AliasFor(annotation = ListenPoint.class)
	String[] schema() default {};
	
	/**
	 * 监听的表
	 * default for all
	 *
	 * @return canal destination
	 * @author 阿导
	 * @time 2018/5/28 15:50
	 * @CopyRight 万物皆导
	 */
	@AliasFor(annotation = ListenPoint.class)
	String[] table() default {};
}
