package com.taotao.cloud.job.biz.quartz.service;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.job.api.model.vo.QuartzJobVO;
import com.taotao.cloud.job.biz.quartz.entity.QuartzJobEntity;
import com.taotao.cloud.job.biz.quartz.param.QuartzJobDTO;
import com.taotao.cloud.job.biz.quartz.param.QuartzJobQuery;
import org.quartz.SchedulerException;

/**
 * 石英工作服务
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-06 09:02:48
 */
public interface QuartzJobService {

	void init() throws SchedulerException;

	/**
	 * 添加任务
	 *
	 * @param quartzJobDTO 石英工作dto
	 * @since 2022-09-06 09:02:48
	 */
	void addJob(QuartzJobDTO quartzJobDTO);

	/**
	 * 更新任务
	 *
	 * @param quartzJobDTO 石英工作dto
	 * @since 2022-09-06 09:02:48
	 */
	void updateJob(QuartzJobDTO quartzJobDTO);

	/**
	 * 立即运行一次定时任务
	 *
	 * @param id id
	 * @since 2022-09-06 09:02:48
	 */
	void runOnce(Long id);

	/**
	 * 开始任务
	 *
	 * @param id id
	 * @since 2022-09-06 09:02:48
	 */
	void start(Long id);

	/**
	 * 停止任务
	 *
	 * @param id id
	 * @since 2022-09-06 09:02:48
	 */
	void stopJob(Long id);

	/**
	 * 删除任务
	 *
	 * @param id
	 */
	void deleteJob(Long id);

	/**
	 * 同步状态
	 *
	 * @since 2022-09-06 09:02:59
	 */
	void syncJobStatus();

	/**
	 * 启动所有任务
	 *
	 * @since 2022-09-06 09:03:03
	 */
	void startAllJobs();

	/**
	 * 暂停所有任务
	 *
	 * @since 2022-09-06 09:03:05
	 */
	void pauseAllJobs();

	/**
	 * 恢复所有任务
	 *
	 * @since 2022-09-06 09:03:06
	 */
	void resumeAllJobs();

	/**
	 * 关闭所有任务
	 *
	 * @since 2022-09-06 09:03:08
	 */
	void shutdownAllJobs();

	QuartzJobEntity findById(Long id);

	PageResult<QuartzJobVO> page(QuartzJobQuery quartzJobQuery);

	/**
	 * 判断是否是定时任务类
	 */
	String judgeJobClass(String jobClassName);
}
