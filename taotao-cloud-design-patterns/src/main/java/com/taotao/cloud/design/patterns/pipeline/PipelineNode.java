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

package com.taotao.cloud.design.patterns.pipeline;

import java.util.function.Predicate;

/**
 * 管道节点定义
 *
 * @param <S> 信号量
 * @param <P> 管道产品
 */
public interface PipelineNode<S, P extends PipelineProduct<S>> {
    /**
     * 节点组装，按照上个管道任务传递的信号，执行 pipelineJob
     *
     * @param pipelineJob 管道任务
     * @return {@link PipelineNode}<{@link S},  {@link P}>
     */
    PipelineNode<S, P> flax(PipelineJob<P> pipelineJob);

    /**
     * 节点组装，按照传递的信号,判断当前管道的信号是否相等，执行 pipelineJob
     *
     * @param signal      信号
     * @param pipelineJob 管道任务
     * @return {@link PipelineNode}<{@link S},  {@link P}>
     */
    PipelineNode<S, P> flax(S signal, PipelineJob<P> pipelineJob);

    /**
     * 节点组装，按照传递的信号,判断当前管道的信号是否相等，执行 pipelineJob
     *
     * @param predicate   信号
     * @param pipelineJob 管道任务
     * @return {@link PipelineNode}<{@link S},  {@link P}>
     */
    PipelineNode<S, P> flax(Predicate<S> predicate, PipelineJob<P> pipelineJob);

    /**
     * 管道节点-任务执行
     *
     * @param product 管道产品
     * @return {@link P}
     */
    P execute(P product);
}
