package com.taotao.cloud.job.core.worker.processor;


/**
 * 内部使用的 Processor 加载器
 *
 * @author shuigedeng
 * @since 2023/1/20
 */
public interface ProcessorLoader {

    ProcessorBean load(ProcessorDefinition definition);
}
