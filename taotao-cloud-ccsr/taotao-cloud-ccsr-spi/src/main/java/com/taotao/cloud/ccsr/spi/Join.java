package com.taotao.cloud.ccsr.spi;

import java.lang.annotation.*;

/**
 * Join
 * Adding this annotation to a class indicates joining the extension mechanism.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Join {

    /**
     * It will be sorted according to the current serial number.
     *
     * @return int.
     */
    int order() default 0;


    /**
     * Indicates that the object joined by @Join is a singleton,
     * otherwise a completely new instance is created each time.
     *
     * @return true or false.
     */
    boolean isSingleton() default true;

}
