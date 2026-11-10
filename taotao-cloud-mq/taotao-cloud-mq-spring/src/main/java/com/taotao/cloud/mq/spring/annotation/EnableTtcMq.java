package com.taotao.cloud.mq.spring.annotation;

import com.taotao.cloud.mq.spring.autoconfigure.TtcMqAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用 ttc-MQ 功能.
 * <p>
 * 使用示例:
 * <pre>
 * {@literal @}SpringBootApplication
 * {@literal @}EnableTtcMq
 * public class MyApplication {
 *     public static void main(String[] args) {
 *         SpringApplication.run(MyApplication.class, args);
 *     }
 * }
 * </pre>
 * <p>
 * 标注此注解后, Spring Boot 自动:
 * <ul>
 *   <li>创建 TtcMqTemplate Bean (类似 RabbitTemplate)</li>
 *   <li>扫描 {@literal @}TtcMqListener 注解的方法, 注册为消息消费者</li>
 *   <li>自动管理 Producer / Consumer 的生命周期</li>
 * </ul>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(TtcMqAutoConfiguration.class)
public @interface EnableTtcMq {
}
