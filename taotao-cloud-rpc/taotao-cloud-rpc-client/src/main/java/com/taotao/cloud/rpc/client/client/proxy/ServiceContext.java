package com.taotao.cloud.rpc.client.client.proxy;

import com.taotao.cloud.rpc.client.client.support.fail.enums.FailTypeEnum;
import com.taotao.cloud.rpc.client.client.support.register.ClientRegisterManager;

import com.taotao.cloud.rpc.client.client.support.filter.RpcFilter;

/**
 * 反射调用上下文
 * @author shuigedeng
 * @since 0.0.6
 * @see ReferenceConfig 对这里的信息进行一次转换。
 */
public interface ServiceContext<T> {

    /**
     * 服务唯一标识
     * @since 0.0.6
     * @return 服务唯一标识
     */
    String serviceId();

    /**
     * 服务接口
     * @since 0.0.6
     * @return 服务接口
     */
    Class<T> serviceInterface();

    /**
     * 调用服务
     * @return 调用服务
     * @since 0.0.6
     */
    InvokeManager invokeManager();

    /**
     * 超时时间
     * 单位：mills
     * @return 超时时间
     * @since 0.0.7
     */
    long timeout();

    /**
     * 调用方式
     * @return 枚举值
     * @since 0.1.0
     */
    CallTypeEnum callType();

    /**
     * 失败策略
     * @return 失败策略枚举
     * @since 0.1.1
     */
    FailTypeEnum failType();

    /**
     * 是否进行泛化调用
     * @return 是否
     * @since 0.1.2
     */
    boolean generic();

    /**
     * 状态管理类
     * @return 状态管理类
     * @since 0.1.3
     */
    StatusManager statusManager();

    /**
     * 拦截器
     * @return 拦截器
     * @since 0.1.4
     */
    RpcInterceptor interceptor();

    /**
     * 客户端注册中心管理类
     * @return 结果
     * @since 0.1.8
     */
    ClientRegisterManager clientRegisterManager();

    /**
     * rpc 过滤器
     * @since 0.2.0
     * @return 过滤器
     */
    RpcFilter rpcFilter();

    /**
     * 负载均衡策略
     * @return 策略
     * @since 0.2.0
     */
    ILoadBalance loadBalance();

}
