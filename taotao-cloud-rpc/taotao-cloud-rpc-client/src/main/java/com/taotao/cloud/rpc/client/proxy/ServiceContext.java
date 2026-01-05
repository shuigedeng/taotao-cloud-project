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

package com.taotao.cloud.rpc.client.proxy;

import com.taotao.cloud.rpc.client.config.reference.ReferenceConfig;
import com.taotao.cloud.rpc.client.support.fail.enums.FailTypeEnum;
import com.taotao.cloud.rpc.client.support.filter.RpcFilter;
import com.taotao.cloud.rpc.client.support.register.ClientRegisterManager;
import com.taotao.cloud.rpc.common.common.constant.enums.CallTypeEnum;
import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptor;
import com.taotao.cloud.rpc.common.common.support.invoke.InvokeManager;
import com.taotao.cloud.rpc.common.common.support.status.service.StatusManager;
import com.taotao.cloud.rpc.common.tmp.LoadBalance;

/**
 * 反射调用上下文
 *
 * @author shuigedeng
 * @see ReferenceConfig 对这里的信息进行一次转换。
 * @since 2024.06
 */
public interface ServiceContext<T> {

    /**
     * 服务唯一标识
     *
     * @return 服务唯一标识
     * @since 2024.06
     */
    String serviceId();

    /**
     * 服务接口
     *
     * @return 服务接口
     * @since 2024.06
     */
    Class<T> serviceInterface();

    /**
     * 调用服务
     *
     * @return 调用服务
     * @since 2024.06
     */
    InvokeManager invokeManager();

    /**
     * 超时时间 单位：mills
     *
     * @return 超时时间
     * @since 2024.06
     */
    long timeout();

    /**
     * 调用方式
     *
     * @return 枚举值
     * @since 0.1.0
     */
    CallTypeEnum callType();

    /**
     * 失败策略
     *
     * @return 失败策略枚举
     * @since 0.1.1
     */
    FailTypeEnum failType();

    /**
     * 是否进行泛化调用
     *
     * @return 是否
     * @since 0.1.2
     */
    boolean generic();

    /**
     * 状态管理类
     *
     * @return 状态管理类
     * @since 0.1.3
     */
    StatusManager statusManager();

    /**
     * 拦截器
     *
     * @return 拦截器
     * @since 0.1.4
     */
    RpcInterceptor interceptor();

    /**
     * 客户端注册中心管理类
     *
     * @return 结果
     * @since 0.1.8
     */
    ClientRegisterManager clientRegisterManager();

    /**
     * rpc 过滤器
     *
     * @return 过滤器
     * @since 0.2.0
     */
    RpcFilter rpcFilter();

    /**
     * 负载均衡策略
     *
     * @return 策略
     * @since 0.2.0
     */
    LoadBalance loadBalance();
}
