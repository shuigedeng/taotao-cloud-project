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

package com.taotao.cloud.rpc.server.config.service;

/**
 * 单个服务配置类
 *
 * 简化用户使用：
 * 在用户使用的时候，这个类应该是不可见的。
 * 直接提供对应的服务注册类即可。
 *
 * 后续拓展
 * （1）版本信息
 * （2）服务端超时时间
 *
 * @author shuigedeng
 * @since 2024.06
 * @param <T> 实现类泛型
 */
public interface ServiceConfig<T> {

    /**
     * 获取唯一标识
     * @return 获取唯一标识
     * @since 2024.06
     */
    String id();

    /**
     * 设置唯一标识
     * @param id 标识信息
     * @return this
     * @since 2024.06
     */
    ServiceConfig<T> id(String id);

    /**
     * 获取引用实体实现
     * @return 实体实现
     * @since 2024.06
     */
    T reference();

    /**
     * 设置引用实体实现
     * @param reference 引用实现
     * @return this
     * @since 2024.06
     */
    ServiceConfig<T> reference(T reference);

    /**
     * 设置是否注册到注册中心
     * @param register 是否注册到配置中心
     * @since 2024.06
     * @return this
     */
    ServiceConfig<T> register(final boolean register);

    /**
     * 返回是否注册到注册中心
     * @since 2024.06
     * @return 是否进行注册到注册中心
     */
    boolean register();

    /**
     * 设置服务延迟暴露时间
     * @param delayInMills 延迟暴露的毫秒数
     * @since 0.1.7
     * @return this
     */
    ServiceConfig<T> delay(final long delayInMills);

    /**
     * 获取延迟暴露毫秒数
     * @return 暴露毫秒数
     * @since 0.1.7
     */
    long delay();
}
