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

package com.taotao.cloud.rpc.common.common.config.application;

/**
 * 应用配置信息
 * （1）服务的应用应该是一个单例。
 * （2）对于用户可以不可见，直接根据 rpc.properties 设置。
 * @author shuigedeng
 * @since 2024.06
 */
public interface ApplicationConfig {

    /**
     * @return 应用名称
     * @since 2024.06
     */
    String name();

    /**
     * @return 环境名称
     * dev test pre_prod prod
     * @since 2024.06
     */
    String env();
}
