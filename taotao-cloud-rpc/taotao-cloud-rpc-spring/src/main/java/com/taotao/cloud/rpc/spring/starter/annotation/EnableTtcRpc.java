package com.taotao.cloud.rpc.spring.starter.annotation;

import com.taotao.cloud.rpc.spring.starter.config.TtcRpcAutoConfiguration;
import com.taotao.cloud.rpc.spring.starter.config.TtcRpcReferenceInjector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 ttc-RPC 框架
 * <p>
 * 标注在 Spring Boot 启动类上，激活 ttc-RPC 自动装配。
 * <pre>
 * &#64;SpringBootApplication
 * &#64;EnableTtcRpc(scanBasePackages = "com.zifang.demo")
 * public class OrderServiceApplication { ... }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({TtcRpcAutoConfiguration.class, TtcRpcReferenceInjector.class})
public @interface EnableTtcRpc {

    /**
     * 扫描的基础包路径，用于发现 @TtcRpcService 和 @TtcRpcReference 注解。
     * <p>
     * 注意：{@code @TtcRpcService} 的导出与 {@code @TtcRpcReference} 的注入都由
     * {@link TtcRpcAutoConfiguration} 注册的 BeanPostProcessor / Bean 生命周期钩子完成，
     * 它们会遍历容器里全部 Bean，因此本属性当前<b>不参与</b>任何扫描；
     * 保留它只是为了与 {@code @SpringBootApplication} 的书写习惯兼容。
     */
    String[] scanBasePackages() default {};
}
