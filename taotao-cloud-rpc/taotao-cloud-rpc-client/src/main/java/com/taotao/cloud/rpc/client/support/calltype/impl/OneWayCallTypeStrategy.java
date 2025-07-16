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

package com.taotao.cloud.rpc.client.support.calltype.impl;

import com.taotao.cloud.rpc.client.proxy.ServiceContext;
import com.taotao.cloud.rpc.client.support.calltype.CallTypeStrategy;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcRequest;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
import com.taotao.cloud.rpc.common.common.rpc.domain.impl.RpcResponses;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * one way 调用服务实现类
 *
 * @author shuigedeng
 * @since 0.1.0
 */
@ThreadSafe
class OneWayCallTypeStrategy implements CallTypeStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(OneWayCallTypeStrategy.class);

    /**
     * 实例
     *
     * @since 0.1.0
     */
    private static final CallTypeStrategy INSTANCE = new OneWayCallTypeStrategy();

    /**
     * 获取实例
     *
     * @since 0.1.0
     */
    static CallTypeStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public RpcResponse result(ServiceContext proxyContext, RpcRequest rpcRequest) {
        final String seqId = rpcRequest.seqId();

        // 结果可以不是简单的 null，而是根据 result 类型处理，避免基本类型NPE。
        RpcResponse rpcResponse = RpcResponses.result(null, rpcRequest.returnType());
        //		LOG.info("[Client] call type is one way, seqId: {} set response to {}", seqId,
        // rpcResponse);

        // 获取结果
        return rpcResponse;
    }
}
