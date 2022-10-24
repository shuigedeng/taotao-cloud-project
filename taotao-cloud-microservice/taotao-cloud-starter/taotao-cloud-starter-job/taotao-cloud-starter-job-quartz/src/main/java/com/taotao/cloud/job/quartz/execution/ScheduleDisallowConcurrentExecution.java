package com.taotao.cloud.job.quartz.execution;

import org.quartz.DisallowConcurrentExecution;

/**
 * 禁止并发
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-06 09:03:26
 */
@DisallowConcurrentExecution
public class ScheduleDisallowConcurrentExecution extends AbstractQuartzExecutionJob {

}
