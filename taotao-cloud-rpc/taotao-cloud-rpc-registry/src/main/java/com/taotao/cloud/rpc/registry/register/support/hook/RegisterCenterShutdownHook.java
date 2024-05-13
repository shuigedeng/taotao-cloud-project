package com.github.houbb.rpc.register.support.hook;

import com.github.houbb.heaven.util.net.NetUtil;
import com.github.houbb.rpc.common.support.hook.AbstractShutdownHook;
import com.github.houbb.rpc.register.domain.message.NotifyMessage;
import com.github.houbb.rpc.register.domain.message.body.RegisterCenterAddNotifyBody;
import com.github.houbb.rpc.register.domain.message.impl.NotifyMessages;
import com.github.houbb.rpc.register.simple.client.RegisterClientService;
import com.github.houbb.rpc.register.simple.constant.MessageTypeConst;
import com.github.houbb.rpc.register.simple.server.RegisterServerService;
import io.netty.channel.Channel;

import java.util.Collection;

/**
 * 注册中心 shutdown
 * @since 0.1.8
 */
public class RegisterCenterShutdownHook extends AbstractShutdownHook {

    /**
     * 服务端
     */
    private final RegisterServerService registerServerService;
    /**
     * 客户端
     */
    private final RegisterClientService registerClientService;

    /**
     * 端口号
     */
    private final int port;

    public RegisterCenterShutdownHook(RegisterServerService registerServerService,
                                      RegisterClientService registerClientService,
                                      int port) {
        this.registerServerService = registerServerService;
        this.registerClientService = registerClientService;
        this.port = port;
    }

    @Override
    protected void doHook() {
        String ip = NetUtil.getLocalIp();
        RegisterCenterAddNotifyBody addNotifyBody = new RegisterCenterAddNotifyBody();
        addNotifyBody.ip(ip);
        addNotifyBody.port(port);
        NotifyMessage notifyMessage = NotifyMessages.of(MessageTypeConst.REGISTER_CENTER_REMOVE_NOTIFY, addNotifyBody);

        //1. 通知所有的服务端
        //TODO: 这些 channel 应该进行一次封装，保留原始的 ip:port 信息
        Collection<Channel> serverList = registerServerService.channels();
        for(Channel channel : serverList) {
            channel.writeAndFlush(notifyMessage);
        }

        //2. 通知所有的客户端
        Collection<Channel> clientList = registerClientService.channels();
        for(Channel channel : clientList) {
            channel.writeAndFlush(notifyMessage);
        }
    }

}
