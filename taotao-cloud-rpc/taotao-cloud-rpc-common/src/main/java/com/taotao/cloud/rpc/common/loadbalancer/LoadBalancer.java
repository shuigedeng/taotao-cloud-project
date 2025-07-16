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

package com.taotao.cloud.rpc.common.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.taotao.cloud.rpc.common.exception.RpcException;
import java.util.List;

/**
 * 负载均衡 接口
 */
public interface LoadBalancer {

    Instance selectService(List<Instance> instances) throws RpcException;

    String selectNode(String[] nodes) throws RpcException;

    static LoadBalancer getByCode(int code) {
        switch (code) {
            case 0:
                return new RandomLoadBalancer();
            case 1:
                return new RoundRobinLoadBalancer();
            default:
                return null;
        }
    }
}
