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
public class DefaultServiceConfig<T> implements ServiceConfig<T> {

    /**
     * 服务的唯一标识
     * @since 2024.06
     */
    private String id;

    /**
     * 设置引用类
     * @since 2024.06
     */
    private T reference;

    /**
     * 是否注册到配置中心
     * 默认：进行注册，如果注册中心可用的话。
     * @since 2024.06
     */
    private boolean register = true;

    /**
     * 延迟暴露的毫秒数
     *
     * 默认不做延迟
     * @since 0.1.7
     */
    private long delay = 0;

    @Override
    public String id() {
        return id;
    }

    @Override
    public DefaultServiceConfig<T> id(String id) {
        this.id = id;
        return this;
    }

    @Override
    public T reference() {
        return reference;
    }

    @Override
    public DefaultServiceConfig<T> reference(T reference) {
        this.reference = reference;
        return this;
    }

    @Override
    public boolean register() {
        return register;
    }

    @Override
    public DefaultServiceConfig<T> register(boolean register) {
        this.register = register;
        return this;
    }

    @Override
    public long delay() {
        return delay;
    }

    @Override
    public DefaultServiceConfig<T> delay(long delay) {
        this.delay = delay;
        return this;
    }
}
