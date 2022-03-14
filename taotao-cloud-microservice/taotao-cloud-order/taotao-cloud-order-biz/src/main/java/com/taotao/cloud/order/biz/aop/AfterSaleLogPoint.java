
package com.taotao.cloud.order.biz.aop;

import java.lang.annotation.*;

/**
 * 售后日志AOP注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterSaleLogPoint {

    /**
     * 日志名称
     *
     * @return
     */
    String description();

    /**
     * 售后SN
     *
     * @return
     */
    String sn();

}
