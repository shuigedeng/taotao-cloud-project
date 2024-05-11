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
