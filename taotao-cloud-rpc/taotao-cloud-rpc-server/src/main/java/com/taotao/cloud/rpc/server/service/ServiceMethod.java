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

import java.lang.reflect.Method;
import java.util.List;

/**
 * 服务方法类
 *
 * @author shuigedeng
 * @since 2024.06
 */
public interface ServiceMethod {

    /**
     * 方法名称
     * @return 名称
     * @since 2024.06
     */
    String name();

    /**
     * 方法类型列表
     * @return 方法类型列表
     * @since 2024.06
     */
    Class[] paramTypes();

    /**
     * 方法类型名称列表
     * @return 方法名称列表
     * @since 2024.06
     */
    List<String> paramTypeNames();

    /**
     * 方法信息
     * @return 方法信息
     * @since 2024.06
     */
    Method method();
}
