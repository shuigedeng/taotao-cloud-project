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

package com.taotao.cloud.rpc.common.common.support.inteceptor;

/**
 * rpc 拦截器
 *
 * 【调用示意流程】
 *
 * <pre>
 *
 * remoteCall() {
 *
 *     try() {
 *          before();
 *
 *         //.... 原来的调用逻辑
 *
 *         after();
 *     } catch(Ex ex) {
 *         ex();
 *     }
 *
 * }
 * </pre>
 *
 * 【拦截器 chain】
 * 将多个拦截器视为一个拦截器。
 * 保证接口的纯粹与统一。
 *
 * @author shuigedeng
 * @since 0.1.4
 */
public interface RpcInterceptor {

    /**
     * 开始
     * @param context 上下文
     * @since 0.1.4
     */
    void before(final RpcInterceptorContext context);

    /**
     * 结束
     * @param context 上下文
     * @since 0.1.4
     */
    void after(final RpcInterceptorContext context);

    /**
     * 异常处理
     * @param context 上下文
     * @since 0.1.4
     */
    void exception(final RpcInterceptorContext context);
}
