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

package com.taotao.cloud.rpc.client.support.filter;

import com.taotao.cloud.rpc.client.proxy.RemoteInvokeContext;

/**
 * <p> 调用上下文 </p>
 *
 * <pre> Created: 2019/10/26 9:30 上午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * 核心目的：
 * （1）用于定义 filter 相关信息
 * （2）用于 load-balance 相关信息处理
 * （3）后期的路由-分区 都可以视为这样的一个抽象实现而已。
 *
 * 插件式实现：
 * （1）远程调用也认为是一次 filter，上下文中构建 filter-chain
 * （2）filter-chain 可以使用 {@link Pipeline} 管理
 *
 *
 * 后期拓展：
 * （1）类似于 aop，用户可以自行定义 interceptor 拦截器
 *
 * @since 2024.06
 */
public interface RpcFilter {

    /**
     * filter 处理
     * @param context 上下文
     * @since 2024.06
     */
    void filter(final RemoteInvokeContext context);
}
