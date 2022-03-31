package com.taotao.cloud.member.biz.aop.annotation;


import java.lang.annotation.*;

/**
 * 会员积分操作aop
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PointLogPoint {

}
