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

package com.taotao.cloud.rpc.common.common.rpc.domain.impl;

import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;

/**
 * 默认 rpc 响应结果
 * @author shuigedeng
 * @since 2024.06
 */
public final class RpcResponses {

    private RpcResponses() {}

    /**
     * 设置结果
     * @param object 结果
     * @param tClass 结果类型
     * @return 结果
     * @since 0.1.0
     */
    public static RpcResponse result(final Object object, final Class tClass) {
        DefaultRpcResponse rpcResponse = new DefaultRpcResponse();
        //        if(ObjectUtil.isNotNull(object)) {
        //            rpcResponse.result(object);
        //        } else {
        //            // 处理基本类型的默认值，避免 NPE
        //            Object defaultVal = PrimitiveUtil.getDefaultValue(tClass);
        //            rpcResponse.result(defaultVal);
        //        }
        return rpcResponse;
    }

    /**
     * 获取结果
     * @param rpcResponse 响应
     * @param returnType 返回值类型
     * @return 结果
     * 如果有异常，则直接抛出异常信息。
     * @since 0.1.1
     */
    public static Object getResult(final RpcResponse rpcResponse, final Class returnType) {
        //        if(ObjectUtil.isNull(rpcResponse)) {
        //            // 根据返回类型处理
        //            return PrimitiveUtil.getDefaultValue(returnType);
        //        }
        //
        //        // 处理异常信息
        //        Throwable throwable = rpcResponse.error();
        //        if(ObjectUtil.isNotNull(throwable)) {
        //            throw new RpcRuntimeException(throwable);
        //        }
        //
        //        // 处理结果信息
        //        Object result = rpcResponse.result();
        //        if(ObjectUtil.isNotNull(result)) {
        //            return result;
        //        }
        //        return PrimitiveUtil.getDefaultValue(returnType);
        return null;
    }

    /**
     * 获取结果
     * @param rpcResponse 响应
     * @return 结果
     * 如果有异常，则直接抛出异常信息。
     * @since 2024.06
     */
    public static Object getResult(final RpcResponse rpcResponse) {
        return getResult(rpcResponse, Object.class);
    }
}
