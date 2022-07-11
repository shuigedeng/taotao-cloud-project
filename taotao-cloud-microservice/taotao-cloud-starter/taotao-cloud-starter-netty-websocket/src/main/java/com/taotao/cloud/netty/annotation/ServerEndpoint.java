package com.taotao.cloud.netty.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当ServerEndpointExporter类通过Spring配置进行声明并被使用，它将会去扫描带有@ServerEndpoint注解的类 被注解的类将被注册成为一个WebSocket端点 所有的配置项都在这个注解的属性中 ( 如:@ServerEndpoint("/ws") )
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-11 09:14:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServerEndpoint {

	@AliasFor("path")
	String value() default "/";

	/**
	 * WebSocket的path,也可以用value来设置
	 */
	@AliasFor("value")
	String path() default "/";

	/**
	 * WebSocket的host,"0.0.0.0"即是所有本地地址
	 */
	String host() default "0.0.0.0";

	/**
	 * WebSocket绑定端口号。如果为0，则使用随机端口(端口获取可见 多端点服务)
	 */
	String port() default "80";

	/**
	 * bossEventLoopGroup的线程数
	 */
	String bossLoopGroupThreads() default "1";

	/**
	 * workerEventLoopGroup的线程数
	 */
	String workerLoopGroupThreads() default "0";

	/**
	 * 是否添加WebSocketServerCompressionHandler到pipeline
	 */
	String useCompressionHandler() default "false";

	//------------------------- option -------------------------

	/**
	 * 与Netty的ChannelOption.CONNECT_TIMEOUT_MILLIS一致
	 */
	String optionConnectTimeoutMillis() default "30000";

	/**
	 * 与Netty的ChannelOption.SO_BACKLOG一致
	 */
	String optionSoBacklog() default "128";

	//------------------------- childOption -------------------------

	/**
	 * 与Netty的ChannelOption.WRITE_SPIN_COUNT一致
	 */
	String childOptionWriteSpinCount() default "16";

	/**
	 * 与Netty的ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK一致,但实际上是使用ChannelOption.WRITE_BUFFER_WATER_MARK
	 */
	String childOptionWriteBufferHighWaterMark() default "65536";

	/**
	 * 与Netty的ChannelOption.WRITE_BUFFER_LOW_WATER_MARK一致,但实际上是使用 ChannelOption.WRITE_BUFFER_WATER_MARK
	 */
	String childOptionWriteBufferLowWaterMark() default "32768";

	/**
	 * 与Netty的ChannelOption.SO_RCVBUF一致
	 */
	String childOptionSoRcvbuf() default "-1";

	/**
	 * 与Netty的ChannelOption.SO_SNDBUF一致
	 */
	String childOptionSoSndbuf() default "-1";

	/**
	 * 与Netty的ChannelOption.TCP_NODELAY一致
	 */
	String childOptionTcpNodelay() default "true";

	/**
	 * 与Netty的ChannelOption.SO_KEEPALIVE一致
	 */
	String childOptionSoKeepalive() default "false";

	/**
	 * 与Netty的ChannelOption.SO_LINGER一致
	 */
	String childOptionSoLinger() default "-1";

	/**
	 * 与Netty的ChannelOption.ALLOW_HALF_CLOSURE一致
	 */
	String childOptionAllowHalfClosure() default "false";

	//------------------------- idleEvent -------------------------

	/**
	 * 与IdleStateHandler中的readerIdleTimeSeconds一致，并且当它不为0时，将在pipeline中添加IdleStateHandler
	 */
	String readerIdleTimeSeconds() default "0";

	/**
	 * 与IdleStateHandler中的writerIdleTimeSeconds一致，并且当它不为0时，将在pipeline中添加IdleStateHandler
	 */
	String writerIdleTimeSeconds() default "0";

	/**
	 * 与IdleStateHandler中的allIdleTimeSeconds一致，并且当它不为0时，将在pipeline中添加IdleStateHandler
	 */
	String allIdleTimeSeconds() default "0";

	//------------------------- handshake -------------------------

	/**
	 * 最大允许帧载荷长度
	 */
	String maxFramePayloadLength() default "65536";

	//------------------------- eventExecutorGroup -------------------------

	/**
	 * 是否使用另一个线程池来执行耗时的同步业务逻辑
	 */
	String useEventExecutorGroup() default "true"; //use EventExecutorGroup(another thread pool) to perform time-consuming synchronous business logic

	/**
	 * eventExecutorGroup的线程数
	 */
	String eventExecutorGroupThreads() default "16";

	//------------------------- ssl (refer to spring Ssl) -------------------------

	/**
	 * 与spring-boot的server.ssl.key-password一致
	 * {@link org.springframework.boot.web.server.Ssl}
	 */
	String sslKeyPassword() default "";

	/**
	 * 与spring-boot的server.ssl.key-store一致
	 */
	String sslKeyStore() default "";            //e.g. classpath:server.jks

	/**
	 * 与spring-boot的server.ssl.key-store-password一致
	 */
	String sslKeyStorePassword() default "";

	/**
	 * 与spring-boot的server.ssl.key-store-type一致
	 */
	String sslKeyStoreType() default "";        //e.g. JKS

	/**
	 * 与spring-boot的server.ssl.trust-store一致
	 */
	String sslTrustStore() default "";

	/**
	 * 与spring-boot的server.ssl.trust-store-password一致
	 */
	String sslTrustStorePassword() default "";

	/**
	 * 与spring-boot的server.ssl.trust-store-type一致
	 */
	String sslTrustStoreType() default "";

	//------------------------- cors (refer to spring CrossOrigin) -------------------------

	/**
	 * 与spring-boot的@CrossOrigin#origins一致
	 * {@link org.springframework.web.bind.annotation.CrossOrigin}
	 */

	String[] corsOrigins() default {};

	/**
	 * 与spring-boot的@CrossOrigin#allowCredentials一致
	 */
	String corsAllowCredentials() default "";


}
