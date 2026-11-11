package com.taotao.cloud.rpc.common.spi;

import java.lang.annotation.*;

/**
 * 扩展点注解
 * <p>
 * 标记一个接口为可扩展 SPI 接口。
 * <pre>
 * &#64;SPI("failover")
 * public interface Cluster {
 *     ...
 * }
 *
 * // 实现
 * public class FailoverCluster implements Cluster { ... }
 * </pre>
 * <p>
 * 资源文件路径：META-INF/ttc-rpc/com.taotao.cloud.rpc.cluster.Cluster
 * <pre>
 * failover=com.taotao.cloud.rpc.cluster.FailoverCluster
 * failfast=com.taotao.cloud.rpc.cluster.FailfastCluster
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

    /**
     * 默认实现 key
     */
    String value() default "";
}
