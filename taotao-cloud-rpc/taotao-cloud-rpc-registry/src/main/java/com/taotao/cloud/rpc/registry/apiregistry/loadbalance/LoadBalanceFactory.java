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

package com.taotao.cloud.rpc.registry.apiregistry.loadbalance;

import com.taotao.cloud.rpc.registry.apiregistry.base.ApiRegistryException;

public class LoadBalanceFactory {
    public static BaseLoadBalance create() {
        //
        // if(RoundRobinLoadBalance.class.getSimpleName().equalsIgnoreCase(ApiRegistryProperties.getRegistryLoadBalanceType())){
        //            return new RoundRobinLoadBalance();
        //        }
        throw new ApiRegistryException("请配置ttc.apiRegistry.loadBalance.type");
    }
}
