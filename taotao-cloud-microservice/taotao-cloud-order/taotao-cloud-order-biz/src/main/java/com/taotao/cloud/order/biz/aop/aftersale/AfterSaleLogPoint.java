
package com.taotao.cloud.order.biz.aop.aftersale;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 售后日志注解
 *
 * @AfterSaleLogPoint(sn = "#rvt.sn", description = "'售后申请:售后编号['+#rvt.sn+']'")
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-07 20:29:56
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterSaleLogPoint {

	/**
	 * 日志名称
	 */
	String description();

	/**
	 * 售后SN
	 */
	String sn();

}
