/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.rpc.registry.support.hook; // package
                                                    // com.taotao.cloud.rpc.registry.register.support.hook;
//
// import com.taotao.cloud.rpc.registry.domain.message.NotifyMessage;
// import com.taotao.cloud.rpc.registry.domain.message.body.RegisterCenterAddNotifyBody;
// import com.taotao.cloud.rpc.registry.domain.message.impl.NotifyMessages;
// import com.taotao.cloud.rpc.registry.simple.client.RegisterClientService;
// import com.taotao.cloud.rpc.registry.simple.constant.MessageTypeConst;
// import com.taotao.cloud.rpc.registry.simple.server.RegisterServerService;
// import io.netty.channel.Channel;
//
// import java.util.Collection;
//
/// **
// * 注册中心 shutdown
// * @since 0.1.8
// */
// public class RegisterCenterShutdownHook extends AbstractShutdownHook {
//
//    /**
//     * 服务端
//     */
//    private final RegisterServerService registerServerService;
//    /**
//     * 客户端
//     */
//    private final RegisterClientService registerClientService;
//
//    /**
//     * 端口号
//     */
//    private final int port;
//
//    public RegisterCenterShutdownHook(RegisterServerService registerServerService,
//                                      RegisterClientService registerClientService,
//                                      int port) {
//        this.registerServerService = registerServerService;
//        this.registerClientService = registerClientService;
//        this.port = port;
//    }
//
//    @Override
//    protected void doHook() {
//        String ip = NetUtil.getLocalIp();
//        RegisterCenterAddNotifyBody addNotifyBody = new RegisterCenterAddNotifyBody();
//        addNotifyBody.ip(ip);
//        addNotifyBody.port(port);
//        NotifyMessage notifyMessage =
// NotifyMessages.of(MessageTypeConst.REGISTER_CENTER_REMOVE_NOTIFY, addNotifyBody);
//
//        //1. 通知所有的服务端
//        //TODO: 这些 channel 应该进行一次封装，保留原始的 ip:port 信息
//        Collection<Channel> serverList = registerServerService.channels();
//        for(Channel channel : serverList) {
//            channel.writeAndFlush(notifyMessage);
//        }
//
//        //2. 通知所有的客户端
//        Collection<Channel> clientList = registerClientService.channels();
//        for(Channel channel : clientList) {
//            channel.writeAndFlush(notifyMessage);
//        }
//    }
//
// }
