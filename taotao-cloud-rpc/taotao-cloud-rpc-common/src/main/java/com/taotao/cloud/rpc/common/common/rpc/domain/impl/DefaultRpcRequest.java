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

import com.taotao.cloud.rpc.common.common.constant.enums.CallTypeEnum;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 默认 rpc 入参
 * @author shuigedeng
 * @since 2024.06
 * @see Class#getMethod(String, Class[]) 反射获取方法信息
 * @see java.lang.reflect.Method#invoke(Object, Object...) 方法反射调用
 */
public class DefaultRpcRequest implements RpcRequest {

    private static final long serialVersionUID = 4284511516221766313L;

    /**
     * 唯一标识号
     * （）
     * @since 2024.06
     */
    private String seqId;

    /**
     * 创建时间
     * @since 2024.06
     * @see System#currentTimeMillis() 当前时间
     */
    private long createTime;

    /**
     * 服务名称唯一标识
     * @since 2024.06
     */
    private String serviceId;

    /**
     * 方法名称
     * @since 2024.06
     */
    private String methodName;

    /**
     * 参数类型名称列表
     * @since 2024.06
     */
    private List<String> paramTypeNames;

    /**
     * 参数值
     * @since 2024.06
     */
    private Object[] paramValues;

    /**
     * 返回值类型
     * @since 0.1.0
     */
    private Class returnType;

    /**
     * 超时时间
     * @since 0.1.3
     */
    private long timeout;

    /**
     * 调用方式
     * @since 0.1.3
     */
    private CallTypeEnum callType;

    @Override
    public String seqId() {
        return seqId;
    }

    @Override
    public DefaultRpcRequest seqId(String seqId) {
        this.seqId = seqId;
        return this;
    }

    @Override
    public long createTime() {
        return createTime;
    }

    public DefaultRpcRequest createTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    public String serviceId() {
        return serviceId;
    }

    public DefaultRpcRequest serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    @Override
    public String methodName() {
        return methodName;
    }

    public DefaultRpcRequest methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @Override
    public List<String> paramTypeNames() {
        return paramTypeNames;
    }

    public DefaultRpcRequest paramTypeNames(List<String> paramTypeNames) {
        this.paramTypeNames = paramTypeNames;
        return this;
    }

    @Override
    public Object[] paramValues() {
        return paramValues;
    }

    public DefaultRpcRequest paramValues(Object[] paramValues) {
        this.paramValues = paramValues;
        return this;
    }

    @Override
    public Class returnType() {
        return returnType;
    }

    public DefaultRpcRequest returnType(Class returnType) {
        this.returnType = returnType;
        return this;
    }

    @Override
    public long timeout() {
        return timeout;
    }

    public DefaultRpcRequest timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public CallTypeEnum callType() {
        return callType;
    }

    public DefaultRpcRequest callType(CallTypeEnum callType) {
        this.callType = callType;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultRpcRequest{"
                + "seqId='"
                + seqId
                + '\''
                + ", createTime="
                + createTime
                + ", serviceId='"
                + serviceId
                + '\''
                + ", methodName='"
                + methodName
                + '\''
                + ", paramTypeNames="
                + paramTypeNames
                + ", paramValues="
                + Arrays.toString(paramValues)
                + ", returnType="
                + returnType
                + ", timeout="
                + timeout
                + ", callType="
                + callType
                + '}';
    }
}
