package com.taotao.cloud.rpc.common.spi;

import java.lang.annotation.*;

/**
 * 自适应扩展点注解
 * <p>
 * 标记在方法上，表示该方法需要根据 URL 参数动态选择具体扩展实现。
 * <p>
 * value 为 URL 中参数的 key（如 "loadbalance"、"cluster"、"protocol" 等），
 * 不指定则默认使用方法第一个参数名为 key。
 *
 * <pre>
 * &#64;Adaptive
 * public Invoker join(Directory directory) {
 *     // 内部根据 URL.getParameter("loadbalance") 选取具体 LB
 * }
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Adaptive {

    /**
     * 自适应 key
     * <p>
     * 默认从 URL 中按这个 key 取值来决定使用哪个扩展实现。
     * 多个值表示依次 fallback。
     */
    String[] value() default {};
}
