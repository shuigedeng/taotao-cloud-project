/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.api.dto.quartz.QuartzLogDto;
import com.taotao.cloud.sys.api.dto.quartz.QuartzLogQueryCriteria;
import com.taotao.cloud.sys.biz.entity.quartz.QuartzLog;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;

public interface IQuartzLogService extends IService<QuartzLog> {

	/**
	 * 查询数据分页
	 *
	 * @param criteria 条件
	 * @param pageable 分页参数
	 * @return Map<String, Object>
	 */
	Map<String, Object> queryAll(QuartzLogQueryCriteria criteria, Pageable pageable);

	/**
	 * 查询所有数据不分页
	 *
	 * @param criteria 条件参数
	 * @return List<QuartzLogDto>
	 */
	List<QuartzLog> queryAll(QuartzLogQueryCriteria criteria);

	/**
	 * 导出数据
	 *
	 * @param all      待导出的数据
	 * @param response /
	 * @throws IOException /
	 */
	void download(List<QuartzLogDto> all, HttpServletResponse response) throws IOException;
}
