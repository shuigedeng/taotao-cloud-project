package com.taotao.cloud.order.biz.aop;

import java.lang.annotation.*;

/**
 * 订单日志AOP注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderLogPoint {

    /**
     * 日志名称
     *
     * @return
     */
    String description();

    /**
     * 订单编号
     *
     * @return
     */
    String orderSn();

}
