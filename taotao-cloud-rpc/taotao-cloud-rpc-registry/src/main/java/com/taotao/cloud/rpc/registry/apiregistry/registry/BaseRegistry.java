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

package com.taotao.cloud.rpc.registry.apiregistry.registry;

import java.util.List;
import java.util.Map;

/**
 * 注册中心实现
 */
public class BaseRegistry implements AutoCloseable {
    public BaseRegistry() {
        // ThreadUtils.shutdown(()->{
        //     try {
        //         this.close();
        //     }catch (Exception e){}
        // },1,false);
    }

    public void register() {}

    /**获取服务列表*/
    public Map<String, List<String>> getServerList() {
        return null;
    }

    /**获取报表信息*/
    public String getReport() {
        return "";
    }

    @Override
    public void close() {}
}
