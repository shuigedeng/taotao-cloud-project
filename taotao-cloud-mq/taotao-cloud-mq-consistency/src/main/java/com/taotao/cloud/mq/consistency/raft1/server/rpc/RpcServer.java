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

package com.taotao.cloud.mq.consistency.raft1.server.rpc;

import com.taotao.cloud.mq.consistency.raft1.common.core.LifeCycle;
import com.taotao.cloud.mq.consistency.raft1.common.rpc.RpcRequest;
import com.taotao.cloud.mq.consistency.raft1.common.rpc.RpcResponse;

/**
 * rpc 服务端
 *
 * @author shuigedeng
 */
public interface RpcServer extends LifeCycle {

    /**
     * 处理请求.
     * @param request 请求参数.
     * @return 返回值.
     */
    RpcResponse<?> handlerRequest(RpcRequest request);
}
