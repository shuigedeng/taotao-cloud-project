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

package com.taotao.cloud.rpc.client.support.fail.impl;

import com.taotao.cloud.rpc.client.proxy.RemoteInvokeContext;
import com.taotao.cloud.rpc.client.support.fail.FailStrategy;
import com.taotao.cloud.rpc.common.common.rpc.domain.impl.RpcResponses;
import javax.annotation.concurrent.ThreadSafe;

/**
 * 快速失败策略
 * @author shuigedeng
 * @since 0.1.1
 */
@ThreadSafe
class FailFastStrategy implements FailStrategy {

    @Override
    public Object fail(final RemoteInvokeContext context) {
        final Class returnType = context.request().returnType();
        return RpcResponses.getResult(context.rpcResponse(), returnType);
    }
}
