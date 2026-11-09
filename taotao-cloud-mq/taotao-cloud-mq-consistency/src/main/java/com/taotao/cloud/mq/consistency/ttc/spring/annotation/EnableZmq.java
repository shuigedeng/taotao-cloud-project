package com.taotao.cloud.mq.consistency.ttc.spring.annotation;

import com.taotao.cloud.mq.consistency.ttc.spring.autoconfigure.ZmqAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 Z-MQ 功能.
 * <p>
 * 使用示例:
 * <pre>
 * {@literal @}SpringBootApplication
 * {@literal @}EnableZmq
 * public class MyApplication {
 *     public static void main(String[] args) {
 *         SpringApplication.run(MyApplication.class, args);
 *     }
 * }
 * </pre>
 * <p>
 * 标注此注解后, Spring Boot 自动:
 * <ul>
 *   <li>创建 ZmqTemplate Bean (类似 RabbitTemplate)</li>
 *   <li>扫描 {@literal @}ZmqListener 注解的方法, 注册为消息消费者</li>
 *   <li>自动管理 Producer / Consumer 的生命周期</li>
 * </ul>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ZmqAutoConfiguration.class)
public @interface EnableZmq {
}
