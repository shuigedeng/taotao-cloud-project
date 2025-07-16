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

package com.taotao.cloud.rpc.common.common.support.inteceptor.impl;

import com.taotao.cloud.rpc.common.common.rpc.DefaultPipeline;
import com.taotao.cloud.rpc.common.common.rpc.Pipeline;
import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptor;
import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptorContext;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象的初始化拦截器
 *
 * @author shuigedeng
 * @since 0.2.2
 */
public abstract class AbstractRpcInterceptorInit extends RpcInterceptorAdaptor {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRpcInterceptorInit.class);

    /**
     * 初始化监听器列表
     *
     * @param pipeline 泳道
     * @param context  重试信息
     * @since 0.2.0
     */
    protected abstract void init(
            final Pipeline<RpcInterceptor> pipeline, final RpcInterceptorContext context);

    @Override
    public void before(RpcInterceptorContext context) {
        Pipeline<RpcInterceptor> pipeline = new DefaultPipeline<>();
        this.init(pipeline, context);

        List<RpcInterceptor> filterList = pipeline.list();

        for (RpcInterceptor filter : filterList) {
            filter.before(context);
        }
    }

    @Override
    public void after(RpcInterceptorContext context) {
        Pipeline<RpcInterceptor> pipeline = new DefaultPipeline<>();
        this.init(pipeline, context);

        List<RpcInterceptor> filterList = pipeline.list();

        for (RpcInterceptor filter : filterList) {
            filter.after(context);
        }
    }
}
