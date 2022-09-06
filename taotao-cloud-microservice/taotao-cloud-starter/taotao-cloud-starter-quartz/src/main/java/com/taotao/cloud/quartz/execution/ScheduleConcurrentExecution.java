package com.taotao.cloud.quartz.execution;

import com.taotao.cloud.quartz.execution.AbstractQuartzExecutionJob;

/**
 * 允许并发（不会等待上一次任务执行完毕，只要时间到就会执行）
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-06 09:03:33
 */
public class ScheduleConcurrentExecution extends AbstractQuartzExecutionJob {

}
