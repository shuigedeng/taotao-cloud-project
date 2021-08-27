package com.taotao.cloud.canal.annotation.table;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.annotation.ListenPoint;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 刪除索引操作
 *
 * @author 阿导
 * @CopyRight 青团社
 * @created 2018年05月30日 17:12:00
 * @Modified_By 阿导 2018/5/30 17:12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ListenPoint(eventType = CanalEntry.EventType.DINDEX)
public @interface DropIndexListenPoint {
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
