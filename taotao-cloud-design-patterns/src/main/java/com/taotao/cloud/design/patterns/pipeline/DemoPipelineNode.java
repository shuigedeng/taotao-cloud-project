package com.taotao.cloud.design.patterns.pipeline;


import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.json.JSONUtil;

/**
 * 审核-管道节点
 *
 */
@Slf4j
public class DemoPipelineNode implements PipelineNode<DemoPipelineProduct.DemoSignalEnum, DemoPipelineProduct> {

    /**
     * 下一管道节点
     */
    private DemoPipelineNode next;

    /**
     * 当前管道任务
     */
    private PipelineJob<DemoPipelineProduct> job;

    /**
     * 节点组装，按照上个管道任务传递的信号，执行 pipelineJob
     *
     * @param pipelineJob 管道任务
     * @return {@link DemoPipelineNode}
     */
    @Override
    public DemoPipelineNode flax(PipelineJob<DemoPipelineProduct> pipelineJob) {
        return flax(DemoPipelineProduct.DemoSignalEnum.NORMAL, pipelineJob);
    }

    /**
     * 节点组装，按照传递的信号,判断当前管道的信号是否相等，执行 pipelineJob
     *
     * @param signal      信号
     * @param pipelineJob 管道任务
     * @return {@link DemoPipelineNode}
     */
    @Override
    public DemoPipelineNode flax(DemoPipelineProduct.DemoSignalEnum signal, PipelineJob<DemoPipelineProduct> pipelineJob) {
        return flax(signal::equals, pipelineJob);
    }

    /**
     * 节点组装，上个管道过来的信号运行 predicate 后是true的话，执行 pipelineJob
     *
     * @param predicate
     * @param pipelineJob
     * @return
     */
    @Override
    public DemoPipelineNode flax(Predicate<DemoPipelineProduct.DemoSignalEnum> predicate,
                                 PipelineJob<DemoPipelineProduct> pipelineJob) {
        this.next = new DemoPipelineNode();
        this.job = (job) -> {
            if (predicate.test(job.getSignal())) {
                return pipelineJob.execute(job);
            } else {
                return job;
            }
        };
        return next;
    }

    /**
     * 管道节点-任务执行
     *
     * @param product 管道产品
     * @return
     */
    @Override
    public DemoPipelineProduct execute(DemoPipelineProduct product) {
        // 执行当前任务
        try {
            product = job == null ? product : job.execute(product);
            return next == null ? product : next.execute(product);
        } catch (Exception e) {
            log.error("流水线处理异常：流程Id=【{}】,任务=【{}】,参数=【{}】", product.getTradeId(), ClassUtil.getClassName(job, true), JSONUtil.toJsonStr(product.getProductData()), product.getException());
            return null;
        }

    }
}
