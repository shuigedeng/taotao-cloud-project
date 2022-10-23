package com.taotao.cloud.mongodb.helper.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * IgnoreDocument
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-10 22:35:53
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface IgnoreDocument {


}
