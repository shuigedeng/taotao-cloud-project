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

package com.taotao.cloud.mq.common.support.invoke;

import com.taotao.cloud.mq.common.rpc.RpcMessageDto;

/**
 * 调用服务接口
 *
 * @author shuigedeng
 * @since 2024.05
 */
public interface IInvokeService {

    /**
     * 添加请求信息
     *
     * @param seqId        序列号
     * @param timeoutMills 超时时间
     * @return this
     * @since 2024.05
     */
    IInvokeService addRequest(final String seqId, final long timeoutMills);

    /**
     * 放入结果
     *
     * @param seqId       唯一标识
     * @param rpcResponse 响应结果
     * @return this
     * @since 2024.05
     */
    IInvokeService addResponse(final String seqId, final RpcMessageDto rpcResponse);

    /**
     * 获取标志信息对应的结果
     *
     * @param seqId 序列号
     * @return 结果
     * @since 2024.05
     */
    RpcMessageDto getResponse(final String seqId);

    /**
     * 是否依然包含请求待处理
     *
     * @return 是否
     * @since 2024.05
     */
    boolean remainsRequest();
}
