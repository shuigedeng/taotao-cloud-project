package com.taotao.cloud.rpc.common.spi;

import java.lang.annotation.*;

/**
 * 自动激活注解
 * <p>
 * 标记扩展实现类，使其在特定条件下被自动加载。
 * <p>
 * 与 @SPI/@Adaptive 配合使用：@Activate 标注的类会被收集到 "激活扩展" 集合，
 * 在运行时通过 ExtensionLoader.getActivateExtension(url, group) 加载。
 *
 * <pre>
 * &#64;Activate(group = "consumer", order = 10)
 * public class ConsumerContextFilter implements Filter { ... }
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Activate {

    /**
     * 所属分组
     * <p>
     * 常见的组名：
     * <ul>
     *   <li>consumer - 消费端激活</li>
     *   <li>provider - 服务端激活</li>
     *   <li>client - 通用客户端</li>
     *   <li>server - 通用服务端</li>
     * </ul>
     */
    String[] group() default {};

    /**
     * 激活条件
     * <p>
     * 数组每项形如 "key:value"，表示 URL 中该参数的值必须匹配 value 才激活。
     */
    String[] value() default {};

    /**
     * 排序顺序
     * <p>
     * 数字越小优先级越高。
     */
    int order() default 0;
}
