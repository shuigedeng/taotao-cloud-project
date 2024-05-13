/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.core;

import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.rpc.common.remote.netty.handler.ChannelHandlers;
import com.github.houbb.rpc.common.remote.netty.impl.DefaultNettyServer;
import com.github.houbb.rpc.register.api.config.RegisterConfig;
import com.github.houbb.rpc.register.simple.client.RegisterClientService;
import com.github.houbb.rpc.register.simple.client.impl.DefaultRegisterClientService;
import com.github.houbb.rpc.register.simple.handler.RegisterCenterServerHandler;

import com.github.houbb.rpc.register.simple.server.RegisterServerService;
import com.github.houbb.rpc.register.simple.server.impl.DefaultRegisterServerService;
import com.github.houbb.rpc.register.support.hook.RegisterCenterShutdownHook;
import io.netty.channel.ChannelHandler;

/**
 * 默认注册中心配置
 * @author shuigedeng
 * @since 0.0.8
 */
public class RegisterBs implements RegisterConfig {

    /**
     * 服务启动端口信息
     * @since 0.0.8
     */
    private int port;

    /**
     * 服务端
     */
    private final RegisterServerService registerServerService;
    /**
     * 客户端
     */
    private final RegisterClientService registerClientService;

    private RegisterBs(){
        registerServerService = new DefaultRegisterServerService();
        registerClientService = new DefaultRegisterClientService();
    }

    public static RegisterBs newInstance() {
        RegisterBs registerBs = new RegisterBs();
        registerBs.port(8527);
        return registerBs;
    }

    @Override
    public RegisterBs port(int port) {
        ArgUtil.notNegative(port, "port");

        this.port = port;
        return this;
    }

    @Override
    public RegisterBs start() {
        ChannelHandler channelHandler = ChannelHandlers.objectCodecHandler(new RegisterCenterServerHandler());
        DefaultNettyServer.newInstance(port, channelHandler).asyncRun();

        // 通知对应的服务端和客户端，服务启动。
        // 新增的时候暂时不处理。
        // 暂时注册中心的是无状态的，无法获取到没有访问过的节点。（如果访问过，则客户端肯定已经有对应的信息。）
        // 如果使用 redis/database 等集中式的存储，或者进行数据同步，则有通知的必要性。

        // 添加对应的 shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                new RegisterCenterShutdownHook(registerServerService, registerClientService, port).hook();
            }
        });

        return this;
    }

}
