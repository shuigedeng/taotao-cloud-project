package com.taotao.cloud.common.utils.context;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用SpringUtil, 即注入SpringUtil到容器中
 *
 * @author sidian
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ContextUtils.class)
public @interface EnableContextUtils {
}
