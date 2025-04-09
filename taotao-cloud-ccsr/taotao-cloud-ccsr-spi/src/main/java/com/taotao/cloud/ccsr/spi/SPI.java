package com.taotao.cloud.ccsr.spi;

import java.lang.annotation.*;

/**
 * SPI Extend the processing.
 * <a href="https://github.com/apache/dubbo/blob/master/dubbo-common/src/main/java/org/apache/dubbo/common/extension">Apache Dubbo Common Extension</a>.
 *
 * @see ExtensionLoader
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

    /**
     * Value string.
     *
     * @return the string
     */
    String value() default "";
}
