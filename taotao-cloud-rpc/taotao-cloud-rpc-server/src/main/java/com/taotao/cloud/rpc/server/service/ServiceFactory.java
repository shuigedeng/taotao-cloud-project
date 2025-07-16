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

package com.taotao.cloud.rpc.server.service;

import com.taotao.cloud.rpc.server.config.service.ServiceConfig;
import com.taotao.cloud.rpc.server.registry.ServiceRegistry;
import java.util.List;

/**
 * 服务方法类仓库管理类-接口
 *
 *
 * （1）对外暴露的方法，应该尽可能的少。
 * （2）对于外部的调用，后期比如 telnet 治理，可以使用比如有哪些服务列表？
 * 单个服务有哪些方法名称？
 *
 * 等等基础信息的查询，本期暂时全部隐藏掉。
 *
 * （3）前期尽可能的少暴露方法。
 * @author shuigedeng
 * @since 2024.06
 * @see ServiceRegistry 服务注册，将服务信息放在这个类中，进行统一的管理。
 * @see ServiceMethod 方法信息
 */
public interface ServiceFactory {

    /**
     * 注册服务列表信息到本地
     * @param serviceConfigList 服务配置列表
     * @return this
     * @since 2024.06
     */
    ServiceFactory registerServicesLocal(final List<ServiceConfig> serviceConfigList);

    /**
     * 直接反射调用
     * （1）此处对于方法反射，为了提升性能，所有的 class.getFullName() 进行拼接然后放进 key 中。
     *
     * @param serviceId 服务名称
     * @param methodName 方法名称
     * @param paramTypeNames 参数类型名称列表
     * @param paramValues 参数值
     * @return 方法调用返回值
     * @since 2024.06
     */
    Object invoke(
            final String serviceId,
            final String methodName,
            List<String> paramTypeNames,
            final Object[] paramValues);
}
