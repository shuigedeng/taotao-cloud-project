package com.taotao.cloud.lock.kylin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于重复注解 KylinLock
 * 分布式锁注解
 *
 * @author wangjinkui
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface KylinLocks {
    /**
     * @return 分布式锁注解
     */
    KylinLock[] value();
}
