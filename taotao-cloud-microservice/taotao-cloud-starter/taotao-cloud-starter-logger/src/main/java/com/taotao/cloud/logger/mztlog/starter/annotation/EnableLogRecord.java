package com.taotao.cloud.logger.mztlog.starter.annotation;

import com.taotao.cloud.logger.mztlog.starter.support.LogRecordConfigureSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogRecordConfigureSelector.class)
public @interface EnableLogRecord {

    String tenant();

    /**
     * ！不要删掉，为 null 就不代理了哦
     * true 都使用 CGLIB 代理
     * false 目标对象实现了接口 – 使用JDK动态代理机制(代理所有实现了的接口) 目标对象没有接口(只有实现类) – 使用CGLIB代理机制
     */
    boolean proxyTargetClass() default false;

    /**
     * Indicate how caching advice should be applied. The default is
     * {@link AdviceMode#PROXY}.
     *
     * @return 代理方式
     * @see AdviceMode
     */
    AdviceMode mode() default AdviceMode.PROXY;
}
