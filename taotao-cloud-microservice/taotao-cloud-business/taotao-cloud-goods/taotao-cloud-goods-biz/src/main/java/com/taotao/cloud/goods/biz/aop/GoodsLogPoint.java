package com.taotao.cloud.goods.biz.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 订单日志注解
 * <pre class="code">
 *  @OrderLogPoint(description = "'订单['+#orderSn+']修改价格，修改后价格为['+#orderPrice+']'", orderSn = "#orderSn")
 * </pre>
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-07 20:29:25
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GoodsLogPoint {

	/**
	 * 日志名称
	 */
	String description();

	/**
	 * 订单编号
	 */
	String orderSn();

}
