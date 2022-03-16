/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.api.dto.quartz.QuartzJobDto;
import com.taotao.cloud.sys.api.dto.quartz.QuartzJobQueryCriteria;
import com.taotao.cloud.sys.biz.entity.quartz.QuartzJob;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;

public interface IQuartzJobService extends IService<QuartzJob> {

	/**
	 * 查询数据分页
	 *
	 * @param criteria 条件
	 * @param pageable 分页参数
	 * @return Map<String, Object>
	 */
	Map<String, Object> queryAll(QuartzJobQueryCriteria criteria, Pageable pageable);

	/**
	 * 查询所有数据不分页
	 *
	 * @param criteria 条件参数
	 * @return List<QuartzJobDto>
	 */
	List<QuartzJob> queryAll(QuartzJobQueryCriteria criteria);

	/**
	 * 导出数据
	 *
	 * @param all      待导出的数据
	 * @param response /
	 * @throws IOException /
	 */
	void download(List<QuartzJobDto> all, HttpServletResponse response) throws IOException;

	/**
	 * 更改定时任务状态
	 *
	 * @param quartzJob /
	 */
	void updateIsPause(QuartzJob quartzJob);

	/**
	 * 立即执行定时任务
	 *
	 * @param quartzJob /
	 */
	void execution(QuartzJob quartzJob);

	/**
	 * 查询启用的任务
	 *
	 * @return List
	 */
	List<QuartzJob> findByIsPauseIsFalse();

	void removeByIds(List<Integer> idList);
}
