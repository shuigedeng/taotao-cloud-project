package com.taotao.cloud.netty.annotation;

import com.taotao.cloud.netty.configuration.NettyWebSocketAutoConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(NettyWebSocketAutoConfiguration.class)
public @interface EnableTaoTaoCloudWebSocket {

	String[] scanBasePackages() default {};

}
