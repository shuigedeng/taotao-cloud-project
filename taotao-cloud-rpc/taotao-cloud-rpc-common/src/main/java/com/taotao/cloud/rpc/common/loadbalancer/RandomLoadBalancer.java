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
import com.taotao.cloud.rpc.common.exception.ServiceNotFoundException;
import java.util.List;
import java.util.Random;

/**
 * 随机选择
 */
public class RandomLoadBalancer implements LoadBalancer {

    @Override
    public Instance selectService(List<Instance> instances) throws RpcException {
        if (instances.size() == 0) {
            throw new ServiceNotFoundException(
                    "service instances size is zero, can't provide service! please start server first!");
        }
        return instances.get(new Random().nextInt(instances.size()));
    }

    @Override
    public String selectNode(String[] nodes) throws RpcException {
        if (nodes.length == 0) {
            throw new ServiceNotFoundException(
                    "service instances size is zero, can't provide service! please start server first!");
        }
        return nodes[new Random().nextInt(nodes.length)];
    }
}
