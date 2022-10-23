package com.taotao.cloud.quartz.service;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.quartz.vo.QuartzJobLogVO;
import com.taotao.cloud.quartz.entity.QuartzJobLog;
import com.taotao.cloud.quartz.param.QuartzJobLogQuery;

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
