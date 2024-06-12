
package com.taotao.cloud.rpc.client.client.support.register;

import com.taotao.cloud.rpc.client.client.model.ClientQueryServerChannelConfig;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcChannelFuture;
import com.taotao.cloud.rpc.registry.register.domain.entry.ServiceEntry;
import com.taotao.cloud.rpc.registry.register.domain.message.body.RegisterCenterAddNotifyBody;
import com.taotao.cloud.rpc.registry.register.domain.message.body.RegisterCenterRemoveNotifyBody;
import io.netty.channel.Channel;

import java.util.List;

/**
 * <p> 客户端注册中心服务接口 </p>
 * @since 2024.06
 */
public interface ClientRegisterManager {

    /**
     * 初始化查询服务端列表
     *
     * 使用场景：第一次查询的时候使用
     *
     * @param config 查询配置信息
     * @since 0.1.8
     */
    void initServerChannelFutureList(ClientQueryServerChannelConfig config);

    /**
     * 查询服务端对应的列表
     * @param serviceId 服务标识
     * @return 结果
     * @since 0.1.8
     */
    List<RpcChannelFuture> queryServerChannelFutures(final String serviceId);

    /**
     * 取消订阅所有服务端信息
     * @since 0.1.8
     */
    void unSubscribeServerAll();

    /**
     * 订阅指定服务端信息
     * @param serviceId 服务标识
     * @since 0.1.8
     */
    void subscribeServer(final String serviceId);

    /**
     * 服务端注册通知
     * @param serviceEntry 服务端信息
     * @since 0.1.8
     */
    void serverRegisterNotify(ServiceEntry serviceEntry);

    /**
     * 服务端取消注册通知
     * @param serviceEntry 服务端信息
     * @since 0.1.8
     */
    void serverUnRegisterNotify(ServiceEntry serviceEntry);

    /**
     * 添加注册中心的 channel
     * @param body 对象
     * @param channel channel
     * @since 0.1.8
     */
    void addRegisterChannel(RegisterCenterAddNotifyBody body, Channel channel);

    /**
     * 移除注册中心的 channel
     * @param body 地址
     * @since 0.1.8
     */
    void removeRegisterChannel(RegisterCenterRemoveNotifyBody body);

}
