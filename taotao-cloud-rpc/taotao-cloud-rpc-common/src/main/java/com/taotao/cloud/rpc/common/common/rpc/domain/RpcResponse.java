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

/**
 * 序列化相关处理
 * @author shuigedeng
 * @since 2024.06
 */
public interface RpcResponse extends BaseRpc {

    /**
     * 异常信息
     * @return 异常信息
     * @since 2024.06
     */
    Throwable error();

    /**
     * 请求结果
     * @return 请求结果
     * @since 2024.06
     */
    Object result();
}
