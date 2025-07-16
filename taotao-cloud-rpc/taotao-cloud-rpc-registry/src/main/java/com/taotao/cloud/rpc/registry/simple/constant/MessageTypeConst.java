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

package com.taotao.cloud.rpc.registry.simple.constant;

/**
 * 注册消息枚举
 *
 * （1）后期还可以添加心跳+重连机制。
 *
 * @author shuigedeng
 * @since 2024.06
 */
public final class MessageTypeConst {

    private MessageTypeConst() {}

    /**
     * 服务端注册请求
     * @since 2024.06
     */
    public static final String SERVER_REGISTER_REQ = "SERVER_REGISTER_REQ";

    /**
     * 服务端注销请求
     * @since 2024.06
     */
    public static final String SERVER_UN_REGISTER_REQ = "SERVER_UN_REGISTER_REQ";

    /**
     * 客户端订阅请求
     * @since 2024.06
     */
    public static final String CLIENT_SUBSCRIBE_REQ = "CLIENT_SUBSCRIBE_REQ";

    /**
     * 客户端取关请求
     * @since 2024.06
     */
    public static final String CLIENT_UN_SUBSCRIBE_REQ = "CLIENT_UN_SUBSCRIBE_REQ";

    /**
     * 客户端查询
     * @since 2024.06
     */
    public static final String CLIENT_LOOK_UP_SERVER_REQ = "CLIENT_LOOK_UP_SERVER_REQ";

    /**
     * 客户端查询服务接口
     * @since 2024.06
     */
    public static final String CLIENT_LOOK_UP_SERVER_RESP = "CLIENT_LOOK_UP_SERVER_RESP";

    /**
     * 服务端注册通知客户端请求
     * @since 2024.06
     */
    public static final String SERVER_REGISTER_NOTIFY_CLIENT_REQ =
            "SERVER_REGISTER_NOTIFY_CLIENT_REQ";

    /**
     * 服务端取消注册通知客户端请求
     * @since 2024.06
     */
    public static final String SERVER_UNREGISTER_NOTIFY_CLIENT_REQ =
            "SERVER_UNREGISTER_NOTIFY_CLIENT_REQ";

    /**
     * 注册中心新增通知
     * @since 0.1.8
     */
    public static final String REGISTER_CENTER_ADD_NOTIFY = "REGISTER_CENTER_ADD_NOTIFY";

    /**
     * 注册中心销毁通知
     * @since 0.1.8
     */
    public static final String REGISTER_CENTER_REMOVE_NOTIFY = "REGISTER_CENTER_REMOVE_NOTIFY";

    /**
     * 服务端心跳
     * @since 0.2.0
     */
    public static final String SERVER_HEARTBEAT_REQ = "SERVER_HEARTBEAT_REQ";
}
