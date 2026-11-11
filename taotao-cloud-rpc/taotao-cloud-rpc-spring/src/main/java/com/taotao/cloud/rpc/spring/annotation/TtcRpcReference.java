package com.taotao.cloud.rpc.spring.annotation;

import java.lang.annotation.*;

/**
 * ttc-RPC 服务引用注解
 * 用于注入远程服务代理
 * <p>
 * 注意：这里<b>不</b>元标注 {@code @Autowired}。字段的代理由
 * {@code TtcRpcReferenceInjector} 按注解属性构造，若再让 Spring 的
 * {@code AutowiredAnnotationBeanPostProcessor} 按类型抢先注入，
 * 轻则双写同一字段、重则因容器里没有该接口的 Bean 而启动失败。
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TtcRpcReference {

    /**
     * 服务接口类
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 服务接口名称
     */
    String interfaceName() default "";

    /**
     * 服务版本
     */
    String version() default "1.0.0";

    /**
     * 服务分组
     */
    String group() default "";

    /**
     * 连接超时（毫秒）
     */
    int timeout() default 3000;

    /**
     * 重试次数
     */
    int retries() default 2;

    /**
     * 负载均衡策略
     * random: 随机
     * roundrobin: 轮询
     * leastactive: 最少活跃调用
     * consistenthash: 一致性哈希
     */
    String loadbalance() default "random";

    /**
     * 集群容错策略
     * failover: 失败重试
     * failfast: 快速失败
     * failsafe: 失败安全
     * failback: 失败恢复
     * broadcast: 广播
     * forking: 并行调用
     */
    String cluster() default "failover";

    /**
     * 是否异步调用
     */
    boolean async() default false;

    /**
     * 是否单向调用（oneway）
     */
    boolean oneway() default false;

    /**
     * 服务注册中心地址
     */
    String registry() default "127.0.0.1:8084";

    /**
     * 直连服务地址列表（绕过注册中心）
     * 多个以分号分隔，如 "ttcrpc://192.168.1.10:20880;ttcrpc://192.168.1.11:20880"
     */
    String url() default "";

    /**
     * 检查服务提供者是否存在
     */
    boolean check() default true;

    /**
     * 延迟初始化
     */
    boolean lazy() default false;

    /**
     * 连接数限制
     */
    int connections() default 0;

    /**
     * 客户端类型
     */
    String client() default "netty";

    /**
     * 序列化方式
     */
    String serialization() default "hessian2";
}
