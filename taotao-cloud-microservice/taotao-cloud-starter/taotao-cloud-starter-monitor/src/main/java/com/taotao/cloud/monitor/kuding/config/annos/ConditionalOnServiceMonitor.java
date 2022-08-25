package com.taotao.cloud.monitor.kuding.config.annos;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.taotao.cloud.monitor.kuding.config.conditions.OnServiceMonitorNoticeCondition;
import com.taotao.cloud.monitor.kuding.config.conditions.PrometheusEnabledCondition;
import org.springframework.context.annotation.Conditional;


@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({ PrometheusEnabledCondition.class, OnServiceMonitorNoticeCondition.class })
public @interface ConditionalOnServiceMonitor {

}
