package com.taotao.cloud.data.analysis.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@DS("doris")
public @interface Doris {
}
