package com.taotao.cloud.job.worker.processor.type;


import com.taotao.cloud.job.worker.processor.ProcessResult;
import com.taotao.cloud.job.worker.processor.task.TaskContext;

/**
 * 基础的处理器，适用于单机执行
 *
 * @author shuigedeng
 * @since 2020/3/18
 */
public interface BasicProcessor {

	/**
	 * 核心处理逻辑
	 *
	 * @param context 任务上下文，可通过 jobParams 和 instanceParams 分别获取控制台参数和OpenAPI传递的任务实例参数
	 * @return 处理结果，msg有长度限制，超长会被裁剪，不允许返回 null
	 * @throws Exception 异常，允许抛出异常，但不推荐，最好由业务开发者自己处理
	 */
	ProcessResult process(TaskContext context) throws Exception;
}
