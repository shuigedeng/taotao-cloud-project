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

package com.taotao.cloud.rpc.common.common.rpc.domain;

import java.io.Serializable;

/**
 * 序列化相关处理
 * @author shuigedeng
 * @since 2024.06
 */
public interface BaseRpc extends Serializable {

    /**
     * 获取唯一标识号
     * （1）用来唯一标识一次调用，便于获取该调用对应的响应信息。
     * @return 唯一标识号
     */
    String seqId();

    /**
     * 设置唯一标识号
     * @param traceId 唯一标识号
     * @return this
     */
    BaseRpc seqId(final String traceId);
}
