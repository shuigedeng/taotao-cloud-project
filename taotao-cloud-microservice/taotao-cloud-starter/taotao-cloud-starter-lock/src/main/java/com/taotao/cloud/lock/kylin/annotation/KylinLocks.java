package com.taotao.cloud.lock.kylin.annotation;

import java.lang.annotation.*;

/**
 * 用于重复注解 KylinLock
 * 分布式锁注解
 *
 * @author wangjinkui
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface KylinLocks {
    /**
     * @return 分布式锁注解
     */
    KylinLock[] value();
}
