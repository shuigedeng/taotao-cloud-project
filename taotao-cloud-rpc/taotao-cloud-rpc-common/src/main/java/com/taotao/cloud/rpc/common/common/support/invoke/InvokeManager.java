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

package com.taotao.cloud.rpc.common.common.support.invoke;

import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;

/**
 * 调用服务接口
 * @author shuigedeng
 * @since 2024.06
 */
public interface InvokeManager {

    /**
     * 添加请求信息
     * @param seqId 序列号
     * @param timeoutMills 超时时间
     * @return this
     * @since 2024.06
     */
    InvokeManager addRequest(final String seqId, final long timeoutMills);

    /**
     * 放入结果
     * @param seqId 唯一标识
     * @param rpcResponse 响应结果
     * @return this
     * @since 2024.06
     */
    InvokeManager addResponse(final String seqId, final RpcResponse rpcResponse);

    /**
     * 获取标志信息对应的结果
     * （1）需要移除对应的结果信息
     * （2）需要移除对应的请求信息
     *
     * 将移除统一放在这里，实际上也有点不太合理。
     * 但是此处考虑到写入的时间较短，可以忽略不计。
     * 如果非要考虑非常细致，可以将移除的地方，单独暴露一个方法，但是个人觉得没有必要。
     * @param seqId 序列号
     * @return 结果
     * @since 2024.06
     */
    RpcResponse getResponse(final String seqId);

    /**
     * 是否依然包含请求待处理
     * @return 是否
     * @since 0.1.3
     */
    boolean remainsRequest();

    /**
     * 移除请求和响应
     * @param seqId 唯一标识
     * @return this
     * @since 0.1.3
     */
    InvokeManager removeReqAndResp(final String seqId);
}
