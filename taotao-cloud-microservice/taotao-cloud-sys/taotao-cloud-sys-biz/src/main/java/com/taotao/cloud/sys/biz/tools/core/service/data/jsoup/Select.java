package com.taotao.cloud.sys.biz.tools.core.service.data.jsoup;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface Select {
    /**
     * 选择到某个元素
     * @return
     */
    String value();

    /**
     * 取某个属性的值
     * content : 取内容值
     * @return
     */
    String attr() default "";
}
