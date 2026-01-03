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

package com.taotao.cloud.rpc.common.protocol;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 请求体
 */
public class RpcRequest implements Serializable {

    /**
     * 请求号，主要用来做 多 客户端 请求包 和 响应包的 过滤
     */
    private String requestId;

    // 调用方法 所属的 接口名
    private String interfaceName;
    // 调用方法名
    private String methodName;
    // 调用方法的参数
    private Object[] parameters;
    // 调用方法的 参数类型
    private Class<?>[] paramTypes;
    // 调用方法的 返回值类型
    private Class<?> returnType;

    private String group;

    /**
     * 指定 是否为 心跳包，区分数据包和心跳包的关键，需要配合 IdleStateHandler 使用 IdleStateHandler 作用 做 读空闲检测、写检测、读写检测 客户端 设置 写 状态超时时，超时时间内 未 向
     * 服务端 写操作，即 触发 读超时时间 理解：表示 客户端 虽然 没有 向 服务端 写 数据了，但 还是 要向 服务端 “表示我还活着” -> 发送 心跳包 注意：这里 JSON 对 boolean 反序列 有时会出现
     * 异常（Reference Chain），最好 使用 引用类型的 Boolean，推荐 KRYO
     */
    private Boolean heartBeat = false;

    /**
     * 没有空 构造方法 会导致 反序列化 失败 Exception: no delegate- or property-based Creator
     */
    public RpcRequest() {
        super();
    }

    public RpcRequest( Builder builder ) {
        this.requestId = builder.requestId;
        this.interfaceName = builder.interfaceName;
        this.methodName = builder.methodName;
        this.parameters = builder.parameters;
        this.paramTypes = builder.paramTypes;
        this.returnType = builder.returnType;
        this.heartBeat = builder.heartBeat;
    }

    /**
     * Builder
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
    public static final class Builder {

        private String requestId;
        private String interfaceName;
        private String methodName;
        private Object[] parameters;
        private Class<?>[] paramTypes;
        private Class<?> returnType;
        private Boolean heartBeat;

        public Builder requestId( String requestId ) {
            this.requestId = requestId;
            return this;
        }

        public Builder interfaceName( String interfaceName ) {
            this.interfaceName = interfaceName;
            return this;
        }

        public Builder methodName( String methodName ) {
            this.methodName = methodName;
            return this;
        }

        public Builder parameters( Object[] parameters ) {
            this.parameters = parameters;
            return this;
        }

        public Builder paramTypes( Class<?>[] paramTypes ) {
            this.paramTypes = paramTypes;
            return this;
        }

        public Builder returnType( Class<?> returnType ) {
            this.returnType = returnType;
            return this;
        }

        public Builder heartBeat( Boolean heartBeat ) {
            this.heartBeat = heartBeat;
            return this;
        }

        public RpcRequest build() {
            return new RpcRequest(this);
        }
    }

    public String getRequestId() {
        return requestId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName( String interfaceName ) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType( Class<?> returnType ) {
        this.returnType = returnType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup( String group ) {
        this.group = group;
    }

    public Boolean getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat( Boolean heartBeat ) {
        this.heartBeat = heartBeat;
    }

    @Override
    public String toString() {
        return "RpcRequest{"
                + "requestId='"
                + requestId
                + '\''
                + ", interfaceName='"
                + interfaceName
                + '\''
                + ", methodName='"
                + methodName
                + '\''
                + ", parameters="
                + Arrays.toString(parameters)
                + ", paramTypes="
                + Arrays.toString(paramTypes)
                + ", group='"
                + group
                + '\''
                + ", heartBeat="
                + heartBeat
                + '}';
    }
}
