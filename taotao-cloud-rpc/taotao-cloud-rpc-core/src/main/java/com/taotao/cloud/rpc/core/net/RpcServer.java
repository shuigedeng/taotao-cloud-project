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

package com.taotao.cloud.rpc.core.net;

import com.taotao.cloud.rpc.common.exception.RpcException;

public interface RpcServer {

    void start();

    <T> void publishService(T service, String serviceClass) throws RpcException;

    <T> void publishService(T service, String groupName, String serviceClass) throws RpcException;

    /**
     * @param fullName         全类名
     * @param simpleName       忽略包类名
     * @param firstLowCaseName 首字母小写类名
     * @param clazz            Class 类，可用于发射
     * @return
     * @throws Exception
     */
    Object newInstance(String fullName, String simpleName, String firstLowCaseName, Class<?> clazz)
            throws Exception;
}
