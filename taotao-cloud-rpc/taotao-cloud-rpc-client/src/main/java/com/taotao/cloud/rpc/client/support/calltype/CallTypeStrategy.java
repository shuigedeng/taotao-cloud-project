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

package com.taotao.cloud.rpc.client.support.calltype;

import com.taotao.cloud.rpc.client.proxy.ServiceContext;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcRequest;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;

/**
 * 调用方式上下文
 * @author shuigedeng
 * @since 0.1.0
 */
public interface CallTypeStrategy {

    /**
     * 获取结果
     * @param proxyContext 代理上下文
     * @param rpcRequest 请求信息
     * @return 结果
     * @since 0.1.0
     */
    RpcResponse result(final ServiceContext proxyContext, final RpcRequest rpcRequest);
}
