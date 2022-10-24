package com.taotao.cloud.job.quartz.service;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.job.quartz.entity.QuartzJobLog;
import com.taotao.cloud.job.quartz.param.QuartzJobLogQuery;
import com.taotao.cloud.job.quartz.vo.QuartzJobLogVO;

/**
 * 定时任务日志
 */
public interface QuartzJobLogService {

	/**
	 * 添加
	 */
	public void add(QuartzJobLog quartzJobLog);

	/**
	 * 分页
	 */
	public PageResult<QuartzJobLogVO> page(QuartzJobLogQuery quartzJobLogQuery);

	/**
	 * 单条
	 */
	public QuartzJobLogVO findById(Long id);


}
