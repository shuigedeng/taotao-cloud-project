package com.taotao.cloud.web.schedule;

/**
 * 任务信息接口
 *
 * @author jitwxs
 * @date 2021年03月27日 16:29
 */
public interface IDSTaskInfo {

	/**
	 * 任务 ID
	 */
	long getId();

	/**
	 * 任务执行 cron 表达式
	 */
	String getCron();

	/**
	 * 任务是否有效
	 */
	boolean isValid();

	/**
	 * 判断任务是否发生变化
	 */
	boolean isChange(IDSTaskInfo oldTaskInfo);
}
